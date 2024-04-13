package net.dnadas.freeroam_summits.service.geolocation;

import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import net.dnadas.freeroam_summits.dto.geolocation.ElevationApiErrorResponseDto;
import net.dnadas.freeroam_summits.dto.geolocation.ElevationApiResponseDto;
import net.dnadas.freeroam_summits.dto.geolocation.GeoLocationResponseDto;
import net.dnadas.freeroam_summits.exception.geolocation.ElevationUnavailableException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GeoLocationService {
  private final WebClient.Builder webClientBuilder;
  private final String ELEVATION_API_URL;
  private final Validator validator;

  public GeoLocationService(
    @Qualifier("nonLoadBalancedWebClientBuilder") WebClient.Builder webClientBuilder,
    @Value("${ELEVATION_API_URL}") String elevationApiUrl, Validator validator) {
    this.webClientBuilder = webClientBuilder;
    this.ELEVATION_API_URL = elevationApiUrl;
    this.validator = validator;
  }

  public Mono<GeoLocationResponseDto> getElevation(Double latitude, Double longitude) {
    return webClientBuilder.build().get().uri(
        ELEVATION_API_URL + String.format("?latitude=%f&longitude=%f", latitude, longitude))
      .retrieve()
      .onStatus(
        HttpStatusCode::isError,
        response -> response.bodyToMono(ElevationApiErrorResponseDto.class)
          .flatMap(responseDto -> Mono.error(
            new ElevationUnavailableException(responseDto.reason(), response.statusCode().value())))
      )
      .bodyToMono(ElevationApiResponseDto.class)
      .handle((responseDto, sink) -> {
        var violations = validator.validate(responseDto);
        if (!violations.isEmpty()) {
          log.error("Validation failed for ElevationApiResponseDto: {}", violations);
          sink.error(new ElevationUnavailableException("Validation errors occurred"));
        } else {
          sink.next(
            new GeoLocationResponseDto(latitude, longitude, responseDto.elevation().get(0)));
        }
      });
  }
}

package net.dnadas.freeroam_summits.service.geolocation;

import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import net.dnadas.freeroam_summits.dto.geolocation.*;
import net.dnadas.freeroam_summits.exception.geolocation.ElevationUnavailableException;
import net.dnadas.freeroam_summits.exception.geolocation.LocationUnavailableException;
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
  private final String REVERSE_GEOCODING_API_URL;
  private static final int REVERSE_GEOCODING_MAX_ZOOM = 15;
  private final String GEOCODING_AUTOCOMPLETE_API_URL;
  private final Validator validator;

  public GeoLocationService(
    @Qualifier("nonLoadBalancedWebClientBuilder") WebClient.Builder webClientBuilder,
    @Value("${FREEROAM_SUMMITS_ELEVATION_API_URL}") String elevationApiUrl,
    @Value("${FREEROAM_SUMMITS_REVERSE_GEOCODING_API_URL}") String reverseGeocodingApiUrl,
    @Value("${FREEROAM_SUMMITS_GEOCODING_AUTOCOMPLETE_API_URL}") String autocompleteApiUrl,
    Validator validator) {
    this.webClientBuilder = webClientBuilder;
    this.ELEVATION_API_URL = elevationApiUrl;
    REVERSE_GEOCODING_API_URL = reverseGeocodingApiUrl;
    GEOCODING_AUTOCOMPLETE_API_URL = autocompleteApiUrl;
    this.validator = validator;
  }

  public Mono<DetailedGeoLocationDto> getGeoLocationDetails(Double latitude, Double longitude) {
    return Mono.zip(
      getElevation(latitude, longitude),
      getGeoJson(latitude, longitude),
      (elevationResponse, geoJsonResponse) -> {
        NominatimApiGeoJsonResponseDto.Properties properties =
          geoJsonResponse.features().isEmpty() ? null : geoJsonResponse.features().get(0)
            .properties();
        Double elevation = elevationResponse.elevation().get(0);
        return new DetailedGeoLocationDto(
          latitude, longitude, elevation,
          properties != null ? properties.display_name() : "No details available",
          properties
        );
      }
    );
  }

  public Mono<OpenMeteoApiAutoCompleteResponseDto> searchGeoLocations(String query) {
    return webClientBuilder.build().get().uri(
        GEOCODING_AUTOCOMPLETE_API_URL +
          String.format("?name=%s&count=10&language=en&format=json", query))
      .retrieve()
      .onStatus(
        HttpStatusCode::isError,
        response -> response.bodyToMono(OpenMeteoApiErrorResponseDto.class)
          .flatMap(responseDto -> Mono.error(
            new LocationUnavailableException(responseDto.reason(), response.statusCode().value())))
      )
      .bodyToMono(OpenMeteoApiAutoCompleteResponseDto.class)
      .handle((responseDto, sink) -> {
        var violations = validator.validate(responseDto);
        if (!violations.isEmpty()) {
          log.error("Validation failed for OpenMeteoApiAutoCompleteResponseDto: {}", violations);
          sink.error(new LocationUnavailableException());
        } else {
          sink.next(responseDto);
        }
      });
  }

  private Mono<OpenMeteoApiElevationResponseDto> getElevation(Double latitude, Double longitude) {
    return webClientBuilder.build().get().uri(
        ELEVATION_API_URL + String.format("?latitude=%f&longitude=%f", latitude, longitude))
      .retrieve()
      .onStatus(
        HttpStatusCode::isError,
        response -> response.bodyToMono(OpenMeteoApiErrorResponseDto.class)
          .flatMap(responseDto -> Mono.error(
            new ElevationUnavailableException(responseDto.reason(), response.statusCode().value())))
      )
      .bodyToMono(OpenMeteoApiElevationResponseDto.class)
      .handle((responseDto, sink) -> {
        var violations = validator.validate(responseDto);
        if (!violations.isEmpty()) {
          log.error("Validation failed for OpenMeteoApiElevationResponseDto: {}", violations);
          sink.error(new ElevationUnavailableException());
        } else {
          sink.next(responseDto);
        }
      });
  }

  private Mono<NominatimApiGeoJsonResponseDto> getGeoJson(
    Double latitude, Double longitude) {
    return webClientBuilder.build().get().uri(
        REVERSE_GEOCODING_API_URL +
          String.format(
            "?lat=%f&lon=%f&format=geojson&zoom=%d", latitude, longitude, REVERSE_GEOCODING_MAX_ZOOM))
      .retrieve()
      .onStatus(
        HttpStatusCode::isError,
        response -> response.bodyToMono(NominatimApiErrorResponseDto.class)
          .flatMap(responseDto -> Mono.error(
            new LocationUnavailableException(responseDto.message(), responseDto.code())))
      )
      .bodyToMono(NominatimApiGeoJsonResponseDto.class)
      .handle((responseDto, sink) -> {
        var violations = validator.validate(responseDto);
        if (!violations.isEmpty()) {
          log.error("Validation failed for NominatimApiGeoJsonResponseDto: {}", violations);
          sink.error(new LocationUnavailableException());
        } else {
          sink.next(responseDto);
        }
      });
  }
}

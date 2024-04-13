package net.dnadas.freeroam_summits.service.security;

import lombok.extern.slf4j.Slf4j;
import net.dnadas.freeroam_summits.dto.security.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Service
@Slf4j
public class S2SAuthenticationService {
  private final WebClient.Builder webClientBuilder;
  private final String clientId;
  private final String clientSecret;
  private final String tokenUri;

  @Autowired
  public S2SAuthenticationService(
    @Qualifier("nonLoadBalancedWebClientBuilder") WebClient.Builder webClientBuilder,
    @Value("${S2S-CLIENT-ID}") String clientId, @Value("${S2S-CLIENT-SECRET}") String clientSecret,
    @Value("${S2S-TOKEN-URI}") String tokenUri) {
    this.webClientBuilder = webClientBuilder;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.tokenUri = tokenUri;
  }

  public Mono<String> getAccessToken() {
    String authHeader = Base64.getEncoder().encodeToString(
      (clientId + ":" + clientSecret).getBytes());

    return webClientBuilder.build()
      .post()
      .uri(tokenUri)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Authorization", "Basic " + authHeader) // Using Basic Auth here
      .bodyValue("grant_type=client_credentials")
      .retrieve()
      .bodyToMono(TokenResponseDto.class)
      .map(TokenResponseDto::access_token);
  }
}

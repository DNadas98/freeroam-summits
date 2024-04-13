package net.dnadas.freeroam_summits.controller;

import lombok.RequiredArgsConstructor;
import net.dnadas.freeroam_summits.service.security.S2SAuthenticationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TestController {
  private final S2SAuthenticationService s2SAuthenticationService;
  private final WebClient.Builder webClientBuilder;

  @GetMapping("/protected/hello")
  public String hello() {
    return "Hello from FreeRoamSummitsApplication protected route!";
  }

  @GetMapping("/public/hello")
  public String hello2() {
    return "Hello from FreeRoamSummitsApplication public route!";
  }

  @GetMapping("/public/trails")
  public Mono<String> trails() {
    return s2SAuthenticationService.getAccessToken().flatMap(token ->
      webClientBuilder.build()
        .get()
        .uri("lb://FREEROAM-TRAILS-SERVICE/protected/hello")
        .header("Authorization", "Bearer " + token)
        .retrieve()
        .bodyToMono(String.class));
  }
}

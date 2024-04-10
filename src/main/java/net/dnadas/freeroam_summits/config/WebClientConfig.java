package net.dnadas.freeroam_summits.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
  @Bean
  @Primary
  @LoadBalanced
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean(name = "nonLoadBalancedWebClientBuilder")
  public WebClient.Builder nonLoadBalancedWebClientBuilder() {
    return WebClient.builder();
  }
}

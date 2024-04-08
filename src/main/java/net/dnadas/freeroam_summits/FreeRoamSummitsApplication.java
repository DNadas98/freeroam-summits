package net.dnadas.freeroam_summits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FreeRoamSummitsApplication {
  public static void main(String[] args) {
    SpringApplication.run(FreeRoamSummitsApplication.class, args);
  }
}

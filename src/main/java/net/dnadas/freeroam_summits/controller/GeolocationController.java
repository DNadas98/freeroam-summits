package net.dnadas.freeroam_summits.controller;

import lombok.RequiredArgsConstructor;
import net.dnadas.freeroam_summits.dto.geolocation.GeoLocationResponseDto;
import net.dnadas.freeroam_summits.service.geolocation.GeoLocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public/geolocation")
@RequiredArgsConstructor
public class GeolocationController {
  private final GeoLocationService geoLocationService;

  @GetMapping("/elevation")
  public Mono<GeoLocationResponseDto> getElevation(
    @RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
    return geoLocationService.getElevation(lat, lng);
  }
}

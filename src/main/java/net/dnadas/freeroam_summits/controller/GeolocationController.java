package net.dnadas.freeroam_summits.controller;

import lombok.RequiredArgsConstructor;
import net.dnadas.freeroam_summits.dto.geolocation.DetailedGeoLocationDto;
import net.dnadas.freeroam_summits.dto.geolocation.OpenMeteoApiAutoCompleteResponseDto;
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

  @GetMapping("/details")
  public Mono<DetailedGeoLocationDto> getGeoLocationDetails(
    @RequestParam("latitude") Double lat, @RequestParam("longitude") Double lng) {
    return geoLocationService.getGeoLocationDetails(lat, lng);
  }

  @GetMapping("/search")
  public Mono<OpenMeteoApiAutoCompleteResponseDto> searchGeoLocations(
    @RequestParam("query") String query) {
    return geoLocationService.searchGeoLocations(query);
  }
}

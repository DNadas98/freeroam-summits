package net.dnadas.freeroam_summits.dto.geolocation;

import java.util.List;

public record NominatimApiGeoJsonResponseDto(String type, String licence, List<Feature> features) {

  public record Feature(
    String type,
    Properties properties,
    List<Double> bbox,
    Geometry geometry
  ) {
  }

  public record Properties(
    Long place_id,
    String osm_type,
    Long osm_id,
    Integer place_rank,
    String category,
    String type,
    Double importance,
    String addresstype,
    String name,
    String display_name,
    Address address
  ) {
  }

  public record Address(
    String road,
    String neighbourhood,
    String suburb,
    String borough,
    String city,
    String county,
    String state_district,
    String state,
    String postcode,
    String country,
    String country_code,
    String region,
    String ISO3166_2_lvl4,
    String ISO3166_2_lvl6
  ) {
  }

  public record Geometry(
    String type,
    List<Double> coordinates
  ) {
  }
}
package net.dnadas.freeroam_summits.dto.geolocation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OpenMeteoApiAutoCompleteResponseDto(
  @NotNull @NotEmpty List<Result> results
) {
  public record Result(
    @NotNull Long id,
    @NotNull String name,
    @NotNull Double latitude,
    @NotNull Double longitude,
    Double elevation,
    String feature_code,
    @NotNull String country_code,
    Long admin1_id,
    Long admin3_id,
    Long admin4_id,
    String timezone,
    Integer population,
    List<String> postcodes,
    Long country_id,
    String country,
    String admin1,
    String admin3,
    String admin4
  ) {
  }
}

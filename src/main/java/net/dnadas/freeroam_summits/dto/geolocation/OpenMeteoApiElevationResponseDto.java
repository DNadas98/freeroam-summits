package net.dnadas.freeroam_summits.dto.geolocation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OpenMeteoApiElevationResponseDto(@NotNull @NotEmpty List<Double> elevation) {
}

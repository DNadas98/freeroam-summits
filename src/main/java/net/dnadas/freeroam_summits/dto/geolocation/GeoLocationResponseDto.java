package net.dnadas.freeroam_summits.dto.geolocation;

import jakarta.validation.constraints.NotNull;

public record GeoLocationResponseDto(@NotNull Double latitude, @NotNull Double longitude,
                                     @NotNull Double elevation) {
}

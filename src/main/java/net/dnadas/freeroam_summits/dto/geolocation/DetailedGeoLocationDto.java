package net.dnadas.freeroam_summits.dto.geolocation;

public record DetailedGeoLocationDto(Double latitude, Double longitude, Double elevation,
                                     String displayName,
                                     NominatimApiGeoJsonResponseDto.Properties properties) {
}

package net.dnadas.freeroam_summits.exception.geolocation;

import lombok.Getter;

@Getter
public class GeoLocationUnavailableException extends RuntimeException {
  private final Integer statusCode;

  public GeoLocationUnavailableException(String message, Integer statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public GeoLocationUnavailableException(String message) {
    super(message);
    this.statusCode = 500;
  }

  public GeoLocationUnavailableException() {
    super("Failed to retrieve the requested geolocation");
    this.statusCode = 500;
  }
}

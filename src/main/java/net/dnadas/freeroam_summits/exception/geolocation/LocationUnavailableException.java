package net.dnadas.freeroam_summits.exception.geolocation;

import lombok.Getter;

@Getter
public class LocationUnavailableException extends RuntimeException {
  private final Integer statusCode;

  public LocationUnavailableException(String message, Integer statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public LocationUnavailableException(String message) {
    super(message);
    this.statusCode = 500;
  }

  public LocationUnavailableException() {
    super("Failed to retrieve the requested location data");
    this.statusCode = 500;
  }
}

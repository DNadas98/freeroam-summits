package net.dnadas.freeroam_summits.exception.geolocation;

import lombok.Getter;

@Getter
public class ElevationUnavailableException extends RuntimeException {
  private final Integer statusCode;

  public ElevationUnavailableException(String message, Integer statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public ElevationUnavailableException(String message) {
    super(message);
    this.statusCode = 500;
  }

  public ElevationUnavailableException() {
    super("Failed to retrieve elevation data for the requested geolocation");
    this.statusCode = 500;
  }
}

package net.dnadas.freeroam_summits.model.geolocation;

public enum NominatimLayer {
  POI, NATURAL, ADDRESS;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}

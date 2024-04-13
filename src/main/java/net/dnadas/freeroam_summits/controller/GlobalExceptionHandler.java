package net.dnadas.freeroam_summits.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.dnadas.freeroam_summits.dto.ErrorResponseDto;
import net.dnadas.freeroam_summits.exception.geolocation.ElevationUnavailableException;
import net.dnadas.freeroam_summits.exception.geolocation.LocationUnavailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {LocationUnavailableException.class})
  public ResponseEntity<ErrorResponseDto> handleLocationUnavailableException(
    LocationUnavailableException e) {
    log.error(e.getMessage());
    return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponseDto(e.getMessage()));
  }

  @ExceptionHandler(value = {ElevationUnavailableException.class})
  public ResponseEntity<ErrorResponseDto> handleElevationUnavailableException(
    ElevationUnavailableException e) {
    log.error(e.getMessage());
    return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponseDto(e.getMessage()));
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
    ConstraintViolationException e) {
    log.error(e.getMessage());
    return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
  }
}

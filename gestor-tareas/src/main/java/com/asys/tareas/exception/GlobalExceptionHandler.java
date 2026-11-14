package com.asys.tareas.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RecursoNoEncontradoException.class)
  public ResponseEntity<ErrorResponse> manejarNoEncontrado(
      RecursoNoEncontradoException ex, HttpServletRequest request) {
    return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request);
  }

  @ExceptionHandler(ReglaNegocioException.class)
  public ResponseEntity<ErrorResponse> manejarReglaNegocio(
      ReglaNegocioException ex, HttpServletRequest request) {
    return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<ErrorResponse> manejarSolicitudInvalida(
      Exception ex, HttpServletRequest request) {
    return construir(HttpStatus.BAD_REQUEST, "La solicitud tiene datos inválidos", request);
  }

  private ResponseEntity<ErrorResponse> construir(
      HttpStatus estado, String mensaje, HttpServletRequest request) {
    ErrorResponse cuerpo = new ErrorResponse(estado.value(), mensaje, request.getRequestURI());
    return ResponseEntity.status(estado).body(cuerpo);
  }
}

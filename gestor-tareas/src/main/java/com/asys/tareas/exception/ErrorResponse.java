package com.asys.tareas.exception;

public record ErrorResponse(int estado, String mensaje, String ruta) {}

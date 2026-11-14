package com.asys.tareas.service.regla;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Tarea;
import org.junit.jupiter.api.Test;

class TituloLongitudReglaTest {

  private final TituloLongitudRegla regla = new TituloLongitudRegla();

  @Test
  void aceptaTituloEnElLimite() {
    String titulo = "a".repeat(TituloLongitudRegla.MAXIMO);
    assertDoesNotThrow(() -> regla.validar(tareaConTitulo(titulo)));
  }

  @Test
  void rechazaTituloMuyLargo() {
    String titulo = "a".repeat(TituloLongitudRegla.MAXIMO + 1);
    assertThrows(ReglaNegocioException.class, () -> regla.validar(tareaConTitulo(titulo)));
  }

  @Test
  void ignoraTituloNulo() {
    assertDoesNotThrow(() -> regla.validar(tareaConTitulo(null)));
  }

  private Tarea tareaConTitulo(String titulo) {
    Tarea tarea = new Tarea();
    tarea.setTitulo(titulo);
    return tarea;
  }
}

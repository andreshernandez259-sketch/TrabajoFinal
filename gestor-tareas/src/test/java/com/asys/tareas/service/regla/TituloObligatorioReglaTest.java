package com.asys.tareas.service.regla;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Tarea;
import org.junit.jupiter.api.Test;

class TituloObligatorioReglaTest {

  private final TituloObligatorioRegla regla = new TituloObligatorioRegla();

  @Test
  void aceptaTituloValido() {
    assertDoesNotThrow(() -> regla.validar(tareaConTitulo("Estudiar JPA")));
  }

  @Test
  void rechazaTituloNulo() {
    assertThrows(ReglaNegocioException.class, () -> regla.validar(tareaConTitulo(null)));
  }

  @Test
  void rechazaTituloEnBlanco() {
    assertThrows(ReglaNegocioException.class, () -> regla.validar(tareaConTitulo("   ")));
  }

  private Tarea tareaConTitulo(String titulo) {
    Tarea tarea = new Tarea();
    tarea.setTitulo(titulo);
    return tarea;
  }
}

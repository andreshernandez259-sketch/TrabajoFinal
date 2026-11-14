package com.asys.tareas.service.regla;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Tarea;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class FechaLimiteFuturaReglaTest {

  private static final LocalDate HOY = LocalDate.of(2026, 11, 14);

  private final Clock reloj = Clock.fixed(Instant.parse("2026-11-14T12:00:00Z"), ZoneOffset.UTC);
  private final FechaLimiteFuturaRegla regla = new FechaLimiteFuturaRegla(reloj);

  @Test
  void aceptaTareaSinFechaLimite() {
    assertDoesNotThrow(() -> regla.validar(tareaConFecha(null)));
  }

  @Test
  void aceptaFechaDeHoy() {
    assertDoesNotThrow(() -> regla.validar(tareaConFecha(HOY)));
  }

  @Test
  void aceptaFechaFutura() {
    assertDoesNotThrow(() -> regla.validar(tareaConFecha(HOY.plusDays(1))));
  }

  @Test
  void rechazaFechaPasada() {
    assertThrows(ReglaNegocioException.class, () -> regla.validar(tareaConFecha(HOY.minusDays(1))));
  }

  private Tarea tareaConFecha(LocalDate fecha) {
    Tarea tarea = new Tarea();
    tarea.setTitulo("Tarea");
    tarea.setFechaLimite(fecha);
    return tarea;
  }
}

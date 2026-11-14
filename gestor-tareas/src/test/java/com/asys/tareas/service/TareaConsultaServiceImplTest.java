package com.asys.tareas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.asys.tareas.exception.RecursoNoEncontradoException;
import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Estado;
import com.asys.tareas.model.Prioridad;
import com.asys.tareas.model.Tarea;
import com.asys.tareas.repository.TareaRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TareaConsultaServiceImplTest {

  private static final LocalDate HOY = LocalDate.of(2026, 11, 14);

  @Mock private TareaRepository repositorio;

  private TareaConsultaServiceImpl servicio;

  @BeforeEach
  void preparar() {
    Clock reloj = Clock.fixed(Instant.parse("2026-11-14T12:00:00Z"), ZoneOffset.UTC);
    servicio = new TareaConsultaServiceImpl(repositorio, reloj);
  }

  @Test
  void buscarPorIdDevuelveLaTarea() {
    Tarea tarea = new Tarea();
    when(repositorio.findById(1L)).thenReturn(Optional.of(tarea));

    assertSame(tarea, servicio.buscarPorId(1L));
  }

  @Test
  void buscarPorIdLanzaExcepcionSiNoExiste() {
    when(repositorio.findById(99L)).thenReturn(Optional.empty());

    assertThrows(RecursoNoEncontradoException.class, () -> servicio.buscarPorId(99L));
  }

  @Test
  void listarPorEstadoConsultaAlRepositorio() {
    when(repositorio.findByEstado(Estado.PENDIENTE)).thenReturn(List.of(new Tarea()));

    assertEquals(1, servicio.listarPorEstado(Estado.PENDIENTE).size());
  }

  @Test
  void listarPorPrioridadConsultaAlRepositorio() {
    when(repositorio.findByPrioridad(Prioridad.ALTA)).thenReturn(List.of(new Tarea()));

    assertEquals(1, servicio.listarPorPrioridad(Prioridad.ALTA).size());
  }

  @Test
  void buscarPorTituloConsultaAlRepositorio() {
    when(repositorio.findByTituloContainingIgnoreCase("est")).thenReturn(List.of(new Tarea()));

    assertEquals(1, servicio.buscarPorTitulo("est").size());
  }

  @Test
  void listarVencidasUsaLaFechaDeHoyYExcluyeCompletadas() {
    when(repositorio.findByFechaLimiteBeforeAndEstadoNot(HOY, Estado.COMPLETADA))
        .thenReturn(List.of(new Tarea()));

    assertEquals(1, servicio.listarVencidas().size());
  }

  @Test
  void listarProximasUsaElRangoDeDias() {
    when(repositorio.findByFechaLimiteBetweenAndEstadoNot(
            HOY, HOY.plusDays(7), Estado.COMPLETADA))
        .thenReturn(List.of(new Tarea()));

    assertEquals(1, servicio.listarProximas(7).size());
  }

  @Test
  void listarProximasRechazaDiasMenoresOIgualesACero() {
    assertThrows(ReglaNegocioException.class, () -> servicio.listarProximas(0));
  }

  @Test
  void listarPorFechaLimiteConsultaAlRepositorio() {
    when(repositorio.findByFechaLimiteIsNotNullOrderByFechaLimiteAsc())
        .thenReturn(List.of(new Tarea()));

    assertEquals(1, servicio.listarPorFechaLimite().size());
  }

  @Test
  void resumenPorEstadoCuentaCadaEstado() {
    when(repositorio.countByEstado(Estado.PENDIENTE)).thenReturn(2L);
    when(repositorio.countByEstado(Estado.EN_PROGRESO)).thenReturn(1L);
    when(repositorio.countByEstado(Estado.COMPLETADA)).thenReturn(3L);

    Map<Estado, Long> resumen = servicio.resumenPorEstado();

    assertEquals(2L, resumen.get(Estado.PENDIENTE));
    assertEquals(1L, resumen.get(Estado.EN_PROGRESO));
    assertEquals(3L, resumen.get(Estado.COMPLETADA));
  }

  @Test
  void contarDevuelveElTotalDeTareas() {
    when(repositorio.count()).thenReturn(5L);

    assertEquals(5L, servicio.contar());
  }
}

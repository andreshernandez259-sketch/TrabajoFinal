package com.asys.tareas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Estado;
import com.asys.tareas.model.Prioridad;
import com.asys.tareas.model.Tarea;
import com.asys.tareas.repository.TareaRepository;
import com.asys.tareas.service.regla.ReglaTarea;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TareaComandoServiceImplTest {

  @Mock private TareaRepository repositorio;
  @Mock private TareaConsultaService consulta;
  @Mock private ReglaTarea regla;

  private TareaComandoServiceImpl servicio;

  @BeforeEach
  void preparar() {
    servicio = new TareaComandoServiceImpl(repositorio, consulta, List.of(regla));
  }

  @Test
  void crearGuardaLaTareaComoPendiente() {
    when(repositorio.save(any(Tarea.class))).thenAnswer(inv -> inv.getArgument(0));

    Tarea creada = servicio.crear(nuevaTarea("Estudiar", Prioridad.ALTA));

    assertEquals(Estado.PENDIENTE, creada.getEstado());
    assertEquals(Prioridad.ALTA, creada.getPrioridad());
    verify(regla).validar(any(Tarea.class));
  }

  @Test
  void crearNoGuardaSiUnaReglaFalla() {
    doThrow(new ReglaNegocioException("inválido")).when(regla).validar(any(Tarea.class));
    Tarea datos = nuevaTarea("Estudiar", Prioridad.BAJA);

    assertThrows(ReglaNegocioException.class, () -> servicio.crear(datos));
    verify(repositorio, never()).save(any(Tarea.class));
  }

  @Test
  void crearUsaPrioridadMediaPorDefecto() {
    when(repositorio.save(any(Tarea.class))).thenAnswer(inv -> inv.getArgument(0));

    Tarea creada = servicio.crear(nuevaTarea("Estudiar", null));

    assertEquals(Prioridad.MEDIA, creada.getPrioridad());
  }

  @Test
  void crearQuitaEspaciosDelTitulo() {
    when(repositorio.save(any(Tarea.class))).thenAnswer(inv -> inv.getArgument(0));

    Tarea creada = servicio.crear(nuevaTarea("  Estudiar  ", Prioridad.ALTA));

    assertEquals("Estudiar", creada.getTitulo());
  }

  @Test
  void crearIgnoraElEstadoEnviadoPorElCliente() {
    when(repositorio.save(any(Tarea.class))).thenAnswer(inv -> inv.getArgument(0));
    Tarea datos = nuevaTarea("Estudiar", Prioridad.BAJA);
    datos.setEstado(Estado.COMPLETADA);

    Tarea creada = servicio.crear(datos);

    assertEquals(Estado.PENDIENTE, creada.getEstado());
  }

  @Test
  void actualizarCambiaLosDatos() {
    Tarea tarea = tareaConEstado(Estado.PENDIENTE);
    when(consulta.buscarPorId(1L)).thenReturn(tarea);
    when(repositorio.save(tarea)).thenReturn(tarea);

    Tarea resultado = servicio.actualizar(1L, nuevaTarea("Nuevo título", Prioridad.BAJA));

    assertEquals("Nuevo título", resultado.getTitulo());
    assertEquals(Prioridad.BAJA, resultado.getPrioridad());
  }

  @Test
  void cambiarEstadoActualizaLaTarea() {
    Tarea tarea = tareaConEstado(Estado.PENDIENTE);
    when(consulta.buscarPorId(1L)).thenReturn(tarea);
    when(repositorio.save(tarea)).thenReturn(tarea);

    Tarea resultado = servicio.cambiarEstado(1L, Estado.EN_PROGRESO);

    assertEquals(Estado.EN_PROGRESO, resultado.getEstado());
  }

  @Test
  void cambiarEstadoDeTareaCompletadaFalla() {
    Tarea tarea = tareaConEstado(Estado.COMPLETADA);
    when(consulta.buscarPorId(1L)).thenReturn(tarea);

    assertThrows(ReglaNegocioException.class, () -> servicio.cambiarEstado(1L, Estado.PENDIENTE));
    verify(repositorio, never()).save(any(Tarea.class));
  }

  @Test
  void eliminarBorraLaTarea() {
    Tarea tarea = tareaConEstado(Estado.PENDIENTE);
    when(consulta.buscarPorId(1L)).thenReturn(tarea);

    servicio.eliminar(1L);

    verify(repositorio).delete(tarea);
  }

  @Test
  void cambiarPrioridadActualizaLaTarea() {
    Tarea tarea = tareaConEstado(Estado.PENDIENTE);
    when(consulta.buscarPorId(1L)).thenReturn(tarea);
    when(repositorio.save(tarea)).thenReturn(tarea);

    Tarea resultado = servicio.cambiarPrioridad(1L, Prioridad.ALTA);

    assertEquals(Prioridad.ALTA, resultado.getPrioridad());
  }

  @Test
  void completarMarcaLaTareaComoCompletada() {
    Tarea tarea = tareaConEstado(Estado.EN_PROGRESO);
    when(consulta.buscarPorId(1L)).thenReturn(tarea);
    when(repositorio.save(tarea)).thenReturn(tarea);

    Tarea resultado = servicio.completar(1L);

    assertEquals(Estado.COMPLETADA, resultado.getEstado());
  }

  @Test
  void completarUnaTareaYaCompletadaFalla() {
    Tarea tarea = tareaConEstado(Estado.COMPLETADA);
    when(consulta.buscarPorId(1L)).thenReturn(tarea);

    assertThrows(ReglaNegocioException.class, () -> servicio.completar(1L));
  }

  @Test
  void eliminarCompletadasBorraTodasYDevuelveLaCantidad() {
    List<Tarea> completadas =
        List.of(tareaConEstado(Estado.COMPLETADA), tareaConEstado(Estado.COMPLETADA));
    when(consulta.listarPorEstado(Estado.COMPLETADA)).thenReturn(completadas);

    long borradas = servicio.eliminarCompletadas();

    assertEquals(2, borradas);
    verify(repositorio).deleteAll(completadas);
  }

  private Tarea nuevaTarea(String titulo, Prioridad prioridad) {
    Tarea tarea = new Tarea();
    tarea.setTitulo(titulo);
    tarea.setPrioridad(prioridad);
    return tarea;
  }

  private Tarea tareaConEstado(Estado estado) {
    Tarea tarea = nuevaTarea("Tarea", Prioridad.MEDIA);
    tarea.setEstado(estado);
    return tarea;
  }
}

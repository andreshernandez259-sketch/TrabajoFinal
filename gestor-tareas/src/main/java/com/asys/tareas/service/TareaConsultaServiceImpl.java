package com.asys.tareas.service;

import com.asys.tareas.exception.RecursoNoEncontradoException;
import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Estado;
import com.asys.tareas.model.Prioridad;
import com.asys.tareas.model.Tarea;
import com.asys.tareas.repository.TareaRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TareaConsultaServiceImpl implements TareaConsultaService {

  private final TareaRepository repositorio;
  private final Clock reloj;

  public TareaConsultaServiceImpl(TareaRepository repositorio, Clock reloj) {
    this.repositorio = repositorio;
    this.reloj = reloj;
  }

  @Override
  public List<Tarea> listar() {
    return repositorio.findAll();
  }

  @Override
  public List<Tarea> listarPorEstado(Estado estado) {
    return repositorio.findByEstado(estado);
  }

  @Override
  public List<Tarea> listarPorPrioridad(Prioridad prioridad) {
    return repositorio.findByPrioridad(prioridad);
  }

  @Override
  public List<Tarea> buscarPorTitulo(String titulo) {
    return repositorio.findByTituloContainingIgnoreCase(titulo);
  }

  @Override
  public List<Tarea> listarVencidas() {
    return repositorio.findByFechaLimiteBeforeAndEstadoNot(
        LocalDate.now(reloj), Estado.COMPLETADA);
  }

  @Override
  public List<Tarea> listarProximas(int dias) {
    if (dias <= 0) {
      throw new ReglaNegocioException("Los días deben ser mayores que cero");
    }
    LocalDate hoy = LocalDate.now(reloj);
    return repositorio.findByFechaLimiteBetweenAndEstadoNot(
        hoy, hoy.plusDays(dias), Estado.COMPLETADA);
  }

  @Override
  public List<Tarea> listarPorFechaLimite() {
    return repositorio.findByFechaLimiteIsNotNullOrderByFechaLimiteAsc();
  }

  @Override
  public Map<Estado, Long> resumenPorEstado() {
    Map<Estado, Long> resumen = new EnumMap<>(Estado.class);
    for (Estado estado : Estado.values()) {
      resumen.put(estado, repositorio.countByEstado(estado));
    }
    return resumen;
  }

  @Override
  public long contar() {
    return repositorio.count();
  }

  @Override
  public Tarea buscarPorId(Long id) {
    return repositorio
        .findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada: " + id));
  }
}

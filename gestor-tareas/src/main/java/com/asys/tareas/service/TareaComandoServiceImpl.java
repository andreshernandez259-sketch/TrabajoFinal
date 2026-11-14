package com.asys.tareas.service;

import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Estado;
import com.asys.tareas.model.Prioridad;
import com.asys.tareas.model.Tarea;
import com.asys.tareas.repository.TareaRepository;
import com.asys.tareas.service.regla.ReglaTarea;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TareaComandoServiceImpl implements TareaComandoService {

  private final TareaRepository repositorio;
  private final TareaConsultaService consulta;
  private final List<ReglaTarea> reglas;

  public TareaComandoServiceImpl(
      TareaRepository repositorio, TareaConsultaService consulta, List<ReglaTarea> reglas) {
    this.repositorio = repositorio;
    this.consulta = consulta;
    this.reglas = reglas;
  }

  @Override
  @Transactional
  public Tarea crear(Tarea datos) {
    validar(datos);
    Tarea tarea = new Tarea();
    copiarDatos(datos, tarea);
    tarea.setEstado(Estado.PENDIENTE);
    return repositorio.save(tarea);
  }

  @Override
  @Transactional
  public Tarea actualizar(Long id, Tarea datos) {
    Tarea tarea = consulta.buscarPorId(id);
    validar(datos);
    copiarDatos(datos, tarea);
    return repositorio.save(tarea);
  }

  @Override
  @Transactional
  public Tarea cambiarEstado(Long id, Estado estado) {
    Tarea tarea = consulta.buscarPorId(id);
    if (tarea.getEstado() == Estado.COMPLETADA) {
      throw new ReglaNegocioException("Una tarea completada no puede cambiar de estado");
    }
    tarea.setEstado(estado);
    return repositorio.save(tarea);
  }

  @Override
  @Transactional
  public Tarea cambiarPrioridad(Long id, Prioridad prioridad) {
    Tarea tarea = consulta.buscarPorId(id);
    tarea.setPrioridad(prioridad);
    return repositorio.save(tarea);
  }

  @Override
  @Transactional
  public Tarea completar(Long id) {
    return cambiarEstado(id, Estado.COMPLETADA);
  }

  @Override
  @Transactional
  public long eliminarCompletadas() {
    List<Tarea> completadas = consulta.listarPorEstado(Estado.COMPLETADA);
    repositorio.deleteAll(completadas);
    return completadas.size();
  }

  @Override
  @Transactional
  public void eliminar(Long id) {
    repositorio.delete(consulta.buscarPorId(id));
  }

  private void validar(Tarea tarea) {
    reglas.forEach(regla -> regla.validar(tarea));
  }

  private void copiarDatos(Tarea origen, Tarea destino) {
    destino.setTitulo(origen.getTitulo().trim());
    destino.setDescripcion(origen.getDescripcion());
    destino.setPrioridad(origen.getPrioridad() == null ? Prioridad.MEDIA : origen.getPrioridad());
    destino.setFechaLimite(origen.getFechaLimite());
  }
}

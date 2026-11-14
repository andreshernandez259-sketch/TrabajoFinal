package com.asys.tareas.service;

import com.asys.tareas.model.Estado;
import com.asys.tareas.model.Prioridad;
import com.asys.tareas.model.Tarea;

public interface TareaComandoService {

  Tarea crear(Tarea datos);

  Tarea actualizar(Long id, Tarea datos);

  Tarea cambiarEstado(Long id, Estado estado);

  Tarea cambiarPrioridad(Long id, Prioridad prioridad);

  Tarea completar(Long id);

  long eliminarCompletadas();

  void eliminar(Long id);
}

package com.asys.tareas.service;

import com.asys.tareas.model.Estado;
import com.asys.tareas.model.Prioridad;
import com.asys.tareas.model.Tarea;
import java.util.List;
import java.util.Map;

public interface TareaConsultaService {

  List<Tarea> listar();

  List<Tarea> listarPorEstado(Estado estado);

  List<Tarea> listarPorPrioridad(Prioridad prioridad);

  List<Tarea> buscarPorTitulo(String titulo);

  List<Tarea> listarVencidas();

  List<Tarea> listarProximas(int dias);

  List<Tarea> listarPorFechaLimite();

  Map<Estado, Long> resumenPorEstado();

  long contar();

  Tarea buscarPorId(Long id);
}

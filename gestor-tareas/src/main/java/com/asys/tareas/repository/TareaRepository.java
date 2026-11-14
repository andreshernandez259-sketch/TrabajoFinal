package com.asys.tareas.repository;

import com.asys.tareas.model.Estado;
import com.asys.tareas.model.Prioridad;
import com.asys.tareas.model.Tarea;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

  List<Tarea> findByEstado(Estado estado);

  List<Tarea> findByPrioridad(Prioridad prioridad);

  List<Tarea> findByTituloContainingIgnoreCase(String titulo);

  List<Tarea> findByFechaLimiteBeforeAndEstadoNot(LocalDate fecha, Estado estado);

  List<Tarea> findByFechaLimiteBetweenAndEstadoNot(
      LocalDate desde, LocalDate hasta, Estado estado);

  List<Tarea> findByFechaLimiteIsNotNullOrderByFechaLimiteAsc();

  long countByEstado(Estado estado);
}

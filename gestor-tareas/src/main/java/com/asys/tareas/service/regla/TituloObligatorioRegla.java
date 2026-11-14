package com.asys.tareas.service.regla;

import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Tarea;
import org.springframework.stereotype.Component;

@Component
public class TituloObligatorioRegla implements ReglaTarea {

  @Override
  public void validar(Tarea tarea) {
    if (tarea.getTitulo() == null || tarea.getTitulo().isBlank()) {
      throw new ReglaNegocioException("El título es obligatorio");
    }
  }
}

package com.asys.tareas.service.regla;

import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Tarea;
import org.springframework.stereotype.Component;

@Component
public class TituloLongitudRegla implements ReglaTarea {

  static final int MAXIMO = 100;

  @Override
  public void validar(Tarea tarea) {
    if (tarea.getTitulo() != null && tarea.getTitulo().trim().length() > MAXIMO) {
      throw new ReglaNegocioException("El título no puede superar " + MAXIMO + " caracteres");
    }
  }
}

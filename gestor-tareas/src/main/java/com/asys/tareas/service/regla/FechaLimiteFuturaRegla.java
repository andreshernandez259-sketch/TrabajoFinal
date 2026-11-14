package com.asys.tareas.service.regla;

import com.asys.tareas.exception.ReglaNegocioException;
import com.asys.tareas.model.Tarea;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class FechaLimiteFuturaRegla implements ReglaTarea {

  private final Clock reloj;

  public FechaLimiteFuturaRegla(Clock reloj) {
    this.reloj = reloj;
  }

  @Override
  public void validar(Tarea tarea) {
    if (tarea.getFechaLimite() != null && tarea.getFechaLimite().isBefore(LocalDate.now(reloj))) {
      throw new ReglaNegocioException("La fecha límite no puede estar en el pasado");
    }
  }
}

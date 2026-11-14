package com.asys.tareas.controller;

import com.asys.tareas.model.Estado;
import com.asys.tareas.model.Prioridad;
import com.asys.tareas.model.Tarea;
import com.asys.tareas.service.TareaComandoService;
import com.asys.tareas.service.TareaConsultaService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

  private final TareaConsultaService consulta;
  private final TareaComandoService comando;

  public TareaController(TareaConsultaService consulta, TareaComandoService comando) {
    this.consulta = consulta;
    this.comando = comando;
  }

  @GetMapping
  public List<Tarea> listar(@RequestParam(required = false) Estado estado) {
    return (estado == null) ? consulta.listar() : consulta.listarPorEstado(estado);
  }

  @GetMapping("/{id}")
  public Tarea buscar(@PathVariable Long id) {
    return consulta.buscarPorId(id);
  }

  @GetMapping("/prioridad/{prioridad}")
  public List<Tarea> listarPorPrioridad(@PathVariable Prioridad prioridad) {
    return consulta.listarPorPrioridad(prioridad);
  }

  @GetMapping("/buscar")
  public List<Tarea> buscarPorTitulo(@RequestParam String titulo) {
    return consulta.buscarPorTitulo(titulo);
  }

  @GetMapping("/vencidas")
  public List<Tarea> listarVencidas() {
    return consulta.listarVencidas();
  }

  @GetMapping("/proximas")
  public List<Tarea> listarProximas(@RequestParam(defaultValue = "7") int dias) {
    return consulta.listarProximas(dias);
  }

  @GetMapping("/por-fecha")
  public List<Tarea> listarPorFechaLimite() {
    return consulta.listarPorFechaLimite();
  }

  @GetMapping("/resumen")
  public Map<Estado, Long> resumenPorEstado() {
    return consulta.resumenPorEstado();
  }

  @GetMapping("/total")
  public long total() {
    return consulta.contar();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Tarea crear(@RequestBody Tarea tarea) {
    return comando.crear(tarea);
  }

  @PutMapping("/{id}")
  public Tarea actualizar(@PathVariable Long id, @RequestBody Tarea tarea) {
    return comando.actualizar(id, tarea);
  }

  @PatchMapping("/{id}/estado")
  public Tarea cambiarEstado(@PathVariable Long id, @RequestParam Estado estado) {
    return comando.cambiarEstado(id, estado);
  }

  @PatchMapping("/{id}/prioridad")
  public Tarea cambiarPrioridad(@PathVariable Long id, @RequestParam Prioridad prioridad) {
    return comando.cambiarPrioridad(id, prioridad);
  }

  @PatchMapping("/{id}/completar")
  public Tarea completar(@PathVariable Long id) {
    return comando.completar(id);
  }

  @DeleteMapping("/completadas")
  public long eliminarCompletadas() {
    return comando.eliminarCompletadas();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable Long id) {
    comando.eliminar(id);
  }
}

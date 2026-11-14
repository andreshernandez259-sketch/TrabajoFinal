package com.asys.tareas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "tareas")
public class Tarea {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String titulo;

  @Column(length = 500)
  private String descripcion;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Prioridad prioridad;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Estado estado;

  private LocalDate fechaLimite;

  public Long getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Prioridad getPrioridad() {
    return prioridad;
  }

  public void setPrioridad(Prioridad prioridad) {
    this.prioridad = prioridad;
  }

  public Estado getEstado() {
    return estado;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }

  public LocalDate getFechaLimite() {
    return fechaLimite;
  }

  public void setFechaLimite(LocalDate fechaLimite) {
    this.fechaLimite = fechaLimite;
  }
}

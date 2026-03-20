package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viaje_id")
    private Long viajeId;

    @Column(name = "evaluador_id")
    private Long evaluadorId; // El que escribe la reseña (ej. el pasajero)

    @Column(name = "evaluado_id")
    private Long evaluadoId; // Al que están calificando (ej. el conductor)

    private Integer calificacion; // Estrellas del 1 al 5
    private String comentario;

    @Column(name = "fecha_resena", insertable = false, updatable = false)
    private LocalDateTime fechaResena;

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getViajeId() { return viajeId; }
    public void setViajeId(Long viajeId) { this.viajeId = viajeId; }

    public Long getEvaluadorId() { return evaluadorId; }
    public void setEvaluadorId(Long evaluadorId) { this.evaluadorId = evaluadorId; }

    public Long getEvaluadoId() { return evaluadoId; }
    public void setEvaluadoId(Long evaluadoId) { this.evaluadoId = evaluadoId; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFechaResena() { return fechaResena; }
    public void setFechaResena(LocalDateTime fechaResena) { this.fechaResena = fechaResena; }
}
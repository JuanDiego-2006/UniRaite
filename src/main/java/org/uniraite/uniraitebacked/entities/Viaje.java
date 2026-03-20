package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "viajes")
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conductor_id")
    private Long conductorId;

    @Transient
    private String conductorNombre;

    @Column(name = "punto_salida")
    private String puntoSalida;

    @Column(length = 500)
    private String destino;

    @Column(name = "hora_salida")
    private String horaSalida;

    @Column(name = "cupos_disponibles")
    private Integer asientosDisponibles;

    private Double costo;
    private String estado;

    // Constructor vacío necesario para Spring Boot
    public Viaje() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConductorId() { return conductorId; }
    public void setConductorId(Long conductorId) { this.conductorId = conductorId; }

    public String getConductorNombre() { return conductorNombre; }
    public void setConductorNombre(String conductorNombre) { this.conductorNombre = conductorNombre; }

    public String getPuntoSalida() { return puntoSalida; }
    public void setPuntoSalida(String puntoSalida) { this.puntoSalida = puntoSalida; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getHoraSalida() { return horaSalida; }
    public void setHoraSalida(String horaSalida) { this.horaSalida = horaSalida; }

    public Integer getAsientosDisponibles() { return asientosDisponibles; }
    public void setAsientosDisponibles(Integer asientosDisponibles) { this.asientosDisponibles = asientosDisponibles; }

    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
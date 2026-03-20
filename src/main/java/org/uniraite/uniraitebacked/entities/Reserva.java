package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viaje_id")
    private Long viajeId;

    @Column(name = "pasajero_id")
    private Long pasajeroId;

    private String estado; // PENDIENTE, CONFIRMADA, CANCELADA

    @Column(name = "fecha_reserva", insertable = false, updatable = false)
    private LocalDateTime fechaReserva;

    // ✨ NUEVO: Variable para guardar y enviar los detalles completos del viaje al celular
    @Transient
    private Viaje viaje;

    // --- GETTERS Y SETTERS MANUALES ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getViajeId() { return viajeId; }
    public void setViajeId(Long viajeId) { this.viajeId = viajeId; }

    public Long getPasajeroId() { return pasajeroId; }
    public void setPasajeroId(Long pasajeroId) { this.pasajeroId = pasajeroId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }

    // Getter y Setter para la nueva variable
    public Viaje getViaje() { return viaje; }
    public void setViaje(Viaje viaje) { this.viaje = viaje; }
}
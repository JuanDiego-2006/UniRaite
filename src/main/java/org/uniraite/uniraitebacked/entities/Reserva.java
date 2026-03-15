package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
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
}
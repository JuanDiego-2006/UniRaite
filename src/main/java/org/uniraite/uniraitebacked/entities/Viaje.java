package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "viajes")
@Data
@NoArgsConstructor // Necesario para recibir JSON de Android
@AllArgsConstructor
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conductor_id")
    private Long conductorId;

    // SOLUCIÓN AL CRASH: Agregamos este campo como Transient para que Java
    // lo reciba sin intentar guardarlo en una columna de MySQL
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
}
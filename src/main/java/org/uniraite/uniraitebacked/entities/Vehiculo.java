package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private String anio;
    private String color;
    private String placas;

    @Column(name = "numero_asientos")
    private Integer numeroAsientos;

    @Column(name = "conductor_id")
    private Long usuarioId;
}
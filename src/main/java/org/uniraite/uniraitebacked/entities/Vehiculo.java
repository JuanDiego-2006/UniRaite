package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "vehiculos")
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

    /** Foto del vehículo (placas/modelo) en JPEG codificado Base64 para identificación del pasajero */
    @Lob
    @Column(name = "foto_base64", columnDefinition = "LONGTEXT")
    private String fotoBase64;

    @Lob
    @Column(name = "foto_placas_base64", columnDefinition = "LONGTEXT")
    private String fotoPlacasBase64;

    public Vehiculo() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getAnio() { return anio; }
    public void setAnio(String anio) { this.anio = anio; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getPlacas() { return placas; }
    public void setPlacas(String placas) { this.placas = placas; }

    public Integer getNumeroAsientos() { return numeroAsientos; }
    public void setNumeroAsientos(Integer numeroAsientos) { this.numeroAsientos = numeroAsientos; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getFotoBase64() { return fotoBase64; }
    public void setFotoBase64(String fotoBase64) { this.fotoBase64 = fotoBase64; }

    public String getFotoPlacasBase64() { return fotoPlacasBase64; }
    public void setFotoPlacasBase64(String fotoPlacasBase64) { this.fotoPlacasBase64 = fotoPlacasBase64; }
}
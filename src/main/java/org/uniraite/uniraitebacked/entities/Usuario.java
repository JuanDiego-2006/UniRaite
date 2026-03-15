package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    private String matricula;

    @Column(name = "correo_institucional")
    private String correoInstitucional;

    private String carrera;

    @Column(length = 1000) // Le damos más espacio por si guardas una URL larga de la foto
    private String foto;

    private String telefono;
    private String contrasena;
    private String rol; // ESTUDIANTE o CONDUCTOR

    @Column(name = "nombre_emergencia")
    private String nombreEmergencia;

    @Column(name = "telefono_emergencia")
    private String telefonoEmergencia;
}
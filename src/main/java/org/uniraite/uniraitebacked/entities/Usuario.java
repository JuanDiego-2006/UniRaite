package org.uniraite.uniraitebacked.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
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

    @Column(length = 1000)
    private String foto;

    private String telefono;
    private String contrasena;
    private String rol;

    @Column(name = "nombre_emergencia")
    private String nombreEmergencia;

    @Column(name = "telefono_emergencia")
    private String telefonoEmergencia;

    private String fcmToken; // Esto crea la columna en MySQL automáticamente

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNombreEmergencia() { return nombreEmergencia; }
    public void setNombreEmergencia(String nombreEmergencia) { this.nombreEmergencia = nombreEmergencia; }

    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    public String getFcmToken() {
        return fcmToken;
    }
}
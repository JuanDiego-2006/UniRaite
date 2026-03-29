package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Usuario;
import org.uniraite.uniraitebacked.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. REGISTRO
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByCorreoInstitucional(usuario.getCorreoInstitucional()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }
        if (usuario.getRol() == null) {
            usuario.setRol("ESTUDIANTE"); // Rol por defecto
        }
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    // 2. LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginData) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreoInstitucionalAndContrasena(
                loginData.getCorreoInstitucional(), loginData.getContrasena());

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

    // 3. OBTENER PERFIL
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. ACTUALIZAR PERFIL
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarPerfil(@PathVariable Long id, @RequestBody Usuario detallesUsuario) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombreCompleto(detallesUsuario.getNombreCompleto());
            usuario.setCarrera(detallesUsuario.getCarrera());
            usuario.setFoto(detallesUsuario.getFoto());
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. ACTUALIZAR CONTACTO DE EMERGENCIA
    @PutMapping("/{id}/contacto")
    public ResponseEntity<Usuario> actualizarContactoEmergencia(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono) {

        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombreEmergencia(nombre);
            usuario.setTelefonoEmergencia(telefono);
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 6. RECUPERACIÓN DE CONTRASEÑA
    @PostMapping("/recuperar")
    public ResponseEntity<?> recuperarContrasena(
            @RequestParam("correo") String correo,
            @RequestParam("nuevaContrasena") String nuevaContrasena) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoInstitucional(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setContrasena(nuevaContrasena);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    // GUARDAR TOKEN FCM (CORREGIDO)
    @PostMapping("/actualizar-token")
    public void guardarTokenFCM(@RequestParam Long usuarioId, @RequestParam String fcmToken) {
        usuarioRepository.findById(usuarioId).ifPresent(usuario -> {
            usuario.setFcmToken(fcmToken);
            usuarioRepository.save(usuario);
            System.out.println("✅ Token FCM actualizado para el usuario: " + usuario.getCorreoInstitucional());
        });
    }
}
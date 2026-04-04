package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Reserva;
import org.uniraite.uniraitebacked.entities.Viaje;
import org.uniraite.uniraitebacked.repositories.ReservaRepository;
import org.uniraite.uniraitebacked.repositories.UsuarioRepository;
import org.uniraite.uniraitebacked.repositories.ViajeRepository;
import org.uniraite.uniraitebacked.services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private ReservaRepository reservaRepository; // Para buscar a los pasajeros

    @Autowired
    private UsuarioRepository usuarioRepository; // Para buscar los Tokens

    @Autowired
    private FcmService fcmService; // Tu motor de Firebase

    @GetMapping
    public List<Viaje> obtenerTodos() {
        return viajeRepository.findByEstadoOrderByHoraSalidaAsc("ACTIVO");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        return viajeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/conductor/{id}")
    public List<Viaje> obtenerViajesPorConductor(@PathVariable Long id) {
        return viajeRepository.findByConductorId(id);
    }

    // 1. CREAR VIAJE Y MANDAR NOTIFICACIÓN REMOTA AL CONDUCTOR
    @PostMapping
    public Viaje crear(@RequestBody Viaje viaje) {
        if (viaje.getEstado() == null) {
            viaje.setEstado("ACTIVO");
        }
        Viaje viajeGuardado = viajeRepository.save(viaje);

        // 🔥 SPRING BOOT LE AVISA A FIREBASE QUE Mande LA NOTIFICACIÓN 🔥
        usuarioRepository.findById(viajeGuardado.getConductorId()).ifPresent(conductor -> {
            fcmService.enviarNotificacionPush(
                    conductor.getFcmToken(),
                    "Ruta Creada 📍",
                    "Tu viaje hacia " + viajeGuardado.getDestino() + " está visible para todos desde el servidor."
            );
        });

        return viajeGuardado;
    }

    @PutMapping("/{id}")
    public Viaje actualizar(@PathVariable Long id, @RequestBody Viaje detalles) {
        return viajeRepository.findById(id).map(viaje -> {
            if (detalles.getPuntoSalida() != null) {
                viaje.setPuntoSalida(detalles.getPuntoSalida());
            }
            if (detalles.getDestino() != null) {
                viaje.setDestino(detalles.getDestino());
            }
            if (detalles.getHoraSalida() != null) {
                viaje.setHoraSalida(detalles.getHoraSalida());
            }
            if (detalles.getAsientosDisponibles() != null) {
                viaje.setAsientosDisponibles(detalles.getAsientosDisponibles());
            }
            if (detalles.getCosto() != null) {
                viaje.setCosto(detalles.getCosto());
            }
            viaje.setLatitudSalida(detalles.getLatitudSalida());
            viaje.setLongitudSalida(detalles.getLongitudSalida());
            viaje.setLatitudDestino(detalles.getLatitudDestino());
            viaje.setLongitudDestino(detalles.getLongitudDestino());

            if (detalles.getEstado() != null) {
                viaje.setEstado(detalles.getEstado());
            } else {
                viaje.setEstado("ACTIVO");
            }

            return viajeRepository.save(viaje);
        }).orElseThrow(() -> new RuntimeException("Viaje no encontrado con id " + id));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        viajeRepository.deleteById(id);
    }

    // 2. NUEVO ENDPOINT: INICIAR VIAJE Y AVISAR A LOS PASAJEROS
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<?> iniciarViaje(@PathVariable Long id) {
        return viajeRepository.findById(id).map(viaje -> {
            viaje.setEstado("EN_PROGRESO");
            viajeRepository.save(viaje);

            // Buscar a todos los alumnos que reservaron este viaje
            List<Reserva> reservas = reservaRepository.findByViajeId(id);
            for (Reserva r : reservas) {
                usuarioRepository.findById(r.getPasajeroId()).ifPresent(pasajero -> {
                    // Manda notificación push a cada pasajero
                    fcmService.enviarNotificacionPush(
                            pasajero.getFcmToken(),
                            "¡Tu viaje ha iniciado! 🚗",
                            "El conductor va en camino al punto de salida (" + viaje.getPuntoSalida() + ")."
                    );
                });
            }
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Reserva;
import org.uniraite.uniraitebacked.repositories.ReservaRepository;
import org.uniraite.uniraitebacked.repositories.UsuarioRepository;
import org.uniraite.uniraitebacked.repositories.ViajeRepository;
import org.uniraite.uniraitebacked.services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Añadimos repositorio de usuarios

    @Autowired
    private FcmService fcmService; // Añadimos nuestro nuevo motor de notificaciones

    @GetMapping("/historial/{pasajeroId}")
    public List<Reserva> obtenerHistorial(@PathVariable Long pasajeroId) {
        List<Reserva> reservas = reservaRepository.findByPasajeroId(pasajeroId);
        List<Reserva> reservasCompletas = new ArrayList<>();

        for (Reserva reserva : reservas) {
            viajeRepository.findById(reserva.getViajeId()).ifPresent(viaje -> {
                reserva.setViaje(viaje);
                reservasCompletas.add(reserva);
            });
        }
        return reservasCompletas;
    }

    @PostMapping
    public Reserva crearReserva(@RequestBody Reserva reserva) {
        if (reserva.getEstado() == null) {
            reserva.setEstado("CONFIRMADA");
        }

        // 1. Guardar y descontar asiento
        viajeRepository.findById(reserva.getViajeId()).ifPresent(viaje -> {
            if (viaje.getAsientosDisponibles() > 0) {
                viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - 1);
                viajeRepository.save(viaje);

                // 🔥 2. MAGIA DE NOTIFICACIONES PUSH REMOTAS 🔥

                // A) Buscar el token del Pasajero y avisarle que fue aceptado
                usuarioRepository.findById(reserva.getPasajeroId()).ifPresent(pasajero -> {
                    fcmService.enviarNotificacionPush(
                            pasajero.getFcmToken(),
                            "¡Solicitud Aceptada! ✅",
                            "Tu lugar hacia " + viaje.getDestino() + " ha sido confirmado en el servidor."
                    );
                });

                // B) Buscar el token del Conductor y avisarle que tiene un nuevo pasajero
                usuarioRepository.findById(viaje.getConductorId()).ifPresent(conductor -> {
                    fcmService.enviarNotificacionPush(
                            conductor.getFcmToken(),
                            "¡Nuevo pasajero! 🚗",
                            "Alguien acaba de reservar un lugar en tu viaje hacia " + viaje.getDestino() + "."
                    );
                });
            }
        });

        return reservaRepository.save(reserva);
    }
}
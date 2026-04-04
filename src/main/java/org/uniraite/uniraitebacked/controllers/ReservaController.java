package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Reserva;
import org.uniraite.uniraitebacked.repositories.ReservaRepository;
import org.uniraite.uniraitebacked.repositories.UsuarioRepository;
import org.uniraite.uniraitebacked.repositories.ViajeRepository;
import org.uniraite.uniraitebacked.services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        List<Reserva> reservas = reservaRepository.findByPasajeroIdOrderByIdDesc(pasajeroId);
        // Una fila por (pasajero + viaje): quedarse con la reserva más reciente (id mayor).
        Map<Long, Reserva> unaPorViaje = new LinkedHashMap<>();
        for (Reserva r : reservas) {
            unaPorViaje.putIfAbsent(r.getViajeId(), r);
        }
        List<Reserva> sinDuplicar = new ArrayList<>(unaPorViaje.values());
        sinDuplicar.sort((a, b) -> Long.compare(
                b.getId() != null ? b.getId() : 0L,
                a.getId() != null ? a.getId() : 0L));

        List<Reserva> reservasCompletas = new ArrayList<>();
        for (Reserva reserva : sinDuplicar) {
            viajeRepository.findById(reserva.getViajeId()).ifPresent(reserva::setViaje);
            reservasCompletas.add(reserva);
        }
        return reservasCompletas;
    }

    @PostMapping
    public Reserva crearReserva(@RequestBody Reserva reserva) {
        if (reserva.getEstado() == null) {
            reserva.setEstado("CONFIRMADA");
        }

        // Idempotente: misma reserva (pasajero + viaje) no crea otra fila ni vuelve a descontar cupos
        if (reserva.getViajeId() != null && reserva.getPasajeroId() != null) {
            Optional<Reserva> ya = reservaRepository.findFirstByViajeIdAndPasajeroIdOrderByIdDesc(
                    reserva.getViajeId(), reserva.getPasajeroId());
            if (ya.isPresent()) {
                Reserva e = ya.get();
                viajeRepository.findById(e.getViajeId()).ifPresent(e::setViaje);
                return e;
            }
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
package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Reserva;
import org.uniraite.uniraitebacked.repositories.ReservaRepository;
import org.uniraite.uniraitebacked.repositories.ViajeRepository;
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

    // Obtener el historial de reservas CON los detalles del viaje
    @GetMapping("/historial/{pasajeroId}")
    public List<Reserva> obtenerHistorial(@PathVariable Long pasajeroId) {
        List<Reserva> reservas = reservaRepository.findByPasajeroId(pasajeroId);

        // ✨ NUEVO: Creamos una nueva lista filtrada
        List<Reserva> reservasCompletas = new ArrayList<>();

        for (Reserva reserva : reservas) {
            viajeRepository.findById(reserva.getViajeId()).ifPresent(viaje -> {
                reserva.setViaje(viaje);
                // Solo enviamos la reserva al celular si el viaje NO ha sido eliminado
                reservasCompletas.add(reserva);
            });
        }
        return reservasCompletas;
    }

    // Crear una nueva reserva y descontar asiento
    @PostMapping
    public Reserva crearReserva(@RequestBody Reserva reserva) {
        if (reserva.getEstado() == null) {
            reserva.setEstado("CONFIRMADA");
        }

        // Descontamos 1 cupo del viaje automáticamente
        viajeRepository.findById(reserva.getViajeId()).ifPresent(viaje -> {
            if (viaje.getAsientosDisponibles() > 0) {
                viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - 1);
                viajeRepository.save(viaje);
            }
        });

        return reservaRepository.save(reserva);
    }
}
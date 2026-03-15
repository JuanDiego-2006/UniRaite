package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Reserva;
import org.uniraite.uniraitebacked.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    // Obtener el historial de reservas de un pasajero específico
    @GetMapping("/historial/{pasajeroId}")
    public List<Reserva> obtenerHistorial(@PathVariable Long pasajeroId) {
        return reservaRepository.findByPasajeroId(pasajeroId);
    }

    // Crear una nueva reserva (cuando el estudiante le da "Unirse al viaje")
    @PostMapping
    public Reserva crearReserva(@RequestBody Reserva reserva) {
        if (reserva.getEstado() == null) {
            reserva.setEstado("PENDIENTE");
        }
        return reservaRepository.save(reserva);
    }
}
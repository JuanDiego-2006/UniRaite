package org.uniraite.uniraitebacked.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.uniraite.uniraitebacked.entities.Reserva;
import org.uniraite.uniraitebacked.entities.Viaje;
import org.uniraite.uniraitebacked.repositories.ReservaRepository;
import org.uniraite.uniraitebacked.repositories.UsuarioRepository;
import org.uniraite.uniraitebacked.repositories.ViajeRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class NotificacionProgramadaService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FcmService fcmService;

    // 🔥 Este método se ejecutará automáticamente cada 60 segundos (60000 milisegundos) 🔥
    @Scheduled(fixedRate = 60000)
    public void verificarViajesProximos() {
        try {
            // 1. Calculamos la hora de "ahora + 15 minutos"
            LocalDateTime tiempoAlerta = LocalDateTime.now().plusMinutes(15);

            // 2. Le damos el MISMO formato que usa tu app de Android
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.US);
            String horaABuscar = tiempoAlerta.format(formatter);

            // 3. Buscamos viajes activos que estén programados EXACTAMENTE para esa hora
            List<Viaje> viajes = viajeRepository.findByHoraSalidaAndEstado(horaABuscar, "ACTIVO");

            for (Viaje v : viajes) {
                // Buscamos a los pasajeros que reservaron este viaje
                List<Reserva> pasajeros = reservaRepository.findByViajeId(v.getId());

                for (Reserva r : pasajeros) {
                    usuarioRepository.findById(r.getPasajeroId()).ifPresent(p -> {
                        // 4. Mandamos la notificación Push Remota
                        fcmService.enviarNotificacionPush(
                                p.getFcmToken(),
                                "¡Prepárate! 🚗",
                                "Tu raite hacia " + v.getDestino() + " sale en 15 minutos."
                        );
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Error en tarea programada: " + e.getMessage());
        }
    }
}
package org.uniraite.uniraitebacked.repositories;

import org.uniraite.uniraitebacked.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Esta línea es la clave para el Historial: busca todas las reservas de un pasajero
    List<Reserva> findByPasajeroId(Long pasajeroId);

    List<Reserva> findByPasajeroIdOrderByIdDesc(Long pasajeroId);

    java.util.Optional<Reserva> findFirstByViajeIdAndPasajeroIdOrderByIdDesc(Long viajeId, Long pasajeroId);

    // 🔥 Agregamos esta línea para que ViajeController pueda buscar pasajeros por viaje
    List<Reserva> findByViajeId(Long viajeId);
}
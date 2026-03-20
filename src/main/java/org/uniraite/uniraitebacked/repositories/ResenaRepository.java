package org.uniraite.uniraitebacked.repositories;

import org.uniraite.uniraitebacked.entities.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Buscar todas las reseñas que le han dejado a un usuario (conductor o pasajero)
    List<Resena> findByEvaluadoId(Long evaluadoId);

    // Sirve para saber si un usuario ya calificó un viaje y no deje doble reseña
    boolean existsByViajeIdAndEvaluadorId(Long viajeId, Long evaluadorId);
}
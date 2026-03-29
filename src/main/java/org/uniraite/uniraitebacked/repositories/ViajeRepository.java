package org.uniraite.uniraitebacked.repositories;

import org.uniraite.uniraitebacked.entities.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    // Busca los viajes filtrando por estado y ordenándolos por hora
    List<Viaje> findByEstadoOrderByHoraSalidaAsc(String estado);

    // Busca los viajes de un conductor específico
    List<Viaje> findByConductorId(Long conductorId);

    // En ViajeRepository.java
    List<Viaje> findByHoraSalidaAndEstado(String horaSalida, String estado);
}
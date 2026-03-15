package org.uniraite.uniraitebacked.repositories;

import org.uniraite.uniraitebacked.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    // Buscar si el usuario ya tiene un carro registrado
    Optional<Vehiculo> findByUsuarioId(Long usuarioId);
}
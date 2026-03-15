package org.uniraite.uniraitebacked.repositories;

import org.uniraite.uniraitebacked.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);
    Optional<Usuario> findByCorreoInstitucionalAndContrasena(String correoInstitucional, String contrasena);
}
package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Vehiculo;
import org.uniraite.uniraitebacked.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    // Ruta para registrar un auto nuevo
    @PostMapping
    public ResponseEntity<Vehiculo> registrarVehiculo(@RequestBody Vehiculo vehiculo) {
        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        return ResponseEntity.ok(guardado);
    }

    // Ruta para verificar si el usuario ya tiene un auto al intentar entrar a "Modo Conductor"
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Vehiculo> obtenerPorUsuario(@PathVariable Long idUsuario) {
        Optional<Vehiculo> vehiculo = vehiculoRepository.findByUsuarioId(idUsuario);
        return vehiculo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
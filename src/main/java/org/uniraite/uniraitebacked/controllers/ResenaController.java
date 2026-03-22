package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Resena;
import org.uniraite.uniraitebacked.repositories.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaRepository resenaRepository;

    @PostMapping
    public ResponseEntity<?> crearResena(@RequestBody Resena resena) {
        // Validar que no haya calificado ya este viaje
        if (resenaRepository.existsByViajeIdAndEvaluadorId(resena.getViajeId(), resena.getEvaluadorId())) {
            return ResponseEntity.badRequest().body("Ya has calificado este viaje.");
        }

        Resena nuevaResena = resenaRepository.save(resena);
        return ResponseEntity.ok(nuevaResena);
    }

    @GetMapping("/evaluado/{id}")
    public List<Resena> obtenerResenasDeUsuario(@PathVariable Long id) {
        return resenaRepository.findByEvaluadoId(id);
    }

    // 🔥 LA MAGIA: Calculamos el promedio de estrellas del conductor
    @GetMapping("/promedio/{usuarioId}")
    public ResponseEntity<Double> obtenerPromedio(@PathVariable Long usuarioId) {
        List<Resena> resenas = resenaRepository.findByEvaluadoId(usuarioId);
        if (resenas.isEmpty()) {
            return ResponseEntity.ok(0.0); // Si no tiene reseñas, tiene 0.0 estrellas (Nuevo)
        }
        double suma = resenas.stream().mapToDouble(Resena::getCalificacion).sum();
        double promedio = suma / resenas.size();
        return ResponseEntity.ok(promedio);
    }
}
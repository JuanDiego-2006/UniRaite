package org.uniraite.uniraitebacked.controllers;

import org.uniraite.uniraitebacked.entities.Viaje;
import org.uniraite.uniraitebacked.repositories.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    @Autowired
    private ViajeRepository viajeRepository;

    @GetMapping
    public List<Viaje> obtenerTodos() {
        // AQUÍ ESTÁ LA CORRECCIÓN: Ahora usa el método nuevo buscando solo los activos
        return viajeRepository.findByEstadoOrderByHoraSalidaAsc("ACTIVO");
    }

    @GetMapping("/conductor/{id}")
    public List<Viaje> obtenerViajesPorConductor(@PathVariable Long id) {
        return viajeRepository.findByConductorId(id);
    }

    @PostMapping
    public Viaje crear(@RequestBody Viaje viaje) {
        if (viaje.getEstado() == null) {
            viaje.setEstado("ACTIVO");
        }
        return viajeRepository.save(viaje);
    }

    @PutMapping("/{id}")
    public Viaje actualizar(@PathVariable Long id, @RequestBody Viaje detalles) {
        return viajeRepository.findById(id).map(viaje -> {
            viaje.setHoraSalida(detalles.getHoraSalida());
            viaje.setAsientosDisponibles(detalles.getAsientosDisponibles());
            viaje.setEstado(detalles.getEstado());
            return viajeRepository.save(viaje);
        }).orElseThrow(() -> new RuntimeException("Viaje no encontrado con id " + id));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        viajeRepository.deleteById(id);
    }
}
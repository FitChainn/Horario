package Horario.Horario.Controller;

import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Service.HorarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping
    public ResponseEntity<List<HorarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(horarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return horarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entrenador/{entrenadorId}")
    public ResponseEntity<List<HorarioResponseDTO>> obtenerPorEntrenador(@PathVariable Long entrenadorId) {
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorEntrenador(entrenadorId);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/establecimiento/{establecimientoId}")
    public ResponseEntity<List<HorarioResponseDTO>> obtenerPorEstablecimiento(@PathVariable Long establecimientoId) {
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorEstablecimiento(establecimientoId);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/dia/{diaSemana}")
    public ResponseEntity<List<HorarioResponseDTO>> obtenerPorDia(@PathVariable String diaSemana) {
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorDia(diaSemana);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(horarios);
    }

    @PostMapping
    public ResponseEntity<HorarioResponseDTO> guardar(@Valid @RequestBody HorarioRequestDTO dto) {
        return ResponseEntity.status(201).body(horarioService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioRequestDTO dto) {
        return ResponseEntity.ok(horarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (horarioService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        horarioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
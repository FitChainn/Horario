package Horario.Horario.Controller;

import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Service.HorarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "HORARIOS", description = "GESTIÓN DE HORARIOS")
@RestController
@RequestMapping("/v1/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @Operation(summary = "OBTENER TODOS LOS HORARIOS", description = "Retorna la lista de todos los horarios. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping
    public ResponseEntity<List<HorarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(horarioService.obtenerTodos());
    }

    @Operation(summary = "OBTENER HORARIO POR ID", description = "Retorna un horario específico por su ID. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario encontrado"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<HorarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return horarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "OBTENER HORARIOS POR ESTABLECIMIENTO", description = "Retorna todos los horarios de un establecimiento. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "Sin horarios para ese establecimiento")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/establecimiento/{establecimientoId}")
    public ResponseEntity<List<HorarioResponseDTO>> obtenerPorEstablecimiento(@PathVariable Long establecimientoId) {
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorEstablecimiento(establecimientoId);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(horarios);
    }

    @Operation(summary = "OBTENER HORARIOS POR ESTABLECIMIENTO Y DÍA", description = "Retorna horarios de un establecimiento filtrados por día de la semana. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "Sin horarios para ese establecimiento y día")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/establecimiento/{establecimientoId}/dia/{diaSemana}")
    public ResponseEntity<List<HorarioResponseDTO>> obtenerPorEstablecimientoYDia(
            @PathVariable Long establecimientoId,
            @PathVariable String diaSemana) {
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorEstablecimientoYDia(establecimientoId, diaSemana);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(horarios);
    }

    @Operation(summary = "OBTENER HORARIOS POR DÍA", description = "Retorna todos los horarios de un día de la semana. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "Sin horarios para ese día")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/dia/{diaSemana}")
    public ResponseEntity<List<HorarioResponseDTO>> obtenerPorDia(@PathVariable String diaSemana) {
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorDia(diaSemana);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(horarios);
    }

    @Operation(summary = "CREAR HORARIO", description = "Crea un nuevo horario. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Horario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Establecimiento no encontrado"),
            @ApiResponse(responseCode = "503", description = "Microservicio no disponible")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HorarioResponseDTO> guardar(@Valid @RequestBody HorarioRequestDTO dto) {
        return ResponseEntity.status(201).body(horarioService.guardar(dto));
    }

    @Operation(summary = "ACTUALIZAR HORARIO", description = "Actualiza un horario existente. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HorarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioRequestDTO dto) {
        return ResponseEntity.ok(horarioService.actualizar(id, dto));
    }

    @Operation(summary = "ELIMINAR HORARIO", description = "Elimina un horario por su ID. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Horario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (horarioService.obtenerPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        horarioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
package Horario.Horario.Controller;

import Horario.Horario.Assembler.HorarioModelAssembler;
import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Service.HorarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Tag(name = "HORARIOS", description = "GESTIÓN DE HORARIOS")
@RestController
@RequestMapping("/v1/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;
    private final HorarioModelAssembler assembler;

    @Operation(summary = "OBTENER TODOS LOS HORARIOS", description = "Retorna la lista de todos los horarios. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "Sin permisos suficientes")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<HorarioResponseDTO>>> obtenerTodos() {
        log.info("GET /v1/horarios - LISTAR TODOS");
        List<EntityModel<HorarioResponseDTO>> horarios = horarioService.obtenerTodos().stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(horarios,
                linkTo(methodOn(HorarioController.class).obtenerTodos()).withSelfRel()));
    }

    @Operation(summary = "OBTENER HORARIO POR ID", description = "Retorna un horario específico por su ID. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario encontrado"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<HorarioResponseDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /v1/horarios/{} - BUSCAR POR ID", id);
        return horarioService.obtenerPorId(id)
                .map(dto -> ResponseEntity.ok(assembler.toModel(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "OBTENER HORARIOS POR ESTABLECIMIENTO", description = "Retorna todos los horarios de un establecimiento. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "Sin horarios para ese establecimiento")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/establecimiento/{establecimientoId}")
    public ResponseEntity<CollectionModel<EntityModel<HorarioResponseDTO>>> obtenerPorEstablecimiento(@PathVariable Long establecimientoId) {
        log.info("GET /v1/horarios/establecimiento/{} - BUSCAR POR ESTABLECIMIENTO", establecimientoId);
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorEstablecimiento(establecimientoId);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        List<EntityModel<HorarioResponseDTO>> modelos = horarios.stream().map(assembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(modelos,
                linkTo(methodOn(HorarioController.class).obtenerPorEstablecimiento(establecimientoId)).withSelfRel()));
    }

    @Operation(summary = "OBTENER HORARIOS POR ESTABLECIMIENTO Y DÍA", description = "Retorna horarios de un establecimiento filtrados por día de la semana. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "Sin horarios para ese establecimiento y día")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/establecimiento/{establecimientoId}/dia/{diaSemana}")
    public ResponseEntity<CollectionModel<EntityModel<HorarioResponseDTO>>> obtenerPorEstablecimientoYDia(
            @PathVariable Long establecimientoId,
            @PathVariable String diaSemana) {
        log.info("GET /v1/horarios/establecimiento/{}/dia/{} - BUSCAR POR ESTABLECIMIENTO Y DÍA", establecimientoId, diaSemana);
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorEstablecimientoYDia(establecimientoId, diaSemana);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        List<EntityModel<HorarioResponseDTO>> modelos = horarios.stream().map(assembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(modelos,
                linkTo(methodOn(HorarioController.class).obtenerPorEstablecimientoYDia(establecimientoId, diaSemana)).withSelfRel()));
    }

    @Operation(summary = "OBTENER HORARIOS POR DÍA", description = "Retorna todos los horarios de un día de la semana. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "204", description = "Sin horarios para ese día")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/dia/{diaSemana}")
    public ResponseEntity<CollectionModel<EntityModel<HorarioResponseDTO>>> obtenerPorDia(@PathVariable String diaSemana) {
        log.info("GET /v1/horarios/dia/{} - BUSCAR POR DÍA", diaSemana);
        List<HorarioResponseDTO> horarios = horarioService.obtenerPorDia(diaSemana);
        if (horarios.isEmpty()) return ResponseEntity.noContent().build();
        List<EntityModel<HorarioResponseDTO>> modelos = horarios.stream().map(assembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(modelos,
                linkTo(methodOn(HorarioController.class).obtenerPorDia(diaSemana)).withSelfRel()));
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
    public ResponseEntity<EntityModel<HorarioResponseDTO>> guardar(@Valid
                                                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del horario")
                                                                   @RequestBody HorarioRequestDTO dto) {
        log.info("POST /v1/horarios - CREAR HORARIO establecimientoId={}", dto.getEstablecimientoId());
        HorarioResponseDTO creado = horarioService.guardar(dto);
        return ResponseEntity.status(201).body(assembler.toModel(creado));
    }

    @Operation(summary = "ACTUALIZAR HORARIO", description = "Actualiza un horario existente. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<HorarioResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioRequestDTO dto) {
        log.info("PUT /v1/horarios/{} - ACTUALIZAR HORARIO", id);
        return ResponseEntity.ok(assembler.toModel(horarioService.actualizar(id, dto)));
    }

    @Operation(summary = "ELIMINAR HORARIO", description = "Elimina un horario por su ID. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Horario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        log.info("DELETE /v1/horarios/{} - ELIMINAR HORARIO", id);
        if (horarioService.obtenerPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        horarioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
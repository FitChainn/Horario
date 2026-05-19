package Horario.Horario.Service;

import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Model.Horario;
import Horario.Horario.Repository.HorarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private HorarioResponseDTO mapToDTO(Horario horario) {
        return new HorarioResponseDTO(
                horario.getId(),
                horario.getHoraIniTurno(),
                horario.getHoraFinTurno(),
                horario.getEntrenadorId(),
                horario.getEstablecimientoId(),
                horario.getDiaSemana(),
                null
        );
    }

    private HorarioResponseDTO mapToDTOConEntrenador(Horario horario) {
        HorarioResponseDTO dto = mapToDTO(horario);
        try {
            Object entrenador = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/api/entrenadores/{id}/simple", horario.getEntrenadorId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            dto.setEntrenador(entrenador);
        } catch (Exception e) {
            log.warn("No se pudo obtener entrenador con ID: {}", horario.getEntrenadorId());
            dto.setEntrenador(null);
        }
        return dto;
    }

    public List<HorarioResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los horarios");
        return horarioRepository.findAll()
                .stream()
                .map(this::mapToDTOConEntrenador)
                .collect(Collectors.toList());
    }

    public Optional<HorarioResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando horario con ID: {}", id);
        return horarioRepository.findById(id).map(this::mapToDTOConEntrenador);
    }

    public List<HorarioResponseDTO> obtenerPorEntrenador(Long entrenadorId) {
        log.info("Buscando horarios del entrenador ID: {}", entrenadorId);
        return horarioRepository.findByEntrenadorId(entrenadorId)
                .stream()
                .map(this::mapToDTOConEntrenador)
                .collect(Collectors.toList());
    }

    public List<HorarioResponseDTO> obtenerPorEstablecimiento(Long establecimientoId) {
        log.info("Buscando horarios del establecimiento ID: {}", establecimientoId);
        return horarioRepository.findByEstablecimientoId(establecimientoId)
                .stream()
                .map(this::mapToDTOConEntrenador)
                .collect(Collectors.toList());
    }

    public List<HorarioResponseDTO> obtenerPorDia(String diaSemana) {
        log.info("Buscando horarios del día: {}", diaSemana);
        return horarioRepository.findByDiaSemana(diaSemana)
                .stream()
                .map(this::mapToDTOConEntrenador)
                .collect(Collectors.toList());
    }

    public HorarioResponseDTO guardar(HorarioRequestDTO dto) {
        log.info("Guardando horario para entrenador ID: {}", dto.getEntrenadorId());
        Horario horario = new Horario();
        horario.setHoraIniTurno(dto.getHoraIniTurno());
        horario.setHoraFinTurno(dto.getHoraFinTurno());
        horario.setEntrenadorId(dto.getEntrenadorId());
        horario.setEstablecimientoId(dto.getEstablecimientoId());
        horario.setDiaSemana(dto.getDiaSemana());
        Horario guardado = horarioRepository.save(horario);
        log.info("Horario guardado con ID: {}", guardado.getId());
        return mapToDTOConEntrenador(guardado);
    }

    public HorarioResponseDTO actualizar(Long id, HorarioRequestDTO dto) {
        log.info("Actualizando horario con ID: {}", id);
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));
        horario.setHoraIniTurno(dto.getHoraIniTurno());
        horario.setHoraFinTurno(dto.getHoraFinTurno());
        horario.setEntrenadorId(dto.getEntrenadorId());
        horario.setEstablecimientoId(dto.getEstablecimientoId());
        horario.setDiaSemana(dto.getDiaSemana());
        return mapToDTOConEntrenador(horarioRepository.save(horario));
    }

    public void eliminarPorId(Long id) {
        log.info("Eliminando horario con ID: {}", id);
        horarioRepository.deleteById(id);
    }
}
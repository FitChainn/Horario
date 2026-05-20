package Horario.Horario.Service;

import Horario.Horario.Dto.EstablecimientoDTO;
import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Model.Horario;
import Horario.Horario.Repository.HorarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${establecimiento-service.url}")
    private String establecimientoServiceUrl;

    private HorarioResponseDTO mapToDTO(Horario horario) {
        HorarioResponseDTO dto = new HorarioResponseDTO();
        dto.setId(horario.getId());
        dto.setEstablecimientoId(horario.getEstablecimientoId());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraApertura(horario.getHoraApertura());
        dto.setHoraCierre(horario.getHoraCierre());
        dto.setAbierto(horario.isAbierto());

        try {
            EstablecimientoDTO establecimiento = webClientBuilder.build()
                    .get()
                    .uri(establecimientoServiceUrl + "/{id}", horario.getEstablecimientoId())
                    .retrieve()
                    .bodyToMono(EstablecimientoDTO.class)
                    .block();
            dto.setEstablecimiento(establecimiento);
        } catch (Exception e) {
            log.warn("No se pudo obtener establecimiento con ID: {}", horario.getEstablecimientoId());
            dto.setEstablecimiento(null);
        }
        return dto;
    }

    public List<HorarioResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los horarios");
        return horarioRepository.findAllByOrderByIdAsc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<HorarioResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando horario con ID: {}", id);
        return horarioRepository.findById(id).map(this::mapToDTO);
    }

    public List<HorarioResponseDTO> obtenerPorEstablecimiento(Long establecimientoId) {
        log.info("Buscando horarios del establecimiento ID: {}", establecimientoId);
        return horarioRepository.findByEstablecimientoIdOrderByIdAsc(establecimientoId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<HorarioResponseDTO> obtenerPorDia(String diaSemana) {
        log.info("Buscando horarios del día: {}", diaSemana);
        return horarioRepository.findByDiaSemanaOrderByIdAsc(diaSemana).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<HorarioResponseDTO> obtenerPorEstablecimientoYDia(Long establecimientoId, String diaSemana) {
        log.info("Buscando horario del establecimiento {} para el día {}", establecimientoId, diaSemana);
        return horarioRepository.findByEstablecimientoIdAndDiaSemanaOrderByIdAsc(establecimientoId, diaSemana).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HorarioResponseDTO guardar(HorarioRequestDTO dto) {
        log.info("Guardando horario para establecimiento ID: {}", dto.getEstablecimientoId());
        Horario horario = new Horario();
        horario.setEstablecimientoId(dto.getEstablecimientoId());
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraApertura(dto.getHoraApertura());
        horario.setHoraCierre(dto.getHoraCierre());
        horario.setAbierto(dto.isAbierto());
        Horario guardado = horarioRepository.save(horario);
        log.info("Horario guardado con ID: {}", guardado.getId());
        return mapToDTO(guardado);
    }

    public HorarioResponseDTO actualizar(Long id, HorarioRequestDTO dto) {
        log.info("Actualizando horario con ID: {}", id);
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));
        horario.setEstablecimientoId(dto.getEstablecimientoId());
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraApertura(dto.getHoraApertura());
        horario.setHoraCierre(dto.getHoraCierre());
        horario.setAbierto(dto.isAbierto());
        return mapToDTO(horarioRepository.save(horario));
    }

    public void eliminarPorId(Long id) {
        log.info("Eliminando horario con ID: {}", id);
        horarioRepository.deleteById(id);
    }
}

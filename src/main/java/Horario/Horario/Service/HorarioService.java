package Horario.Horario.Service;

import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Model.Horario;
import Horario.Horario.Repository.HorarioRepository;
import Horario.Horario.WebClient.EstablecimientoClient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private EstablecimientoClient establecimientoClient;

    private HorarioResponseDTO mapToDTO(Horario horario) {
        HorarioResponseDTO dto = new HorarioResponseDTO();
        dto.setId(horario.getId());
        dto.setEstablecimientoId(horario.getEstablecimientoId());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraApertura(horario.getHoraApertura());
        dto.setHoraCierre(horario.getHoraCierre());
        dto.setAbierto(horario.isAbierto());
        dto.setEstablecimiento(establecimientoClient.obtenerEstablecimientoPorId(horario.getEstablecimientoId()));
        return dto;
    }

    public List<HorarioResponseDTO> obtenerTodos() {
        log.info("OBTENIENDO TODOS LOS HORARIOS");
        return horarioRepository.findAllByOrderByIdAsc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<HorarioResponseDTO> obtenerPorId(Long id) {
        log.info("BUSCANDO HORARIO CON ID: {}", id);
        return horarioRepository.findById(id).map(this::mapToDTO);
    }

    public List<HorarioResponseDTO> obtenerPorEstablecimiento(Long establecimientoId) {
        log.info("BUSCANDO HORARIOS DEL ESTABLECIMIENTO ID: {}", establecimientoId);
        return horarioRepository.findByEstablecimientoIdOrderByIdAsc(establecimientoId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<HorarioResponseDTO> obtenerPorDia(String diaSemana) {
        log.info("BUSCANDO HORARIOS DEL DÍA: {}", diaSemana);
        return horarioRepository.findByDiaSemanaOrderByIdAsc(diaSemana).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<HorarioResponseDTO> obtenerPorEstablecimientoYDia(Long establecimientoId, String diaSemana) {
        log.info("BUSCANDO HORARIO DEL ESTABLECIMIENTO {} PARA EL DÍA {}", establecimientoId, diaSemana);
        return horarioRepository.findByEstablecimientoIdAndDiaSemanaOrderByIdAsc(establecimientoId, diaSemana).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HorarioResponseDTO guardar(HorarioRequestDTO dto) {
        log.info("GUARDANDO HORARIO PARA ESTABLECIMIENTO ID: {}", dto.getEstablecimientoId());
        Horario horario = new Horario();
        horario.setEstablecimientoId(dto.getEstablecimientoId());
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraApertura(dto.getHoraApertura());
        horario.setHoraCierre(dto.getHoraCierre());
        horario.setAbierto(dto.isAbierto());
        Horario guardado = horarioRepository.save(horario);
        log.info("HORARIO GUARDADO CON ID: {}", guardado.getId());
        return mapToDTO(guardado);
    }

    public HorarioResponseDTO actualizar(Long id, HorarioRequestDTO dto) {
        log.info("ACTUALIZANDO HORARIO CON ID: {}", id);
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Horario no encontrado con ID: " + id));
        horario.setEstablecimientoId(dto.getEstablecimientoId());
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraApertura(dto.getHoraApertura());
        horario.setHoraCierre(dto.getHoraCierre());
        horario.setAbierto(dto.isAbierto());
        return mapToDTO(horarioRepository.save(horario));
    }

    public void eliminarPorId(Long id) {
        log.info("ELIMINANDO HORARIO CON ID: {}", id);
        horarioRepository.deleteById(id);
    }
}
package Horario.Horario;

import Horario.Horario.Dto.EstablecimientoDTO;
import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Model.Horario;
import Horario.Horario.Repository.HorarioRepository;
import Horario.Horario.Service.HorarioService;
import Horario.Horario.WebClient.EstablecimientoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PRUEBAS UNITARIAS DEL SERVICE DE HORARIOS")
public class HorarioServiceTest {

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private EstablecimientoClient establecimientoClient;

    @InjectMocks
    private HorarioService horarioService;

    private Horario horario;
    private EstablecimientoDTO establecimientoDTO;
    private HorarioRequestDTO hRequest;

    @BeforeEach
    void setUp() {
        horario = new Horario(1L, 1L, "LUNES", LocalTime.of(8, 0), LocalTime.of(22, 0), true);
        establecimientoDTO = new EstablecimientoDTO(1L, "GYM CENTRAL", "CALLE LIMA");
        hRequest = new HorarioRequestDTO(1L, "LUNES", LocalTime.of(8, 0), LocalTime.of(22, 0), true);
    }

    @Test
    @DisplayName("DEBE OBTENER TODOS LOS HORARIOS")
    void shouldObtenerTodos() {
        when(horarioRepository.findAllByOrderByIdAsc()).thenReturn(List.of(horario));
        when(establecimientoClient.obtenerEstablecimientoPorId(1L)).thenReturn(establecimientoDTO);

        List<HorarioResponseDTO> resultado = horarioService.obtenerTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("LUNES", resultado.get(0).getDiaSemana());
        assertEquals("GYM CENTRAL", resultado.get(0).getEstablecimiento().getNombre());
        verify(horarioRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    @DisplayName("DEBE OBTENER HORARIO POR ID")
    void shouldObtenerPorId() {
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(horario));
        when(establecimientoClient.obtenerEstablecimientoPorId(1L)).thenReturn(establecimientoDTO);

        Optional<HorarioResponseDTO> resultado = horarioService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("LUNES", resultado.get().getDiaSemana());
        verify(horarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("DEBE OBTENER HORARIOS POR ESTABLECIMIENTO")
    void shouldObtenerPorEstablecimiento() {
        when(horarioRepository.findByEstablecimientoIdOrderByIdAsc(1L)).thenReturn(List.of(horario));
        when(establecimientoClient.obtenerEstablecimientoPorId(1L)).thenReturn(establecimientoDTO);

        List<HorarioResponseDTO> resultado = horarioService.obtenerPorEstablecimiento(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(1L, resultado.get(0).getEstablecimientoId());
        verify(horarioRepository, times(1)).findByEstablecimientoIdOrderByIdAsc(1L);
    }

    @Test
    @DisplayName("DEBE OBTENER HORARIOS POR DIA")
    void shouldObtenerPorDia() {
        when(horarioRepository.findByDiaSemanaOrderByIdAsc("LUNES")).thenReturn(List.of(horario));
        when(establecimientoClient.obtenerEstablecimientoPorId(1L)).thenReturn(establecimientoDTO);

        List<HorarioResponseDTO> resultado = horarioService.obtenerPorDia("LUNES");

        assertFalse(resultado.isEmpty());
        assertEquals("LUNES", resultado.get(0).getDiaSemana());
        verify(horarioRepository, times(1)).findByDiaSemanaOrderByIdAsc("LUNES");
    }

    @Test
    @DisplayName("DEBE OBTENER HORARIOS POR ESTABLECIMIENTO Y DIA")
    void shouldObtenerPorEstablecimientoYDia() {
        when(horarioRepository.findByEstablecimientoIdAndDiaSemanaOrderByIdAsc(1L, "LUNES")).thenReturn(List.of(horario));
        when(establecimientoClient.obtenerEstablecimientoPorId(1L)).thenReturn(establecimientoDTO);

        List<HorarioResponseDTO> resultado = horarioService.obtenerPorEstablecimientoYDia(1L, "LUNES");

        assertFalse(resultado.isEmpty());
        assertEquals(1L, resultado.get(0).getEstablecimientoId());
        assertEquals("LUNES", resultado.get(0).getDiaSemana());
        verify(horarioRepository, times(1)).findByEstablecimientoIdAndDiaSemanaOrderByIdAsc(1L, "LUNES");
    }

    @Test
    @DisplayName("DEBE GUARDAR UN HORARIO")
    void shouldGuardarHorario() {
        when(horarioRepository.save(any(Horario.class))).thenReturn(horario);
        when(establecimientoClient.obtenerEstablecimientoPorId(1L)).thenReturn(establecimientoDTO);

        HorarioResponseDTO resultado = horarioService.guardar(hRequest);

        assertNotNull(resultado);
        assertEquals("LUNES", resultado.getDiaSemana());
        assertEquals(1L, resultado.getEstablecimientoId());
        verify(horarioRepository, times(1)).save(any(Horario.class));
    }

    @Test
    @DisplayName("DEBE ACTUALIZAR UN HORARIO")
    void shouldActualizarHorario() {
        Horario actualizado = new Horario(1L, 1L, "MARTES", LocalTime.of(8, 0), LocalTime.of(22, 0), true);
        hRequest.setDiaSemana("MARTES");

        when(horarioRepository.findById(1L)).thenReturn(Optional.of(horario));
        when(horarioRepository.save(any(Horario.class))).thenReturn(actualizado);
        when(establecimientoClient.obtenerEstablecimientoPorId(1L)).thenReturn(establecimientoDTO);

        HorarioResponseDTO resultado = horarioService.actualizar(1L, hRequest);

        assertNotNull(resultado);
        assertEquals("MARTES", resultado.getDiaSemana());
        verify(horarioRepository, times(1)).findById(1L);
        verify(horarioRepository, times(1)).save(any(Horario.class));
    }

    @Test
    @DisplayName("DEBE LANZAR EXCEPCION AL ACTUALIZAR HORARIO INEXISTENTE")
    void shouldThrowWhenActualizarNotFound() {
        when(horarioRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> horarioService.actualizar(99L, hRequest));

        assertEquals("Horario no encontrado con ID: 99", ex.getMessage());
        verify(horarioRepository, never()).save(any(Horario.class));
    }

    @Test
    @DisplayName("DEBE ELIMINAR UN HORARIO POR ID")
    void shouldEliminarPorId() {
        doNothing().when(horarioRepository).deleteById(1L);

        horarioService.eliminarPorId(1L);

        verify(horarioRepository, times(1)).deleteById(1L);
    }
}
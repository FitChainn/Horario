package Horario.Horario;

import Horario.Horario.Config.SecurityConfig;
import Horario.Horario.Controller.HorarioController;
import Horario.Horario.Dto.EstablecimientoDTO;
import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Filter.RolHeaderFilter;
import Horario.Horario.Service.HorarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HorarioController.class)
@Import({SecurityConfig.class, RolHeaderFilter.class})
@DisplayName("PRUEBAS UNITARIAS DEL CONTROLLER DE HORARIOS")
public class HorarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HorarioService horarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private HorarioResponseDTO hResponse;
    private HorarioRequestDTO hRequest;

    @BeforeEach
    void setUp() {
        EstablecimientoDTO estDTO = new EstablecimientoDTO(1L, "GYM CENTRAL", "CALLE LIMA");
        hResponse = new HorarioResponseDTO(1L, 1L, "LUNES", LocalTime.of(8, 0), LocalTime.of(22, 0), true, estDTO);
        hRequest = new HorarioRequestDTO(1L, "LUNES", LocalTime.of(8, 0), LocalTime.of(22, 0), true);
    }

    @Test
    @DisplayName("DEBE RETORNAR TODOS LOS HORARIOS")
    void GET_obtenerTodos() throws Exception {
        when(horarioService.obtenerTodos()).thenReturn(List.of(hResponse));

        mockMvc.perform(get("/v1/horarios")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].diaSemana").value("LUNES"));
    }

    @Test
    @DisplayName("DEBE OBTENER UN HORARIO POR ID")
    void GET_obtenerPorId() throws Exception {
        when(horarioService.obtenerPorId(1L)).thenReturn(Optional.of(hResponse));

        mockMvc.perform(get("/v1/horarios/1")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.diaSemana").value("LUNES"));
    }

    @Test
    @DisplayName("DEBE RETORNAR 404 SI EL HORARIO NO EXISTE POR ID")
    void GET_obtenerPorId_NotFound() throws Exception {
        when(horarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/horarios/99")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DEBE OBTENER HORARIOS POR ESTABLECIMIENTO")
    void GET_obtenerPorEstablecimiento() throws Exception {
        when(horarioService.obtenerPorEstablecimiento(1L)).thenReturn(List.of(hResponse));

        mockMvc.perform(get("/v1/horarios/establecimiento/1")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].establecimientoId").value(1L));
    }

    @Test
    @DisplayName("DEBE RETORNAR 204 SI NO HAY HORARIOS POR ESTABLECIMIENTO")
    void GET_obtenerPorEstablecimiento_NoContent() throws Exception {
        when(horarioService.obtenerPorEstablecimiento(99L)).thenReturn(List.of());

        mockMvc.perform(get("/v1/horarios/establecimiento/99")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DEBE OBTENER HORARIOS POR ESTABLECIMIENTO Y DIA")
    void GET_obtenerPorEstablecimientoYDia() throws Exception {
        when(horarioService.obtenerPorEstablecimientoYDia(1L, "LUNES")).thenReturn(List.of(hResponse));

        mockMvc.perform(get("/v1/horarios/establecimiento/1/dia/LUNES")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].diaSemana").value("LUNES"));
    }

    @Test
    @DisplayName("DEBE OBTENER HORARIOS POR DIA")
    void GET_obtenerPorDia() throws Exception {
        when(horarioService.obtenerPorDia("LUNES")).thenReturn(List.of(hResponse));

        mockMvc.perform(get("/v1/horarios/dia/LUNES")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].diaSemana").value("LUNES"));
    }

    @Test
    @DisplayName("DEBE CREAR UN HORARIO")
    void POST_guardar() throws Exception {
        when(horarioService.guardar(any(HorarioRequestDTO.class))).thenReturn(hResponse);

        mockMvc.perform(post("/v1/horarios")
                        .header("X-User-Rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.diaSemana").value("LUNES"));
    }

    @Test
    @DisplayName("DEBE DEVOLVER 400 AL CREAR CON DATOS INVALIDOS")
    void POST_guardar_Invalido() throws Exception {
        HorarioRequestDTO reqInvalido = new HorarioRequestDTO();

        mockMvc.perform(post("/v1/horarios")
                        .header("X-User-Rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DEBE ACTUALIZAR UN HORARIO")
    void PUT_actualizar() throws Exception {
        hRequest.setDiaSemana("MARTES");
        HorarioResponseDTO actualizado = new HorarioResponseDTO(1L, 1L, "MARTES", LocalTime.of(8, 0), LocalTime.of(22, 0), true, hResponse.getEstablecimiento());

        when(horarioService.actualizar(eq(1L), any(HorarioRequestDTO.class))).thenReturn(actualizado);

        mockMvc.perform(put("/v1/horarios/1")
                        .header("X-User-Rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diaSemana").value("MARTES"));
    }

    @Test
    @DisplayName("DEBE ELIMINAR UN HORARIO")
    void DELETE_eliminar() throws Exception {
        when(horarioService.obtenerPorId(1L)).thenReturn(Optional.of(hResponse));
        doNothing().when(horarioService).eliminarPorId(1L);

        mockMvc.perform(delete("/v1/horarios/1")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DEBE DEVOLVER 404 AL ELIMINAR UN HORARIO INEXISTENTE")
    void DELETE_eliminar_NotFound() throws Exception {
        when(horarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/v1/horarios/99")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isNotFound());
    }
}
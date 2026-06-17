package Horario.Horario;

import Horario.Horario.Controller.HorarioController;
import Horario.Horario.Dto.HorarioRequestDTO;
import Horario.Horario.Dto.HorarioResponseDTO;
import Horario.Horario.Service.HorarioService;
import Horario.Horario.Config.SecurityConfig;
import Horario.Horario.Filter.RolHeaderFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HorarioController.class)
@Import({SecurityConfig.class, RolHeaderFilter.class})
@WithMockUser
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
        hResponse = new HorarioResponseDTO(1L, 1L, "LUNES", LocalTime.of(9, 0), LocalTime.of(20, 0), true, null);
        hRequest = new HorarioRequestDTO(1L, "MIERCOLES", LocalTime.of(9, 0), LocalTime.of(20, 0), true);
    }

    @Test
    void Get_obtenerTodos() throws Exception {
        when(horarioService.obtenerTodos()).thenReturn(List.of(hResponse));

        mockMvc.perform(get("/v1/horarios")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].diaSemana").value("LUNES"));
    }

    @Test
    void Post_guardar201() throws Exception {
        when(horarioService.guardar(any(HorarioRequestDTO.class))).thenReturn(hResponse);

        mockMvc.perform(post("/v1/horarios")
                        .with(csrf())
                        .header("X-User-Rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void Get_obtenerPorId() throws Exception {
        when(horarioService.obtenerPorId(1L)).thenReturn(Optional.of(hResponse));

        mockMvc.perform(get("/v1/horarios/1")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diaSemana").value("LUNES"));
    }

    @Test
    void Put_actualizar() throws Exception {
        when(horarioService.actualizar(eq(1L), any(HorarioRequestDTO.class))).thenReturn(hResponse);

        mockMvc.perform(put("/v1/horarios/1")
                        .with(csrf())
                        .header("X-User-Rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diaSemana").value("LUNES"));
    }

    @Test
    void Delete_eliminar() throws Exception {
        when(horarioService.obtenerPorId(1L)).thenReturn(Optional.of(hResponse));

        mockMvc.perform(delete("/v1/horarios/1")
                        .with(csrf())
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isNoContent());
    }
}
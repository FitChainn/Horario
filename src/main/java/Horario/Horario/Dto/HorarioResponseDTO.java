package Horario.Horario.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioResponseDTO {
    private Long id;
    private LocalTime horaIniTurno;
    private LocalTime horaFinTurno;
    private Long entrenadorId;
    private Long establecimientoId;
    private String diaSemana;
    private Object entrenador;
}
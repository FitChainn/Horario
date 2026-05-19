package Horario.Horario.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioRequestDTO {

    @NotNull(message = "La hora de inicio no puede estar vacía")
    private LocalTime horaIniTurno;

    @NotNull(message = "La hora de fin no puede estar vacía")
    private LocalTime horaFinTurno;

    @NotNull(message = "El entrenador es obligatorio")
    private Long entrenadorId;

    @NotNull(message = "El establecimiento es obligatorio")
    private Long establecimientoId;

    @NotBlank(message = "El día de la semana es obligatorio")
    private String diaSemana;
}
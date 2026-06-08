package Horario.Horario.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DATOS PARA CREAR Y/O ACTUALIZAR EL HORARIO")
public class HorarioRequestDTO {

    @NotNull(message = "El establecimiento es obligatorio")
    @Schema(description = "ID del establecimiento", example = "1")
    private Long establecimientoId;

    @NotBlank(message = "El día de la semana es obligatorio")
    @Schema(description = "DÍA DE LA SEMANA", example = "LUNES")
    private String diaSemana;

    private LocalTime horaApertura;

    private LocalTime horaCierre;

    private boolean abierto;
}

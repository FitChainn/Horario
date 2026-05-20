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
    private Long establecimientoId;
    private String diaSemana;
    private LocalTime horaApertura;
    private LocalTime horaCierre;
    private boolean abierto;
    private EstablecimientoDTO establecimiento;
}

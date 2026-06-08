package Horario.Horario.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DATOS DEL ESTABLECIMIENTO")
public class EstablecimientoDTO {
    private Long id;
    private String nombre;
    private String direccion;
}

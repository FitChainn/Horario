package Horario.Horario.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstablecimientoDTO {
    private Long id;
    private String nombre;
    private String direccion;
}

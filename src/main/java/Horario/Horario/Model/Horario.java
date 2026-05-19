package Horario.Horario.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "horarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime horaIniTurno;

    @Column(nullable = false)
    private LocalTime horaFinTurno;

    @Column(nullable = false)
    private Long entrenadorId;

    @Column(nullable = false)
    private Long establecimientoId;

    @Column(nullable = false)
    private String diaSemana;
}
package Horario.Horario.Config;

import Horario.Horario.Model.Horario;
import Horario.Horario.Repository.HorarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Datainitializer implements CommandLineRunner {

    private final HorarioRepository horarioRepository;

    private static final LocalTime APERTURA_SEMANA = LocalTime.of(7, 0);
    private static final LocalTime CIERRE_SEMANA   = LocalTime.of(21, 0);
    private static final LocalTime APERTURA_SABADO = LocalTime.of(9, 0);
    private static final LocalTime CIERRE_SABADO   = LocalTime.of(15, 0);

    @Override
    public void run(String... args) {
        log.info(">>> DataInitializer: insertando horarios del establecimiento...");
        crearHorariosEstablecimiento(1L);
        crearHorariosEstablecimiento(2L);
        log.info(">>> DataInitializer: {} horarios insertados correctamente.", horarioRepository.count());
    }

    private void crearHorariosEstablecimiento(Long establecimientoId) {
        List<String> diasSemana = List.of("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES");

        for (String dia : diasSemana) {
            horarioRepository.save(new Horario(null, establecimientoId, dia,
                    APERTURA_SEMANA, CIERRE_SEMANA, true));
        }

        horarioRepository.save(new Horario(null, establecimientoId, "SABADO",
                APERTURA_SABADO, CIERRE_SABADO, true));

        horarioRepository.save(new Horario(null, establecimientoId, "DOMINGO",
                null, null, false));

        horarioRepository.save(new Horario(null, establecimientoId, "FESTIVO",
                null, null, false));
    }
}

package Horario.Horario.Config;

import Horario.Horario.Model.Horario;
import Horario.Horario.Repository.HorarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Datainitializer implements CommandLineRunner {

    private final HorarioRepository horarioRepository;

    @Override
    public void run(String... args) {
        if (horarioRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial.");
            return;
        }

        log.info(">>> DataInitializer: BD vacía detectada, insertando datos de prueba...");

        // Entrenador 1 - Establecimiento 1
        horarioRepository.save(new Horario(null, LocalTime.of(8, 0), LocalTime.of(10, 0), 1L, 1L, "LUNES"));
        horarioRepository.save(new Horario(null, LocalTime.of(10, 0), LocalTime.of(12, 0), 1L, 1L, "MIERCOLES"));
        horarioRepository.save(new Horario(null, LocalTime.of(14, 0), LocalTime.of(16, 0), 1L, 1L, "VIERNES"));

        // Entrenador 2 - Establecimiento 1
        horarioRepository.save(new Horario(null, LocalTime.of(9, 0), LocalTime.of(11, 0), 2L, 1L, "MARTES"));
        horarioRepository.save(new Horario(null, LocalTime.of(15, 0), LocalTime.of(17, 0), 2L, 1L, "JUEVES"));

        // Entrenador 3 - Establecimiento 2
        horarioRepository.save(new Horario(null, LocalTime.of(7, 0), LocalTime.of(9, 0), 3L, 2L, "LUNES"));
        horarioRepository.save(new Horario(null, LocalTime.of(18, 0), LocalTime.of(20, 0), 3L, 2L, "MIERCOLES"));

        // Entrenador 4 - Establecimiento 2
        horarioRepository.save(new Horario(null, LocalTime.of(8, 0), LocalTime.of(10, 0), 4L, 2L, "MARTES"));
        horarioRepository.save(new Horario(null, LocalTime.of(16, 0), LocalTime.of(18, 0), 4L, 2L, "VIERNES"));

        log.info(">>> DataInitializer: {} horarios insertados correctamente.", horarioRepository.count());
    }
}
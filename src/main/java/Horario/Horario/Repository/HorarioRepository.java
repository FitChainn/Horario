package Horario.Horario.Repository;

import Horario.Horario.Model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findByEntrenadorId(Long entrenadorId);
    List<Horario> findByEstablecimientoId(Long establecimientoId);
    List<Horario> findByDiaSemana(String diaSemana);
}
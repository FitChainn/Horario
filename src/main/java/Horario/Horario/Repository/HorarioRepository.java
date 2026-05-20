package Horario.Horario.Repository;

import Horario.Horario.Model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findAllByOrderByIdAsc();
    List<Horario> findByEstablecimientoIdOrderByIdAsc(Long establecimientoId);
    List<Horario> findByDiaSemanaOrderByIdAsc(String diaSemana);
    List<Horario> findByEstablecimientoIdAndDiaSemanaOrderByIdAsc(Long establecimientoId, String diaSemana);
}

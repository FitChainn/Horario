package Horario.Horario;

import Horario.Horario.Model.Horario;
import Horario.Horario.Repository.HorarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("PRUEBAS UNITARIAS DEL REPOSITORY DE HORARIO")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HorarioRepositoryTest {

    @Autowired
    private HorarioRepository repo;

    @Autowired
    private TestEntityManager em;

    // FALTA EL BEFORE EACH PAR ALIMPIAR LA BD CREO
    @BeforeEach
    void limpiarBd(){
        repo.deleteAll();
        em.flush();
    }

    private Horario crearHorario(Long establecimientoId, String diaSemana) {
        Horario h = new Horario();
        h.setEstablecimientoId(establecimientoId);
        h.setDiaSemana(diaSemana);
        h.setHoraApertura(LocalTime.of(8, 0));
        h.setHoraCierre(LocalTime.of(22, 0));
        h.setAbierto(true);
        return em.persistAndFlush(h);
    }

    @Test
    @DisplayName("DEBE ENCONTRAR UN HORARIO POR ID")
    void findById_ShouldReturnHorario() {
        Horario h = crearHorario(1L, "LUNES");

        Optional<Horario> result = repo.findById(h.getId());

        assertTrue(result.isPresent());
        assertEquals("LUNES", result.get().getDiaSemana());
    }

    @Test
    @DisplayName("DEBE RETORNAR VACIO SI HORARIO NO EXISTE")
    void findById_ShouldReturnEmpty() {
        Optional<Horario> result = repo.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("DEBE ENCONTRAR TODOS ORDENADOS POR ID")
    void findAllByOrderByIdAsc_ShouldReturnHorarios() {
        crearHorario(1L, "LUNES");
        crearHorario(2L, "MARTES");

        List<Horario> result = repo.findAllByOrderByIdAsc();

        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("DEBE ENCONTRAR HORARIOS POR ESTABLECIMIENTO ID")
    void findByEstablecimientoIdOrderByIdAsc_ShouldReturnHorarios() {
        crearHorario(2L, "LUNES");
        crearHorario(2L, "MARTES");
        crearHorario(3L, "MIERCOLES");

        List<Horario> result = repo.findByEstablecimientoIdOrderByIdAsc(2L);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(h -> h.getEstablecimientoId().equals(2L)));
    }

    @Test
    @DisplayName("DEBE RETORNAR LISTA VACIA SI ESTABLECIMIENTO NO TIENE HORARIOS")
    void findByEstablecimientoIdOrderByIdAsc_ShouldReturnEmpty() {
        List<Horario> result = repo.findByEstablecimientoIdOrderByIdAsc(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("DEBE ENCONTRAR HORARIOS POR DIA")
    void findByDiaSemanaOrderByIdAsc_ShouldReturnHorarios() {
        crearHorario(2L, "LUNES");
        crearHorario(3L, "LUNES");
        crearHorario(4L, "MARTES");

        List<Horario> result = repo.findByDiaSemanaOrderByIdAsc("LUNES");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(h -> h.getDiaSemana().equals("LUNES")));
    }

    @Test
    @DisplayName("DEBE RETORNAR LISTA VACIA SI NO HAY HORARIOS ESE DIA")
    void findByDiaSemanaOrderByIdAsc_ShouldReturnEmpty() {
        List<Horario> result = repo.findByDiaSemanaOrderByIdAsc("DOMINGO");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("DEBE ENCONTRAR HORARIOS POR ESTABLECIMIENTO Y DIA")
    void findByEstablecimientoIdAndDiaSemanaOrderByIdAsc_ShouldReturnHorarios() {
        crearHorario(2L, "LUNES");
        crearHorario(2L, "MARTES");

        List<Horario> result = repo.findByEstablecimientoIdAndDiaSemanaOrderByIdAsc(2L, "LUNES");

        assertEquals(1, result.size());
        assertEquals("LUNES", result.get(0).getDiaSemana());
    }

    @Test
    @DisplayName("DEBE GUARDAR UN HORARIO")
    void save_ShouldPersistHorario() {
        Horario h = new Horario();
        h.setEstablecimientoId(2L);
        h.setDiaSemana("VIERNES");
        h.setHoraApertura(LocalTime.of(8, 0));
        h.setHoraCierre(LocalTime.of(20, 0));
        h.setAbierto(true);

        Horario saved = repo.save(h);

        assertNotNull(saved.getId());
        assertEquals("VIERNES", saved.getDiaSemana());
    }

    @Test
    @DisplayName("DEBE ELIMINAR UN HORARIO")
    void delete_ShouldRemoveHorario() {
        Horario h = crearHorario(2L, "SABADO");
        Long id = h.getId();

        repo.deleteById(id);
        em.flush();

        assertFalse(repo.findById(id).isPresent());
    }
}
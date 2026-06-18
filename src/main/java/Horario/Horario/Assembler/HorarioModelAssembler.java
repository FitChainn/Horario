package Horario.Horario.Assembler;

import Horario.Horario.Controller.HorarioController;
import Horario.Horario.Dto.HorarioResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HorarioModelAssembler implements RepresentationModelAssembler<HorarioResponseDTO, EntityModel<HorarioResponseDTO>> {

    @Override
    public EntityModel<HorarioResponseDTO> toModel(HorarioResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(HorarioController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(HorarioController.class).obtenerTodos()).withRel("horarios"),
                linkTo(methodOn(HorarioController.class).obtenerPorEstablecimiento(dto.getEstablecimientoId())).withRel("horarios-por-establecimiento"),
                linkTo(methodOn(HorarioController.class).obtenerPorDia(dto.getDiaSemana())).withRel("horarios-por-dia")
        );
    }
}
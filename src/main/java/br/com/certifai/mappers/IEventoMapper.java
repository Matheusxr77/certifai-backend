package br.com.certifai.mappers;

import br.com.certifai.dto.EventoDTO;
import br.com.certifai.model.Evento;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IEventoMapper {
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "itens", ignore = true)
    Evento toEntity(EventoDTO dto);

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "itensIds", expression = "java(entity.getItens() != null ? entity.getItens().stream().map(c -> c.getId()).toList() : null)")
    EventoDTO toDTO(Evento entity);

    List<EventoDTO> toDtoList(List<Evento> eventos);
}

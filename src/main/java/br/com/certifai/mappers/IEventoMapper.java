package br.com.certifai.mappers;

import br.com.certifai.dto.EventoDTO;
import br.com.certifai.model.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IEventoMapper {
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "certificacoes", ignore = true)
    Evento toEntity(EventoDTO dto);

    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "certificacaoIds", expression = "java(entity.getCertificacoes() != null ? entity.getCertificacoes().stream().map(c -> c.getId()).toList() : null)")
    EventoDTO toDTO(Evento entity);
}

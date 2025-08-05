package br.com.certifai.mappers;

import br.com.certifai.dto.ProvaDTO;
import br.com.certifai.model.Prova;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IProvaMapper {
    @Mapping(target = "certificacao", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "resposta", ignore = true)
    Prova toEntity(ProvaDTO dto);

    @Mapping(target = "certificacaoId", source = "certificacao.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "respostaId", source = "resposta.id")
    ProvaDTO toDTO(Prova entity);
}

package br.com.certifai.mappers;

import br.com.certifai.dto.RespostaDTO;
import br.com.certifai.model.Resposta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IRespostaMapper {
    @Mapping(target = "alternativa", ignore = true)
    @Mapping(target = "prova", ignore = true)
    @Mapping(target = "questao", ignore = true)
    Resposta toEntity(RespostaDTO dto);

    @Mapping(target = "alternativaId", source = "alternativa.id")
    @Mapping(target = "provaId", source = "prova.id")
    @Mapping(target = "questaoId", source = "questao.id")
    RespostaDTO toDTO(Resposta entity);
}

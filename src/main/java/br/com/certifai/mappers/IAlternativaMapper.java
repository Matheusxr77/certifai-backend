package br.com.certifai.mappers;

import br.com.certifai.dto.AlternativaDTO;
import br.com.certifai.model.Alternativa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAlternativaMapper {
    @Mapping(target = "respostas", ignore = true)
    @Mapping(target = "questao", ignore = true)
    Alternativa toEntity(AlternativaDTO dto);

    @Mapping(target = "questaoId", source = "questao.id")
    AlternativaDTO toDTO(Alternativa entity);
}

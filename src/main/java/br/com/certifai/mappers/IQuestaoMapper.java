package br.com.certifai.mappers;

import br.com.certifai.dto.QuestaoDTO;
import br.com.certifai.model.Questao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IQuestaoMapper {
    @Mapping(target = "alternativas", ignore = true)
    @Mapping(target = "respostas", ignore = true)
    @Mapping(target = "certificacoes", ignore = true)
    Questao toEntity(QuestaoDTO dto);

    @Mapping(target = "alternativaIds", expression = "java(entity.getAlternativas() != null ? entity.getAlternativas().stream().map(a -> a.getId()).toList() : null)")
    @Mapping(target = "respostaIds", expression = "java(entity.getRespostas() != null ? entity.getRespostas().stream().map(r -> r.getId()).toList() : null)")
    @Mapping(target = "certificacaoIds", expression = "java(entity.getCertificacoes() != null ? entity.getCertificacoes().stream().map(c -> c.getId()).toList() : null)")
    QuestaoDTO toDTO(Questao entity);
}

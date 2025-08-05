package br.com.certifai.mappers;

import br.com.certifai.dto.CertificacaoDTO;
import br.com.certifai.model.Certificacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICertificacaoMapper {
    @Mapping(target = "checklists", ignore = true)
    @Mapping(target = "provas", ignore = true)
    @Mapping(target = "eventos", ignore = true)
    @Mapping(target = "questoes", ignore = true)
    Certificacao toEntity(CertificacaoDTO dto);

    @Mapping(target = "checklistIds", expression = "java(entity.getChecklists() != null ? entity.getChecklists().stream().map(c -> c.getId()).toList() : null)")
    @Mapping(target = "provaIds", expression = "java(entity.getProvas() != null ? entity.getProvas().stream().map(p -> p.getId()).toList() : null)")
    @Mapping(target = "eventoIds", expression = "java(entity.getEventos() != null ? entity.getEventos().stream().map(e -> e.getId()).toList() : null)")
    @Mapping(target = "questaoIds", expression = "java(entity.getQuestoes() != null ? entity.getQuestoes().stream().map(q -> q.getId()).toList() : null)")
    CertificacaoDTO toDTO(Certificacao entity);
}

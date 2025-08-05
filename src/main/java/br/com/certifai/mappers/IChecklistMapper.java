package br.com.certifai.mappers;

import br.com.certifai.dto.ChecklistDTO;
import br.com.certifai.model.Checklist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IChecklistMapper {
    @Mapping(target = "itensChecklist", ignore = true)
    @Mapping(target = "certificacao", ignore = true)
    Checklist toEntity(ChecklistDTO dto);

    @Mapping(target = "certificacaoId", source = "certificacao.id")
    @Mapping(target = "itemChecklistIds", expression = "java(entity.getItensChecklist() != null ? entity.getItensChecklist().stream().map(i -> i.getId()).toList() : null)")
    ChecklistDTO toDTO(Checklist entity);
}

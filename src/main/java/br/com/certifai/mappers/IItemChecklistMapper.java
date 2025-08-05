package br.com.certifai.mappers;

import br.com.certifai.dto.ItemChecklistDTO;
import br.com.certifai.model.ItemChecklist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IItemChecklistMapper {
    @Mapping(target = "checklist", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    ItemChecklist toEntity(ItemChecklistDTO dto);

    @Mapping(target = "checklistId", source = "checklist.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    ItemChecklistDTO toDTO(ItemChecklist entity);
}

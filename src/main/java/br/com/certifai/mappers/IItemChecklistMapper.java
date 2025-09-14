package br.com.certifai.mappers;

import br.com.certifai.dto.ItemChecklistDTO;
import br.com.certifai.model.ItemChecklist;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IItemChecklistMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true)
    ItemChecklist toEntity(ItemChecklistDTO dto);

    // @Mapping(target = "checklistId", source = "checklist.id")
    // @Mapping(target = "usuarioId", source = "usuario.id")
    // @Mapping(target = "eventoId", source = "evento.id") 
    ItemChecklistDTO toDTO(ItemChecklist entity);

    List<ItemChecklistDTO> toDtoList(List<ItemChecklist> itens);
}

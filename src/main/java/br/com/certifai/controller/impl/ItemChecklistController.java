package br.com.certifai.controller.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.certifai.controller.interfaces.ItemChecklistApi;
import br.com.certifai.dto.ChecklistDTO;
import br.com.certifai.dto.ItemChecklistDTO;
import br.com.certifai.dto.UsuarioDTO;
import br.com.certifai.mappers.IChecklistMapper;
import br.com.certifai.mappers.IItemChecklistMapper;
import br.com.certifai.model.ItemChecklist;
import br.com.certifai.response.AbstractResponse;
import br.com.certifai.service.impl.ChecklistService;
import br.com.certifai.service.impl.ItemChecklistService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemChecklistController implements ItemChecklistApi{

    private final ItemChecklistService itemChecklistService;
    private final IItemChecklistMapper mapper;

    @Override
    public ResponseEntity<AbstractResponse<ItemChecklistDTO>> criar(
        Long checklistId, 
        ItemChecklistDTO itemDTO
    ) {
     
        ItemChecklist item = mapper.toEntity(itemDTO);
        ItemChecklist criado = itemChecklistService.createItem(item);
        ItemChecklistDTO response = mapper.toDTO(criado);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(AbstractResponse.success(response, "Item criado com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<ItemChecklistDTO>> getById(
        Long checklistId,
        Long itemId
    ) {
        ItemChecklist item = itemChecklistService.getItemById(itemId);
        return ResponseEntity.ok(AbstractResponse.success(mapper.toDTO(item)));
    }

    @Override
    public ResponseEntity<AbstractResponse<List<ItemChecklistDTO>>> getAllByChecklist(Long checklistId) {
        List<ItemChecklistDTO> response = itemChecklistService.getItemsByChecklistId(checklistId).stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(AbstractResponse.success(response));
    }

    @Override
    public ResponseEntity<AbstractResponse<ItemChecklistDTO>> update(
        Long checklistId,
        Long itemId,
        ItemChecklistDTO itemDTO
    ) {
        ItemChecklist item = mapper.toEntity(itemDTO);
        ItemChecklist atualizado = itemChecklistService.updateItem(itemId, item);
        ItemChecklistDTO response = mapper.toDTO(atualizado);
        return ResponseEntity.ok(
            AbstractResponse.success(response, "Item atualizado com sucesso."));
    }

    @Override
    public ResponseEntity<Void> delete(Long checklistId, Long itemId) {
        itemChecklistService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{itemId}/evento/{eventoId}")
    public ResponseEntity<ItemChecklistDTO> vincularEvento(
            @PathVariable Long itemId,
            @PathVariable Long eventoId
    ) {
        ItemChecklist atualizado = itemChecklistService.vincularEvento(itemId, eventoId);
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{itemId}/evento")
    public ResponseEntity<ItemChecklistDTO> desvincularEvento(
            @PathVariable Long itemId
    ) {
        ItemChecklist atualizado = itemChecklistService.desvincularEvento(itemId);
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }
}

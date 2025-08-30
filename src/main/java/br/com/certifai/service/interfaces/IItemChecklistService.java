package br.com.certifai.service.interfaces;

import java.util.List;

import br.com.certifai.dto.ItemChecklistDTO;
import br.com.certifai.model.ItemChecklist;

public interface IItemChecklistService {
    ItemChecklist createItem(ItemChecklist item);
    ItemChecklist getItemById(Long id);
    List<ItemChecklist> getItemsByChecklistId(Long checklistId);
    ItemChecklist updateItem(Long id, ItemChecklist item);
    void deleteItem(Long id);
}

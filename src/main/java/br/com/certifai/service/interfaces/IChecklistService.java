package br.com.certifai.service.interfaces;

import java.util.List;
import java.util.Optional;

import br.com.certifai.dto.ChecklistDTO;
import br.com.certifai.model.Checklist;

public interface IChecklistService {
    Checklist createChecklist(Checklist checklist);
    Optional<Checklist> getChecklistById(Long id);
    List<Checklist> getAllChecklists();
    Checklist updateChecklist(Long id, Checklist checklist);
    void deleteChecklist(Long id);
}

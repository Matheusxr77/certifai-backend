package br.com.certifai.repository;

import br.com.certifai.model.ItemChecklist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemChecklistRepository extends JpaRepository<ItemChecklist, Long>{
    List<ItemChecklist> findByChecklistId(Long checklistId);
}

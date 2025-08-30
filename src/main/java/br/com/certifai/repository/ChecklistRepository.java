package br.com.certifai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.certifai.model.Checklist;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    
}

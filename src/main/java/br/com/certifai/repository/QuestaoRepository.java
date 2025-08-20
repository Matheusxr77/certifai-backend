package br.com.certifai.repository;

import br.com.certifai.model.Questao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestaoRepository extends JpaRepository<Questao, Long> {
    @Query(value = "SELECT * FROM questao WHERE dificuldade = :dificuldade ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Questao> findRandomQuestoesByDificuldade(
            @Param("dificuldade") String dificuldade,
            @Param("limit") int limit
    );
}

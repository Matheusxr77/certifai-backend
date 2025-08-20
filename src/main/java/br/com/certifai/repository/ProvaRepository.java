package br.com.certifai.repository;

import br.com.certifai.model.Prova;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvaRepository extends JpaRepository<Prova, Long> {
    List<Prova> findByCertificacaoId(Long id);
    List<Prova> findByUsuarioId(Long id);
}

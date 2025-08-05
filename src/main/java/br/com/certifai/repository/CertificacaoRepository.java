package br.com.certifai.repository;

import br.com.certifai.model.Certificacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificacaoRepository extends JpaRepository<Certificacao, Long> {
}

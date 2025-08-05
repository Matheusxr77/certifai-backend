package br.com.certifai.service.interfaces;

import br.com.certifai.model.Certificacao;
import java.util.List;
import java.util.Optional;

public interface ICertificacaoService {
    Certificacao create(Certificacao certificacao);
    List<Certificacao> findAll();
    Optional<Certificacao> findById(Long id);
    Certificacao update(Long id, Certificacao certificacaoDetails);
    void delete(Long id);
    Certificacao calcularEAtualizarTempoTotal(Long certificacaoId);
}

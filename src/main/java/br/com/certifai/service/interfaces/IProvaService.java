package br.com.certifai.service.interfaces;

import br.com.certifai.model.Prova;
import br.com.certifai.requests.MontarProvaRequest;

import java.util.List;
import java.util.Optional;

public interface IProvaService {
    Prova montarProvaPersonalizada(MontarProvaRequest request);
    Prova create(Prova prova);
    Optional<Prova> findById(Long id);
    List<Prova> findAll();
    List<Prova> findByUsuarioId(Long usuarioId);
    List<Prova> findByCertificacaoId(Long certificacaoId);
    Prova update(Long id, Prova provaDetails);
    void delete(Long id);
    Prova iniciarProva(Long provaId);
    Prova finalizarProva(Long provaId);
}

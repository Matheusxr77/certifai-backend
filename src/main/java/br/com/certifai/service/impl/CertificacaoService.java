package br.com.certifai.service.impl;

import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.model.Certificacao;
import br.com.certifai.model.Prova;
import br.com.certifai.repository.CertificacaoRepository;
import br.com.certifai.service.interfaces.ICertificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificacaoService implements ICertificacaoService {

    private final CertificacaoRepository certificacaoRepository;

    @Override
    @Transactional
    public Certificacao create(Certificacao certificacao) {
        certificacao.setId(null);
        return certificacaoRepository.save(certificacao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificacao> findAll() {
        return certificacaoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Certificacao> findById(Long id) {
        return certificacaoRepository.findById(id);
    }

    @Override
    @Transactional
    public Certificacao update(Long id, Certificacao certificacaoDetails) {
        Certificacao certificacaoExistente = certificacaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Certificação não encontrada com o ID: " + id));

        certificacaoExistente.setNome(certificacaoDetails.getNome());
        certificacaoExistente.setDescricao(certificacaoDetails.getDescricao());
        certificacaoExistente.setDificuldade(certificacaoDetails.getDificuldade());
        certificacaoExistente.setTempo(certificacaoDetails.getTempo());

        return certificacaoRepository.save(certificacaoExistente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!certificacaoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Certificação não encontrada com o ID: " + id);
        }
        certificacaoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Certificacao calcularEAtualizarTempoTotal(Long certificacaoId) {
        Certificacao certificacao = certificacaoRepository.findById(certificacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Certificação não encontrada com o ID: " + certificacaoId));

        Long tempoTotal = certificacao.getProvas().stream()
                .filter(prova -> prova.getTempo() != null)
                .mapToLong(Prova::getTempo)
                .sum();

        certificacao.setTempo(tempoTotal);
        return certificacaoRepository.save(certificacao);
    }
}

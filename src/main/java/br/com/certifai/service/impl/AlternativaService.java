package br.com.certifai.service.impl;

import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.model.Alternativa;
import br.com.certifai.repository.AlternativaRepository;
import br.com.certifai.service.interfaces.IAlternativaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlternativaService implements IAlternativaService {

    private final AlternativaRepository alternativaRepository;

    @Override
    @Transactional
    public Alternativa create(Alternativa alternativa) {
        alternativa.setId(null);
        return alternativaRepository.save(alternativa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alternativa> findAll() {
        return alternativaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Alternativa> findById(Long id) {
        return alternativaRepository.findById(id);
    }

    @Override
    @Transactional
    public Alternativa update(Long id, Alternativa alternativaDetails) {
        Alternativa alternativaExistente = alternativaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Alternativa com ID " + id + " não encontrada para atualização."));

        alternativaExistente.setTexto(alternativaDetails.getTexto());
        alternativaExistente.setCorreta(alternativaDetails.isCorreta());
        if (alternativaDetails.getQuestao() != null) {
            alternativaExistente.setQuestao(alternativaDetails.getQuestao());
        }

        return alternativaRepository.save(alternativaExistente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!alternativaRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Alternativa com ID " + id + " não encontrada para exclusão.");
        }
        alternativaRepository.deleteById(id);
    }
}

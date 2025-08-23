package br.com.certifai.controller.impl;

import br.com.certifai.controller.interfaces.AlternativaApi;
import br.com.certifai.dto.AlternativaDTO;
import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.mappers.IAlternativaMapper;
import br.com.certifai.model.Alternativa;
import br.com.certifai.model.Questao;
import br.com.certifai.repository.QuestaoRepository;
import br.com.certifai.response.AbstractResponse;
import br.com.certifai.service.interfaces.IAlternativaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AlternativaController implements AlternativaApi {

    private final IAlternativaService alternativaService;

    private final IAlternativaMapper alternativaMapper;

    private final QuestaoRepository questaoRepository;

    @Override
    public ResponseEntity<AbstractResponse<AlternativaDTO>> criar(AlternativaDTO alternativaDTO) {
        Questao questao = questaoRepository.findById(alternativaDTO.getQuestaoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Questão com ID " + alternativaDTO.getQuestaoId() + " não encontrada."));

        Alternativa alternativaParaCriar = alternativaMapper.toEntity(alternativaDTO);
        alternativaParaCriar.setQuestao(questao);

        Alternativa novaAlternativa = alternativaService.create(alternativaParaCriar);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AbstractResponse.success(alternativaMapper.toDTO(novaAlternativa), "Alternativa criada com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<List<AlternativaDTO>>> listarTodas() {
        List<Alternativa> alternativas = alternativaService.findAll();

        List<AlternativaDTO> dtos = alternativas.stream()
                .map(alternativaMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(AbstractResponse.success(dtos));
    }

    @Override
    public ResponseEntity<AbstractResponse<AlternativaDTO>> buscarPorId(Long id) {
        Alternativa alternativa = alternativaService.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Alternativa com ID " + id + " não encontrada."));

        return ResponseEntity.ok(AbstractResponse.success(alternativaMapper.toDTO(alternativa)));
    }

    @Override
    public ResponseEntity<AbstractResponse<AlternativaDTO>> atualizar(Long id, AlternativaDTO alternativaDTO) {
        Questao questao = questaoRepository.findById(alternativaDTO.getQuestaoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Questão com ID " + alternativaDTO.getQuestaoId() + " não encontrada."));

        Alternativa detalhesAlternativa = alternativaMapper.toEntity(alternativaDTO);
        detalhesAlternativa.setQuestao(questao);

        Alternativa alternativaAtualizada = alternativaService.update(id, detalhesAlternativa);

        return ResponseEntity.ok(AbstractResponse.success(alternativaMapper.toDTO(alternativaAtualizada), "Alternativa atualizada com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        alternativaService.delete(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Alternativa removida com sucesso."));
    }
}

package br.com.certifai.controller.impl;

import br.com.certifai.controller.interfaces.CertificacaoApi;
import br.com.certifai.dto.CertificacaoDTO;
import br.com.certifai.mappers.ICertificacaoMapper;
import br.com.certifai.model.Certificacao;
import br.com.certifai.response.AbstractResponse;
import br.com.certifai.service.interfaces.ICertificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CertificacaoController implements CertificacaoApi {

    private final ICertificacaoService certificacaoService;
    private final ICertificacaoMapper certificacaoMapper;

    @Override
    public ResponseEntity<AbstractResponse<CertificacaoDTO>> criar(CertificacaoDTO certificacaoDTO) {
        Certificacao certificacao = certificacaoMapper.toEntity(certificacaoDTO);
        Certificacao novaCertificacao = certificacaoService.create(certificacao);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AbstractResponse.success(certificacaoMapper.toDTO(novaCertificacao), "Certificação criada com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<List<CertificacaoDTO>>> listarTodas() {
        List<CertificacaoDTO> certificacoes = certificacaoService.findAll()
                .stream()
                .map(certificacaoMapper::toDTO)
                .toList();
        return ResponseEntity.ok(AbstractResponse.success(certificacoes));
    }

    @Override
    public ResponseEntity<AbstractResponse<CertificacaoDTO>> buscarPorId(Long id) {
        Certificacao certificacao = certificacaoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificação não encontrada"));
        return ResponseEntity.ok(AbstractResponse.success(certificacaoMapper.toDTO(certificacao)));
    }

    @Override
    public ResponseEntity<AbstractResponse<CertificacaoDTO>> atualizar(Long id, CertificacaoDTO certificacaoDTO) {
        Certificacao certificacao = certificacaoMapper.toEntity(certificacaoDTO);
        Certificacao certificacaoAtualizada = certificacaoService.update(id, certificacao);
        return ResponseEntity.ok(AbstractResponse.success(certificacaoMapper.toDTO(certificacaoAtualizada), "Certificação atualizada com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        certificacaoService.delete(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Certificação removida com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<CertificacaoDTO>> recalcularTempoTotal(Long id) {
        Certificacao certificacaoAtualizada = certificacaoService.calcularEAtualizarTempoTotal(id);
        return ResponseEntity.ok(AbstractResponse.success(certificacaoMapper.toDTO(certificacaoAtualizada), "Tempo total recalculado com sucesso."));
    }
}
package br.com.certifai.controller.impl;

import br.com.certifai.controller.interfaces.ProvaApi;
import br.com.certifai.dto.ProvaDTO;
import br.com.certifai.mappers.IProvaMapper;
import br.com.certifai.model.Prova;
import br.com.certifai.requests.MontarProvaRequest;
import br.com.certifai.response.AbstractResponse;
import br.com.certifai.service.interfaces.IProvaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProvaController implements ProvaApi {

    private final IProvaService provaService;
    private final IProvaMapper provaMapper;

    @Override
    public ResponseEntity<AbstractResponse<ProvaDTO>> montarProvaPersonalizada(MontarProvaRequest request) {
        Prova provaCriada = provaService.montarProvaPersonalizada(request);
        ProvaDTO provaDTO = provaMapper.toDTO(provaCriada);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AbstractResponse.success(provaDTO, "Prova montada com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<ProvaDTO>> buscarPorId(Long id) {
        Prova prova = provaService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prova não encontrada com o ID: " + id));
        return ResponseEntity.ok(AbstractResponse.success(provaMapper.toDTO(prova)));
    }

    @Override
    public ResponseEntity<AbstractResponse<List<ProvaDTO>>> listarTodas() {
        List<ProvaDTO> dtos = provaService.findAll().stream()
                .map(provaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(AbstractResponse.success(dtos));
    }

    @Override
    public ResponseEntity<AbstractResponse<List<ProvaDTO>>> listarPorUsuario(Long usuarioId) {
        List<ProvaDTO> dtos = provaService.findByUsuarioId(usuarioId).stream()
                .map(provaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(AbstractResponse.success(dtos));
    }

    @Override
    public ResponseEntity<AbstractResponse<ProvaDTO>> atualizar(Long id, ProvaDTO provaDTO) {
        Prova provaParaAtualizar = provaMapper.toEntity(provaDTO);
        Prova provaAtualizada = provaService.update(id, provaParaAtualizar);
        return ResponseEntity.ok(AbstractResponse.success(provaMapper.toDTO(provaAtualizada), "Prova atualizada com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> excluir(Long id) {
        provaService.delete(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Prova removida com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<ProvaDTO>> iniciar(Long id) {
        Prova provaIniciada = provaService.iniciarProva(id);
        return ResponseEntity.ok(AbstractResponse.success(provaMapper.toDTO(provaIniciada), "Prova iniciada com sucesso."));
    }

    @Override
    public ResponseEntity<AbstractResponse<ProvaDTO>> finalizar(Long id) {
        Prova provaFinalizada = provaService.finalizarProva(id);
        return ResponseEntity.ok(AbstractResponse.success(provaMapper.toDTO(provaFinalizada), "Prova finalizada com sucesso."));
    }
}

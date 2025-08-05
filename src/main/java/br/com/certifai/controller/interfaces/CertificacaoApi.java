package br.com.certifai.controller.interfaces;

import br.com.certifai.dto.CertificacaoDTO;
import br.com.certifai.response.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Certificações", description = "API para gerenciamento de Certificações")
@RequestMapping("/certificacoes")
public interface CertificacaoApi {

    @PostMapping
    @Operation(summary = "Criar uma nova certificação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Certificação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    ResponseEntity<AbstractResponse<CertificacaoDTO>> criar(@Valid @RequestBody CertificacaoDTO certificacaoDTO);

    @GetMapping
    @Operation(summary = "Listar todas as certificações")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de certificações retornada com sucesso")
    })
    ResponseEntity<AbstractResponse<List<CertificacaoDTO>>> listarTodas();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar certificação por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Certificação encontrada"),
            @ApiResponse(responseCode = "404", description = "Certificação não encontrada")
    })
    ResponseEntity<AbstractResponse<CertificacaoDTO>> buscarPorId(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma certificação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Certificação atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Certificação não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<AbstractResponse<CertificacaoDTO>> atualizar(@PathVariable("id") Long id, @Valid @RequestBody CertificacaoDTO certificacaoDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma certificação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Certificação removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Certificação não encontrada")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);

    @PostMapping("/{id}/calcular-tempo")
    @Operation(summary = "Recalcular o tempo total da certificação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tempo total recalculado e certificação atualizada"),
            @ApiResponse(responseCode = "404", description = "Certificação não encontrada")
    })
    ResponseEntity<AbstractResponse<CertificacaoDTO>> recalcularTempoTotal(@PathVariable("id") Long id);
}

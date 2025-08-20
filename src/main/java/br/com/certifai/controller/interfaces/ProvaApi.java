package br.com.certifai.controller.interfaces;

import br.com.certifai.dto.ProvaDTO;
import br.com.certifai.requests.MontarProvaRequest;
import br.com.certifai.response.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Provas", description = "API para gerenciamento de Provas")
@RequestMapping("/provas")
public interface ProvaApi {

    @PostMapping("/montar-personalizada")
    @Operation(summary = "Monta e cria uma nova prova personalizada")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prova montada e criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Usuário ou Certificação não encontrados")
    })
    ResponseEntity<AbstractResponse<ProvaDTO>> montarProvaPersonalizada(@Valid @RequestBody MontarProvaRequest request);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma prova por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prova encontrada"),
            @ApiResponse(responseCode = "404", description = "Prova não encontrada")
    })
    ResponseEntity<AbstractResponse<ProvaDTO>> buscarPorId(@PathVariable Long id);

    @GetMapping
    @Operation(summary = "Listar todas as provas (acesso de admin)")
    ResponseEntity<AbstractResponse<List<ProvaDTO>>> listarTodas();

    @GetMapping("/por-usuario/{usuarioId}")
    @Operation(summary = "Listar todas as provas de um usuário específico")
    ResponseEntity<AbstractResponse<List<ProvaDTO>>> listarPorUsuario(@PathVariable Long usuarioId);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de uma prova")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prova atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Prova não encontrada")
    })
    ResponseEntity<AbstractResponse<ProvaDTO>> atualizar(@PathVariable Long id, @RequestBody ProvaDTO provaDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma prova")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prova removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Prova não encontrada")
    })
    ResponseEntity<AbstractResponse<Void>> excluir(@PathVariable Long id);

    @PostMapping("/{id}/iniciar")
    @Operation(summary = "Inicia uma prova, mudando seu status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prova iniciada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Prova não encontrada"),
            @ApiResponse(responseCode = "409", description = "Prova já foi iniciada ou finalizada (Conflito)")
    })
    ResponseEntity<AbstractResponse<ProvaDTO>> iniciar(@PathVariable Long id);

    @PostMapping("/{id}/finalizar")
    @Operation(summary = "Finaliza uma prova, calcula e salva a pontuação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prova finalizada e pontuação calculada"),
            @ApiResponse(responseCode = "404", description = "Prova não encontrada"),
            @ApiResponse(responseCode = "409", description = "Prova não está em andamento (Conflito)")
    })
    ResponseEntity<AbstractResponse<ProvaDTO>> finalizar(@PathVariable Long id);
}

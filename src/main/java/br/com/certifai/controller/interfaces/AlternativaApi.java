package br.com.certifai.controller.interfaces;

import br.com.certifai.dto.AlternativaDTO;
import br.com.certifai.response.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Alternativas", description = "API para gerenciamento de Alternativas de Questões")
@RequestMapping("/alternativas")
public interface AlternativaApi {

    @PostMapping
    @Operation(summary = "Criar uma nova alternativa para uma questão")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Alternativa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Questão associada não encontrada")
    })
    ResponseEntity<AbstractResponse<AlternativaDTO>> criar(@Valid @RequestBody AlternativaDTO alternativaDTO);

    @GetMapping
    @Operation(summary = "Listar todas as alternativas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de alternativas retornada com sucesso")
    })
    ResponseEntity<AbstractResponse<List<AlternativaDTO>>> listarTodas();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alternativa por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alternativa encontrada"),
            @ApiResponse(responseCode = "404", description = "Alternativa não encontrada")
    })
    ResponseEntity<AbstractResponse<AlternativaDTO>> buscarPorId(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma alternativa existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alternativa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alternativa ou Questão não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<AbstractResponse<AlternativaDTO>> atualizar(@PathVariable("id") Long id, @Valid @RequestBody AlternativaDTO alternativaDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma alternativa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alternativa removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Alternativa não encontrada")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);
}

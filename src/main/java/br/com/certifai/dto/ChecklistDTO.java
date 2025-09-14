package br.com.certifai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class ChecklistDTO {
    private Long id;
    private String nome;
    private String descricao;

    @JsonProperty("certificacao_id")
    private Long certificacaoId; 
    private Long usuarioId;      
    private List<ItemChecklistDTO> itensChecklist;
}

package br.com.certifai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Long certificacaoId;
    private Long usuarioId;
    private List<Long> itemChecklistIds;
}

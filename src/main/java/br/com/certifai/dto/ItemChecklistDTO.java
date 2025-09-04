package br.com.certifai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemChecklistDTO {
    private Long id;
    private String descricao;
    private boolean concluido;
    private LocalDateTime conclusao;
    private Long checklistId;
    private Long usuarioId;
    private Long eventoId;
}

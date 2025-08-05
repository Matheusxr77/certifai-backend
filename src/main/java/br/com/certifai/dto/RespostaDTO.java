package br.com.certifai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespostaDTO {
    private Long id;
    private boolean acertou;
    private Long alternativaId;
    private Long provaId;
    private Long questaoId;
}

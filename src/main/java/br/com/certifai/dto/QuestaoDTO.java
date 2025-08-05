package br.com.certifai.dto;

import br.com.certifai.enums.Categorias;
import br.com.certifai.enums.Dificuldades;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestaoDTO {
    private Long id;
    private String enunciado;
    private Categorias categoria;
    private Dificuldades dificuldade;
    private List<Long> alternativaIds;
    private List<Long> respostaIds;
    private List<Long> certificacaoIds;
}

package br.com.certifai.dto;

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
public class CertificacaoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Dificuldades dificuldade;
    private Long tempo;
    private List<Long> checklistIds;
    private List<Long> provaIds;
    private List<Long> eventoIds;
    private List<Long> questaoIds;
}

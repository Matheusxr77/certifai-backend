package br.com.certifai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "acertou", nullable = false)
    private boolean acertou;

    @ManyToOne
    @JoinColumn(name = "resposta_alternativa")
    private Alternativa alternativa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prova_id", nullable = false)
    private Prova prova;

    @ManyToOne
    @JoinColumn(name = "questao_id")
    private Questao questao;
}

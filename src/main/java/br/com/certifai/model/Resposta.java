package br.com.certifai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "acertou", nullable = false)
    private boolean acertou;

    @ManyToOne
    @JoinColumn(name = "resposta_alternativa")
    private Alternativa alternativa;

    @ManyToOne
    @JoinColumn(name = "resposta_prova")
    private Prova prova;

    @ManyToOne
    @JoinColumn(name = "questao_id")
    private Questao questao;
}

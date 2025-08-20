package br.com.certifai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Alternativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Texto é obrigatório")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    @NotBlank(message = "Resposta correta é obrigatória")
    private boolean correta;

    @OneToMany(mappedBy = "alternativa",  cascade = CascadeType.ALL, orphanRemoval = true)
    List<Resposta> respostas;

    @ManyToOne
    @JoinColumn(name = "questao_id")
    private Questao questao;
}

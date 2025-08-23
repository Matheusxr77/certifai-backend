package br.com.certifai.model;

import br.com.certifai.enums.Categorias;
import br.com.certifai.enums.Dificuldades;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Enunciado é obrigatório")
    @Column(nullable = false,  columnDefinition = "TEXT")
    private String enunciado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categorias categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Dificuldades dificuldade;

    @OneToMany(mappedBy = "questao",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alternativa> alternativas;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas;

    @ManyToMany(mappedBy = "questoes")
    private List<Certificacao> certificacoes;
}

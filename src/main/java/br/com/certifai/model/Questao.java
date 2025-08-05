package br.com.certifai.model;

import br.com.certifai.enums.Categorias;
import br.com.certifai.enums.Dificuldades;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Enunciado é obrigatório")
    @Size(min = 3, max = 100, message = "Enunciado deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
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

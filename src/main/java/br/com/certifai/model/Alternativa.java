package br.com.certifai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 100, message = "Texto deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String texto;

    @NotBlank(message = "Resposta correta é obrigatório")
    @Size(min = 1, max = 100, message = "Resposta correta deve ter entre 1 e 100 caracteres")
    @Column(nullable = false)
    private String correta;

    @OneToMany(mappedBy = "alternativa",  cascade = CascadeType.ALL, orphanRemoval = true)
    List<Resposta> respostas;

    @ManyToOne
    @JoinColumn(name = "questao_id")
    private Questao questao;
}

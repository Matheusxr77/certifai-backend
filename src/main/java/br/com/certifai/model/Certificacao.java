package br.com.certifai.model;

import br.com.certifai.enums.Dificuldades;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Certificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Dificuldades dificuldade;

    @Column(name = "tempo")
    private Long tempo;

    @OneToMany(mappedBy = "certificacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checklist> checklists;

    @OneToMany(mappedBy = "certificacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prova> provas;

    @ManyToMany
    @JoinTable(name = "certificacao_evento", joinColumns = @JoinColumn(name = "certificacao_id"), inverseJoinColumns = @JoinColumn(name = "evento_id"))
    private List<Evento> eventos;

    @ManyToMany
    @JoinTable(name = "certificacao_questao", joinColumns = @JoinColumn(name = "certificacao_id"), inverseJoinColumns = @JoinColumn(name = "questao_id"))
    private List<Questao> questoes;
}

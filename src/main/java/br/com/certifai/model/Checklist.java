package br.com.certifai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 100, message = "Título deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @OneToMany(mappedBy = "checklist",  cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<ItemChecklist> itensChecklist;

    @ManyToOne
    @JoinColumn(name = "certificacao_id")
    private Certificacao certificacao;
}

package br.com.certifai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
public class ItemChecklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "concluido", nullable = false)
    private boolean concluido = false;

    @NotNull(message = "O campo conclusão não pode ser nulo")
    @Column(nullable = false)
    private LocalDateTime conclusao;

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;
}

package br.com.certifai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 100, message = "Título deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull(message = "O campo tempo não pode ser nulo")
    @Column(nullable = false)
    private LocalDateTime inicio;

    @NotNull(message = "O campo tempo não pode ser nulo")
    @Column(nullable = false)
    private LocalDateTime fim;

    @ManyToMany(mappedBy = "eventos")
    private List<Certificacao> certificacoes;
}

package br.com.certifai.dto;

import br.com.certifai.enums.Roles;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioDTO {
    private Long id;
    private String name;
    private String email;
    private Roles role;
    private boolean ativo;
    private LocalDateTime tokenExpiration;
}

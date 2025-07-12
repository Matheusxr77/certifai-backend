package br.com.certifai.requests;

import jakarta.validation.constraints.NotBlank;

public record RecuperarSenhaRequest (String token,
                                    @NotBlank(message = "A nova senha não pode estar em branco") String novaSenha,
                                    @NotBlank(message = "A confirmação da nova senha não pode estar em branco") String confirmarNovaSenha){
}

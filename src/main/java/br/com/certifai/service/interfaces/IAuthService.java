package br.com.certifai.service.interfaces;

import br.com.certifai.model.Usuario;
import br.com.certifai.requests.RecuperarSenhaRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IAuthService extends UserDetailsService {
    Usuario createUser(Usuario user);
    Optional<Usuario> getUsuarioByEmail(String email);
    String gerarToken(Usuario usuario);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    List<GrantedAuthority> getAuthoritiesFromToken(String token);
    Optional<Usuario> getPrincipal();
    boolean verifyEmail(String email);
    void iniciarRecuperacaoSenha(String email);
    void invalidateToken(String token);
    boolean isTokenInvalid(String token);
    void resetarSenha(RecuperarSenhaRequest novaSenhaRequest);
    boolean isResetTokenValid(String token);
}

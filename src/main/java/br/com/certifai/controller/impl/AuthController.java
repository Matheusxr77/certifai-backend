package br.com.certifai.controller.impl;

import br.com.certifai.controller.interfaces.AuthApi;
import br.com.certifai.dto.LoginDTO;
import br.com.certifai.dto.UsuarioDTO;
import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.mappers.IUsuarioMapper;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.UsuarioRepository;
import br.com.certifai.requests.RecuperarSenhaRequest;
import br.com.certifai.response.AbstractResponse;
import br.com.certifai.service.interfaces.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final IAuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final IUsuarioMapper usuarioMapper;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<AbstractResponse<String>> testAdminAccess() {
        return ResponseEntity.ok(AbstractResponse.success("Admin access granted!"));
    }

    @Override
    public ResponseEntity<AbstractResponse<UsuarioDTO>> registerUser(Usuario user) {
        Usuario usuarioCriado = authService.createUser(user);
        UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuarioCriado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AbstractResponse.success(usuarioDTO, "Usuário criado com sucesso"));
    }

    @Override
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        if (authService.verifyEmail(token)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("https://certifai-front-ruby.vercel.app/verification-success"));
            return ResponseEntity.ok("Seu e-mail foi verificado com sucesso! Você já pode fazer login.");
        } else {
            return ResponseEntity.badRequest().body("Token de verificação inválido ou expirado.");
        }
    }

    @Override
    public ResponseEntity<AbstractResponse<LoginDTO>> loginUser(Usuario usuario) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getPassword())
        );

        Optional<Usuario> usuarioOpt = authService.getUsuarioByEmail(usuario.getEmail());

        return usuarioOpt.map(usuarioAutenticado -> {
            UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuarioAutenticado);
            if (!usuarioAutenticado.isEmailVerified()) {
                LoginDTO loginDTO = new LoginDTO(usuarioDTO, null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AbstractResponse<LoginDTO>(false, "E-mail não verificado, por favor, consulte sua caixa de entrada ou spam.", "401", loginDTO));
            }
            if (!usuarioAutenticado.getAtivo()) {
                usuarioAutenticado.setAtivo(true);
                usuarioRepository.save(usuarioAutenticado);
            }
            String token = authService.gerarToken(usuarioAutenticado);
            LoginDTO login = new LoginDTO(usuarioDTO, token);
            return ResponseEntity.ok(AbstractResponse.success(login, "Login realizado com sucesso"));
        }).orElseGet(() ->
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AbstractResponse.error("Usuário não encontrado após autenticação", "USER_NOT_FOUND"))
        );
    }

    @Override
    public ResponseEntity<AbstractResponse<String>> esqueceuSenha(@RequestParam String email) {
        try {
            authService.iniciarRecuperacaoSenha(email);
            return ResponseEntity.ok(AbstractResponse.success("E-mail de redefinição de senha enviado com sucesso."));
        } catch (EntidadeNaoEncontradaException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AbstractResponse.error("Usuário com este e-mail não foi encontrado.", "NOT_FOUND"));
        }
    }

    @Override
    public ResponseEntity<AbstractResponse<UsuarioDTO>> getUsuarioLogado() {
        return authService.getPrincipal()
                .map(usuario -> {
                    UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuario);
                    return ResponseEntity.ok(AbstractResponse.success(usuarioDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AbstractResponse.error("Usuário não autenticado ou token inválido", "UNAUTHORIZED")));
    }

    @Override
    public ResponseEntity<AbstractResponse<String>> resetarSenha(@RequestBody RecuperarSenhaRequest novaSenhaRequest) {
        try {
            authService.resetarSenha(novaSenhaRequest);
            return ResponseEntity.ok(AbstractResponse.success("Senha redefinida com sucesso."));
        } catch (EntidadeNaoEncontradaException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AbstractResponse.error("Usuário não encontrado para o token informado.", "NOT_FOUND"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AbstractResponse.error(ex.getMessage(), "INVALID_TOKEN"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AbstractResponse.error("Erro interno ao redefinir a senha.", "INTERNAL_ERROR"));
        }
    }

    @Override
    public ResponseEntity<AbstractResponse<String>> validateResetToken(@RequestParam String token) {
        try {
            boolean valido = authService.isResetTokenValid(token);
            if (valido) {
                return ResponseEntity.ok(AbstractResponse.success("Token válido"));
            } else {
                return ResponseEntity.badRequest().body(AbstractResponse.error("Token inválido ou expirado", "TOKEN_INVALID"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(AbstractResponse.error("Token inválido ou expirado", "TOKEN_INVALID"));
        }
    }
}

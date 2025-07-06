package br.com.certifai.controller.impl;

import br.com.certifai.controller.interfaces.AuthApi;
import br.com.certifai.dto.LoginDTO;
import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.UsuarioRepository;
import br.com.certifai.response.AbstractResponse;
import br.com.certifai.service.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final IAuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ResponseEntity<AbstractResponse<String>> testAdminAccess() {
        return ResponseEntity.ok(AbstractResponse.success("Admin access granted!"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Usuario>> registerUser(Usuario user) {
        Usuario usuarioCriado = authService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AbstractResponse.success(usuarioCriado, "Usuário criado com sucesso"));
    }

    @Override
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        if (authService.verifyEmail(token)) {
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
            Usuario userRetorno = new Usuario();
            userRetorno.setEmail(usuarioAutenticado.getEmail());
            userRetorno.setName(usuarioAutenticado.getName());
            if (!usuarioAutenticado.isEmailVerified()) {
                LoginDTO loginDTO = new LoginDTO(userRetorno, null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AbstractResponse<LoginDTO>(false, "E-mail não verificado, por favor, consulte sua caixa de entrada ou spam.", "401", loginDTO));
            }
            if (!usuarioAutenticado.getAtivo()) {
                usuarioAutenticado.setAtivo(true);
                usuarioRepository.save(usuarioAutenticado);
            }
            String token = authService.gerarToken(usuarioAutenticado);
            LoginDTO login = new LoginDTO(userRetorno, token);
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
}

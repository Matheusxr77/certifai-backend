package br.com.certifai.service.impl;

import br.com.certifai.enums.Roles;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.UsuarioRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Primary
public class CustomOAuth2UserService extends OidcUserService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();

        usuarioRepository.findByEmail(email).ifPresentOrElse(user -> {
        }, () -> {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setName(name);
            novoUsuario.setEmail(email);
            novoUsuario.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            novoUsuario.setRole(Roles.ESTUDANTE);
            novoUsuario.setEmailVerified(true);
            usuarioRepository.save(novoUsuario);
        });

        return oidcUser;
    }
}

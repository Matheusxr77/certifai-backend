package br.com.certifai.service.impl;

import br.com.certifai.enums.Roles;
import br.com.certifai.exception.ConflitoException;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.UsuarioRepository;
import br.com.certifai.service.interfaces.IAuthService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${jwt.secret}")
    private String secretKey;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> user = usuarioRepository.findByEmail(username);
        if (user.isPresent()) {
            Usuario usuario = user.get();
            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getPassword())
                    .roles(getRoles(usuario))
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }
    }


    private String getRoles(Usuario user) {
        if (user == null) {
            throw new ConflitoException("É necessário escolher um perfil de usuário");
        }

        if (user.getRole() == null) {
            return Roles.ESTUDANTE.toString();
        }

        return user.getRole().toString();
    }

    @Override
    public Usuario createUser(Usuario user) {
        if (usuarioRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflitoException("Usuário já existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false);
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setTokenExpiresAt(LocalDateTime.now().plusHours(24));
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationToken);
        return usuarioRepository.save(user);
    }

    @Override
    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public String gerarToken(Usuario usuario){
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário inválido para gerar token");
        }
        return JWT.create()
                .withSubject(usuario.getEmail())
                .withClaim("roles", List.of(getRoles(usuario)))
                .withExpiresAt(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00")))
                .sign(getAlgorithm());
    }

    @Override
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    private DecodedJWT decodeToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        if (validateToken(token)) {
            DecodedJWT decodedJWT = decodeToken(token);
            return decodedJWT != null ? decodedJWT.getSubject() : null;
        }
        return null;
    }

    @Override
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        if (validateToken(token)) {
            DecodedJWT decodedJWT = decodeToken(token);
            if (decodedJWT != null) {
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                if (roles == null) {
                    return Collections.emptyList();
                }
                return roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Usuario> getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return Optional.empty();
        }
        return getUsuarioByEmail(auth.getName());
    }

    public boolean verifyEmail(String token) {
        Optional<Usuario> userOptional = usuarioRepository.findByVerificationToken(token);

        if (userOptional.isEmpty()) {
            Optional<Usuario> userByEmailVerified = usuarioRepository.findAll().stream()
                    .filter(Usuario::isEmailVerified)
                    .findAny();

            return userByEmailVerified.isPresent();
        }

        Usuario user = userOptional.get();

        if (user.isEmailVerified()) {
            return true;
        }

        if (user.getTokenExpiresAt() != null && user.getTokenExpiresAt().isBefore(LocalDateTime.now())) {
            System.out.println("Token de verificação expirado para o usuário: " + user.getEmail());
            user.setEmailVerified(false);
            return true;
        } else {
            user.setEmailVerified(true);
        }

        user.setVerificationToken(null);
        user.setTokenExpiresAt(null);
        usuarioRepository.save(user);

        System.out.println("Email verificado com sucesso para: " + user.getEmail());
        return true;
    }

    @Override
    public void iniciarRecuperacaoSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String resetToken = UUID.randomUUID().toString();
        usuario.setResetToken(resetToken);
        usuario.setResetTokenExpiresAt(LocalDateTime.now().plusHours(1));

        usuarioRepository.save(usuario);
        emailService.sendPasswordResetEmail(usuario.getEmail(), usuario.getName(), resetToken);
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}

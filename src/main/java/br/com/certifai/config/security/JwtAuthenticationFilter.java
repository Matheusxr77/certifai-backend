package br.com.certifai.config.security;

import br.com.certifai.enums.Endpoints;
import br.com.certifai.model.Usuario;
import br.com.certifai.service.interfaces.IAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String COOKIE_NAME = "auth_token";
    private final IAuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        try {

            extractTokenFromRequest(request).ifPresentOrElse(token -> {

                if (authService.isTokenInvalid(token)) {
                    log.warn("Token inválido ou revogado para usuário: {}", authService.getUsernameFromToken(token));
                    return;
                }

                if (authService.validateToken(token)) {
                    String email = authService.getUsernameFromToken(token);
                    Optional<Usuario> usuario = authService.getUsuarioByEmail(email);
                    if (usuario.isPresent()) {
                        authenticateUser(request, usuario, token);
                    } else {
                    }
                } else {
                }
            }, () -> log.warn("Nenhum token encontrado na requisição"));

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Erro de autenticação: ", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication");
        }
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Optional<String> tokenFromCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .filter(t -> !t.isBlank())
                    .findFirst();
            if (tokenFromCookie.isPresent()) {
                return tokenFromCookie;
            }
        }

        Optional<String> token = Optional.ofNullable(request.getHeader(AUTH_HEADER))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .filter(t -> !t.isBlank());

        return token;
    }

    private void authenticateUser(HttpServletRequest request,
                                  Optional<Usuario> usuario,
                                  String token) {
        usuario.ifPresent(u -> {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            u,
                            null,
                            authService.getAuthoritiesFromToken(token)
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String[] publicEndpoints = Endpoints.getPublicEndpoints();
        AntPathMatcher matcher = new AntPathMatcher();

        for (String pattern : publicEndpoints) {
            if (matcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}

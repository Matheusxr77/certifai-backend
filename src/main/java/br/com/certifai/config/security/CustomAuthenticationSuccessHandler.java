package br.com.certifai.config.security;

import br.com.certifai.enums.Roles;
import br.com.certifai.model.Usuario;
import br.com.certifai.service.interfaces.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final IAuthService authService;

    @Autowired
    public CustomAuthenticationSuccessHandler(IAuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Usuario> usuario = authService.getUsuarioByEmail(userDetails.getUsername());

        if (usuario.isEmpty() || usuario.get().getRole() == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not properly configured");
            return;
        }

        try {
            Roles role = Roles.fromAuthority(usuario.get().getRole().toString());
            response.sendRedirect(role.getRedirectUrl());
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid role: " + usuario.get().getRole());
        }
    }
}

package br.com.certifai.controller.impl;

import br.com.certifai.controller.interfaces.DashboardApi;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class DashboardController implements DashboardApi {

    @Override
    public String dashboard(OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
        } else {
            model.addAttribute("name", "Usuário não autenticado");
            model.addAttribute("email", "");
        }
        return "dashboard";
    }
}

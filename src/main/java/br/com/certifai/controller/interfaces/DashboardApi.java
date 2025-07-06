package br.com.certifai.controller.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/dashboard")
public interface DashboardApi {

    @GetMapping
    @Operation(summary = "Exibe a página do dashboard com dados do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard carregado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    String dashboard(OAuth2User principal, Model model);

}

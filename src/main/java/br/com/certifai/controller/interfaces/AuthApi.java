package br.com.certifai.controller.interfaces;

import br.com.certifai.dto.LoginDTO;
import br.com.certifai.dto.UsuarioDTO;
import br.com.certifai.model.Usuario;
import br.com.certifai.response.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
public interface AuthApi {

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Endpoint para teste de acesso admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Acesso permitido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<String>> testAdminAccess();

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    ResponseEntity<AbstractResponse<UsuarioDTO>> registerUser(@RequestBody Usuario user);

    @GetMapping("/verify")
    @Operation(summary = "Verificar e-mail do usuário",
            description = "Confirma o e-mail de um novo usuário através de um token de verificação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "E-mail verificado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Token de verificação inválido ou expirado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    ResponseEntity<String> verifyEmail(
            @Parameter(description = "Token de verificação enviado para o e-mail do usuário", required = true)
            @RequestParam String token
    );

    @PostMapping("/login")
    @Operation(summary = "Login do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    ResponseEntity<AbstractResponse<LoginDTO>> loginUser(@RequestBody Usuario usuario);

    @Operation(summary = "Solicitar redefinição de senha",
            description = "Envia um e-mail com o link para redefinir a senha.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "E-mail de redefinição enviado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário com o e-mail informado não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao enviar e-mail")
    })
    @PostMapping("/esqueci-senha")
    ResponseEntity<AbstractResponse<String>> esqueceuSenha(
            @Parameter(description = "E-mail do usuário que esqueceu a senha", required = true)
            @RequestParam String email
    );
}

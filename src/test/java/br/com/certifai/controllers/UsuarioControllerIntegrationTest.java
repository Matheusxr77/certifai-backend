package br.com.certifai.controllers;

import br.com.certifai.enums.Roles;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.UsuarioRepository;
import br.com.certifai.requests.NovaSenhaRequest;
import br.com.certifai.service.interfaces.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        usuario = new Usuario();
        usuario.setName("Ana Costa");
        usuario.setEmail("ana.costa@email.com");
        usuario.setPassword(passwordEncoder.encode("senha123"));
        usuario.setRole(Roles.ADMIN);
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        adminToken = "Bearer " + authService.gerarToken(usuario);
    }

    @Test
    void deveListarUsuariosComSucesso() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].email").value(usuario.getEmail()));
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        mockMvc.perform(get("/usuarios/{id}", usuario.getId())
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(usuario.getEmail()));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        usuario.setName("Ana Costa Silva");

        String usuarioJson = objectMapper.writeValueAsString(usuario);

        mockMvc.perform(put("/usuarios/{id}", usuario.getId())
                        .with(csrf())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuário atualizado com sucesso"))
                .andExpect(jsonPath("$.data.name").value("Ana Costa Silva"));
    }

    @Test
    void deveAlterarSenhaComSucesso() throws Exception {
        NovaSenhaRequest novaSenhaRequest = new NovaSenhaRequest("novaSenhaSuperForte123");
        String json = objectMapper.writeValueAsString(novaSenhaRequest);

        mockMvc.perform(patch("/usuarios/{id}/senha", usuario.getId())
                        .with(csrf())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Senha alterada com sucesso"));
    }

    @Test
    void deveRemoverUsuarioComSucesso() throws Exception {
        mockMvc.perform(delete("/usuarios/{id}", usuario.getId())
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuário removido com sucesso"));
    }
}

package br.com.certifai.controllers;

import br.com.certifai.dto.UsuarioDTO;
import br.com.certifai.enums.Roles;
import br.com.certifai.mappers.IUsuarioMapper;
import br.com.certifai.model.Usuario;
import br.com.certifai.requests.NovaSenhaRequest;
import br.com.certifai.service.interfaces.IUsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(UsuarioControllerIntegrationTest.TestConfig.class)
class UsuarioControllerIntegrationTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public IUsuarioService usuarioService() {
            return Mockito.mock(IUsuarioService.class);
        }

        @Bean
        public IUsuarioMapper usuarioMapper() {
            return Mockito.mock(IUsuarioMapper.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IUsuarioMapper usuarioMapper;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .name("Ana Costa")
                .email("ana.costa@email.com")
                .password("senhaCriptografada")
                .role(Roles.ADMIN)
                .ativo(true)
                .build();

        usuarioDTO = UsuarioDTO.builder()
                .id(1L)
                .name("Ana Costa")
                .email("ana.costa@email.com")
                .role(Roles.ADMIN)
                .ativo(true)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os usuários com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosOsUsuariosComSucesso() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(Collections.singletonList(usuario));
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Ana Costa")));
    }

    @Test
    @DisplayName("Deve buscar um usuário por ID com sucesso")
    @WithMockUser
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        mockMvc.perform(get("/usuarios/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.email", is("ana.costa@email.com")));
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCriarUsuarioComSucesso() throws Exception {
        when(usuarioMapper.toEntity(any(UsuarioDTO.class))).thenReturn(usuario);
        when(usuarioService.criar(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/usuarios/1"))
                .andExpect(jsonPath("$.message", is("Usuário criado com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar um usuário com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarUsuarioComSucesso() throws Exception {
        usuarioDTO.setName("Ana Costa Silva");
        usuario.setName("Ana Costa Silva");

        when(usuarioMapper.toEntity(any(UsuarioDTO.class))).thenReturn(usuario);
        when(usuarioService.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        mockMvc.perform(put("/usuarios/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Ana Costa Silva")))
                .andExpect(jsonPath("$.message", is("Usuário atualizado com sucesso")));
    }

    @Test
    @DisplayName("Deve alterar a senha de um usuário com sucesso")
    @WithMockUser
    void deveAlterarSenhaComSucesso() throws Exception {
        NovaSenhaRequest novaSenhaRequest = new NovaSenhaRequest("novaSenhaSuperForte123");
        doNothing().when(usuarioService).alterarSenha(1L, "novaSenhaSuperForte123");

        mockMvc.perform(patch("/usuarios/{id}/senha", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaSenhaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Senha alterada com sucesso")));
    }

    @Test
    @DisplayName("Deve remover um usuário com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverUsuarioComSucesso() throws Exception {
        doNothing().when(usuarioService).remover(1L);

        mockMvc.perform(delete("/usuarios/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Usuário removido com sucesso")));
    }
}

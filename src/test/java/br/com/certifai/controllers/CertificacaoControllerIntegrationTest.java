package br.com.certifai.controllers;

import br.com.certifai.dto.CertificacaoDTO;
import br.com.certifai.enums.Dificuldades;
import br.com.certifai.enums.Roles;
import br.com.certifai.model.Certificacao;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.CertificacaoRepository;
import br.com.certifai.repository.ProvaRepository;
import br.com.certifai.repository.UsuarioRepository;
import br.com.certifai.service.interfaces.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
class CertificacaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CertificacaoRepository certificacaoRepository;

    @Autowired
    private ProvaRepository provaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAuthService authService;

    private String adminToken;
    private Certificacao certificacao;

    @BeforeEach
    void setUp() {
        provaRepository.deleteAll();
        certificacaoRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario admin = new Usuario();
        admin.setName("Admin Teste");
        admin.setEmail("admin.teste@email.com");
        admin.setPassword(passwordEncoder.encode("senha123"));
        admin.setRole(Roles.ADMIN);
        admin.setAtivo(true);
        usuarioRepository.save(admin);
        adminToken = "Bearer " + authService.gerarToken(admin);

        certificacao = Certificacao.builder()
                .nome("Fundamentos de Kubernetes")
                .descricao("Aprenda a orquestrar containers.")
                .dificuldade(Dificuldades.INTERMEDIARIO)
                .build();
        certificacaoRepository.save(certificacao);
    }

    @Test
    @DisplayName("Deve criar uma certificação com sucesso")
    void deveCriarCertificacaoComSucesso() throws Exception {
        CertificacaoDTO novaCertificacaoDTO = CertificacaoDTO.builder()
                .nome("Nova Certificação em Go")
                .descricao("Linguagem Go para backend.")
                .dificuldade(Dificuldades.BASICO)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(novaCertificacaoDTO);

        mockMvc.perform(post("/certificacoes")
                        .with(csrf())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nome").value("Nova Certificação em Go"));
    }

    @Test
    @DisplayName("Deve listar todas as certificações")
    void deveListarTodasAsCertificacoes() throws Exception {
        mockMvc.perform(get("/certificacoes")
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].nome").value("Fundamentos de Kubernetes"));
    }

    @Test
    @DisplayName("Deve buscar certificação por ID com sucesso")
    void deveBuscarCertificacaoPorIdComSucesso() throws Exception {
        mockMvc.perform(get("/certificacoes/{id}", certificacao.getId())
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(certificacao.getId()))
                .andExpect(jsonPath("$.data.nome").value(certificacao.getNome()));
    }

    @Test
    @DisplayName("Deve atualizar uma certificação com sucesso")
    void deveAtualizarCertificacaoComSucesso() throws Exception {
        CertificacaoDTO certificacaoAtualizadaDTO = CertificacaoDTO.builder()
                .nome("Fundamentos de Kubernetes Avançado")
                .descricao(certificacao.getDescricao())
                .dificuldade(Dificuldades.AVANCADO)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(certificacaoAtualizadaDTO);

        mockMvc.perform(put("/certificacoes/{id}", certificacao.getId())
                        .with(csrf())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Certificação atualizada com sucesso."))
                .andExpect(jsonPath("$.data.nome").value("Fundamentos de Kubernetes Avançado"))
                .andExpect(jsonPath("$.data.dificuldade").value("AVANCADO"));
    }

    @Test
    @DisplayName("Deve remover uma certificação com sucesso")
    void deveRemoverCertificacaoComSucesso() throws Exception {
        mockMvc.perform(delete("/certificacoes/{id}", certificacao.getId())
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Certificação removida com sucesso."));
    }
}

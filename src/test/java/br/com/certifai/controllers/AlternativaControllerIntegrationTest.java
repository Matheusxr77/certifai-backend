package br.com.certifai.controllers;

import br.com.certifai.dto.AlternativaDTO;
import br.com.certifai.enums.Categorias;
import br.com.certifai.enums.Dificuldades;
import br.com.certifai.enums.Roles;
import br.com.certifai.model.Alternativa;
import br.com.certifai.model.Questao;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.AlternativaRepository;
import br.com.certifai.repository.QuestaoRepository;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Boa prática para garantir que os testes sejam independentes
class AlternativaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlternativaRepository alternativaRepository;

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAuthService authService;

    private String adminToken;
    private Questao questaoSalva;
    private Alternativa alternativaSalva;

    @BeforeEach
    void setUp() {
        alternativaRepository.deleteAll();
        questaoRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario admin = new Usuario();
        admin.setName("Admin Teste Alternativa");
        admin.setEmail("admin.alternativa@email.com");
        admin.setPassword(passwordEncoder.encode("senha123"));
        admin.setRole(Roles.ADMIN);
        admin.setAtivo(true);
        usuarioRepository.save(admin);
        adminToken = "Bearer " + authService.gerarToken(admin);

        Questao questao = Questao.builder().enunciado("No modelo relacional, a chave primária de uma relação deve:").categoria(Categorias.BANCO).dificuldade(Dificuldades.BASICO).build();
        questaoSalva = questaoRepository.save(questao);

        alternativaSalva = Alternativa.builder()
                .texto("Identificar unicamente cada tupla da relação.")
                .correta(true)
                .questao(questaoSalva)
                .build();
        alternativaRepository.save(alternativaSalva);
    }

    @Test
    @DisplayName("Deve criar uma alternativa com sucesso")
    void deveCriarAlternativaComSucesso() throws Exception {
        AlternativaDTO novaAlternativaDTO = AlternativaDTO.builder()
                .texto("Permitir valores nulos, desde que não sejam repetidos.")
                .correta(false)
                .questaoId(questaoSalva.getId())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(novaAlternativaDTO);

        mockMvc.perform(post("/alternativas")
                        .with(csrf())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.texto").value("Permitir valores nulos, desde que não sejam repetidos."));
    }

    @Test
    @DisplayName("Deve listar todas as alternativas")
    void deveListarTodasAsAlternativas() throws Exception {
        mockMvc.perform(get("/alternativas")
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].texto").value("Identificar unicamente cada tupla da relação."));
    }

    @Test
    @DisplayName("Deve buscar alternativa por ID com sucesso")
    void deveBuscarAlternativaPorIdComSucesso() throws Exception {
        mockMvc.perform(get("/alternativas/{id}", alternativaSalva.getId())
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(alternativaSalva.getId()))
                .andExpect(jsonPath("$.data.texto").value(alternativaSalva.getTexto()));
    }

    @Test
    @DisplayName("Deve atualizar uma alternativa com sucesso")
    void deveAtualizarAlternativaComSucesso() throws Exception {
        AlternativaDTO alternativaAtualizadaDTO = AlternativaDTO.builder()
                .texto("Ser sempre composta por mais de um atributo.")
                .correta(false)
                .questaoId(questaoSalva.getId())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(alternativaAtualizadaDTO);

        mockMvc.perform(put("/alternativas/{id}", alternativaSalva.getId())
                        .with(csrf())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Alternativa atualizada com sucesso."))
                .andExpect(jsonPath("$.data.texto").value("Ser sempre composta por mais de um atributo."));
    }

    @Test
    @DisplayName("Deve remover uma alternativa com sucesso")
    void deveRemoverAlternativaComSucesso() throws Exception {
        mockMvc.perform(delete("/alternativas/{id}", alternativaSalva.getId())
                        .with(csrf())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Alternativa removida com sucesso."));
    }
}

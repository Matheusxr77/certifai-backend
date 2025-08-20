package br.com.certifai.controller.impl;

import br.com.certifai.enums.Dificuldade;
import br.com.certifai.enums.Status;
import br.com.certifai.model.Certificacao;
import br.com.certifai.model.Prova;
import br.com.certifai.model.Questao;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.CertificacaoRepository;
import br.com.certifai.repository.ProvaRepository;
import br.com.certifai.repository.QuestaoRepository;
import br.com.certifai.repository.UsuarioRepository;
import br.com.certifai.requests.MontarProvaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Garante que cada teste rode em sua transação, que será revertida ao final
class ProvaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProvaRepository provaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CertificacaoRepository certificacaoRepository;
    @Autowired
    private QuestaoRepository questaoRepository;

    private Usuario usuario;
    private Certificacao certificacao;
    private Prova prova;

    @BeforeEach
    void setUp() {
        // Limpa os repositórios para garantir isolamento
        provaRepository.deleteAll();
        questaoRepository.deleteAll();
        certificacaoRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Cria entidades base para os testes
        usuario = usuarioRepository.save(Usuario.builder().nome("Usuario Teste").email("teste@certifai.com").build());
        certificacao = certificacaoRepository.save(Certificacao.builder().nome("Cert Teste").questoes(new HashSet<>()).build());

        Questao questao = questaoRepository.save(Questao.builder()
                .enunciado("Qual a resposta?")
                .dificuldade(Dificuldade.FACIL)
                .certificacoes(Collections.singleton(certificacao))
                .build());

        certificacao.getQuestoes().add(questao);
        certificacaoRepository.save(certificacao);

        prova = provaRepository.save(Prova.builder()
                .usuario(usuario)
                .certificacao(certificacao)
                .status(Status.PENDENTE)
                .build());
    }

    @Test
    @DisplayName("POST /provas/montar-personalizada - Deve montar prova e retornar 201 Created")
    void montarProvaPersonalizada_ComRequestValido_DeveRetornarStatus201() throws Exception {
        // Arrange
        MontarProvaRequest request = new MontarProvaRequest(
                usuario.getId(),
                certificacao.getId(),
                1,
                Dificuldade.FACIL,
                true,
                60);

        // Act & Assert
        mockMvc.perform(post("/v1/provas/montar-personalizada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Prova montada com sucesso.")))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.usuarioId", is(usuario.getId().intValue())))
                .andExpect(jsonPath("$.data.status", is("PENDENTE")));
    }

    @Test
    @DisplayName("GET /provas/{id} - Deve buscar prova por ID e retornar 200 OK")
    void buscarPorId_ComIdExistente_DeveRetornarStatus200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/provas/{id}", prova.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(prova.getId().intValue())));
    }

    @Test
    @DisplayName("GET /provas/{id} - Deve retornar 404 Not Found para ID inexistente")
    void buscarPorId_ComIdInexistente_DeveRetornarStatus404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/provas/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /provas - Deve listar todas as provas e retornar 200 OK")
    void listarTodas_DeveRetornarListaDeProvasEStatus200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/provas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(prova.getId().intValue())));
    }

    @Test
    @DisplayName("GET /provas/usuario/{usuarioId} - Deve listar provas por usuário e retornar 200 OK")
    void listarPorUsuario_DeveRetornarListaDeProvasEStatus200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/provas/usuario/{usuarioId}", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].usuarioId", is(usuario.getId().intValue())));
    }

    @Test
    @DisplayName("DELETE /provas/{id} - Deve excluir prova e retornar 200 OK")
    void excluir_ComIdExistente_DeveRetornarStatus200() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/v1/provas/{id}", prova.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Prova removida com sucesso.")));
    }

    @Test
    @DisplayName("POST /provas/{id}/iniciar - Deve iniciar prova e retornar 200 OK")
    void iniciar_ComProvaPendente_DeveRetornarStatus200() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/v1/provas/{id}/iniciar", prova.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("ANDAMENTO")))
                .andExpect(jsonPath("$.message", is("Prova iniciada.")));
    }

    @Test
    @DisplayName("POST /provas/{id}/iniciar - Deve retornar 409 Conflict ao iniciar prova não pendente")
    void iniciar_ComProvaNaoPendente_DeveRetornarStatus409() throws Exception {
        // Arrange
        prova.setStatus(Status.CONCLUIDA);
        provaRepository.save(prova);

        // Act & Assert
        mockMvc.perform(post("/v1/provas/{id}/iniciar", prova.getId()))
                .andExpect(status().isConflict()); // Assumindo que IllegalStateException é mapeado para 409
    }

    @Test
    @DisplayName("POST /provas/{id}/finalizar - Deve finalizar prova e retornar 200 OK")
    void finalizar_ComProvaEmAndamento_DeveRetornarStatus200() throws Exception {
        prova.setStatus(Status.ANDAMENTO);
        provaRepository.save(prova);

        mockMvc.perform(post("/v1/provas/{id}/finalizar", prova.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("CONCLUIDA")))
                .andExpect(jsonPath("$.data.pontuacao", is(0)))
                .andExpect(jsonPath("$.message", is("Prova finalizada com sucesso.")));
    }
}
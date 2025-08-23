package br.com.certifai.services;

import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.model.Alternativa;
import br.com.certifai.model.Questao;
import br.com.certifai.repository.AlternativaRepository;
import br.com.certifai.service.impl.AlternativaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlternativaServiceTest {

    @Mock
    private AlternativaRepository alternativaRepository;

    @InjectMocks
    private AlternativaService alternativaService;

    private Questao questao;
    private Alternativa alternativa;

    @BeforeEach
    void setUp() {
        questao = Questao.builder().id(1L).enunciado("Qual a capital do Brasil?").build();
        alternativa = Alternativa.builder()
                .id(10L)
                .texto("Brasília")
                .correta(true)
                .questao(questao)
                .build();
    }

    @Test
    @DisplayName("Deve criar uma alternativa com sucesso")
    void criarDeveRetornarAlternativaSalva() {
        Alternativa alternativaParaSalvar = Alternativa.builder()
                .texto("Brasília")
                .correta(true)
                .questao(questao)
                .build();

        when(alternativaRepository.save(any(Alternativa.class))).thenReturn(alternativa);

        Alternativa savedAlternativa = alternativaService.create(alternativaParaSalvar);

        assertNotNull(savedAlternativa);
        assertEquals("Brasília", savedAlternativa.getTexto());
        assertEquals(10L, savedAlternativa.getId());
        verify(alternativaRepository, times(1)).save(any(Alternativa.class));
    }

    @Test
    @DisplayName("Deve encontrar alternativa por ID com sucesso")
    void findByIdComSucesso() {
        when(alternativaRepository.findById(10L)).thenReturn(Optional.of(alternativa));

        Optional<Alternativa> foundOptional = alternativaService.findById(10L);

        assertTrue(foundOptional.isPresent());
        assertEquals(10L, foundOptional.get().getId());
    }

    @Test
    @DisplayName("Deve retornar um Optional vazio se a alternativa não existe")
    void findByIdSemSucesso() {
        when(alternativaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Alternativa> result = alternativaService.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar uma lista de todas as alternativas")
    void findAllComSucesso() {
        when(alternativaRepository.findAll()).thenReturn(List.of(alternativa));

        List<Alternativa> result = alternativaService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Brasília", result.get(0).getTexto());
    }

    @Test
    @DisplayName("Deve atualizar uma alternativa com sucesso")
    void updateComSucesso() {
        Alternativa detalhesAtualizacao = Alternativa.builder().texto("Distrito Federal").correta(true).build();
        Alternativa alternativaAtualizada = Alternativa.builder().id(10L).texto("Distrito Federal").correta(true).questao(questao).build();

        when(alternativaRepository.findById(10L)).thenReturn(Optional.of(alternativa));
        when(alternativaRepository.save(any(Alternativa.class))).thenReturn(alternativaAtualizada);

        Alternativa result = alternativaService.update(10L, detalhesAtualizacao);

        assertNotNull(result);
        assertEquals("Distrito Federal", result.getTexto());
        verify(alternativaRepository, times(1)).findById(10L);
        verify(alternativaRepository, times(1)).save(any(Alternativa.class));
    }


    @Test
    @DisplayName("Deve retornar uma lista vazia se não houver alternativas")
    void findAllSemSucesso() {
        when(alternativaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Alternativa> result = alternativaService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar uma alternativa com sucesso")
    void deleteComAlternativaExistente() {
        when(alternativaRepository.existsById(10L)).thenReturn(true);
        doNothing().when(alternativaRepository).deleteById(10L);

        alternativaService.delete(10L);

        verify(alternativaRepository, times(1)).existsById(10L);
        verify(alternativaRepository, times(1)).deleteById(10L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar alternativa que não existe")
    void deleteComAlternativaInexistente() {
        when(alternativaRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            alternativaService.delete(99L);
        });
        verify(alternativaRepository, never()).deleteById(anyLong());
    }
}

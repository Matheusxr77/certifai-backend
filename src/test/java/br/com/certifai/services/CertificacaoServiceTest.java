package br.com.certifai.services;

import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.model.Certificacao;
import br.com.certifai.model.Prova;
import br.com.certifai.repository.CertificacaoRepository;
import br.com.certifai.service.impl.CertificacaoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificacaoServiceTest {

    @Mock
    private CertificacaoRepository certificacaoRepository;

    @InjectMocks
    private CertificacaoService certificacaoService;

    private Certificacao certificacao;

    @BeforeEach
    void setUp() {
        certificacao = Certificacao.builder()
                .id(1L)
                .nome("Certificação Java Básico")
                .descricao("Fundamentos da linguagem Java")
                .provas(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Deve criar uma certificação com sucesso")
    void deveCriarCertificacao_quandoDadosValidos() {
        Certificacao certificacaoParaSalvar = Certificacao.builder().nome("Nova Certificação").build();
        when(certificacaoRepository.save(any(Certificacao.class))).thenReturn(certificacao);

        Certificacao certificacaoSalva = certificacaoService.create(certificacaoParaSalvar);

        assertNotNull(certificacaoSalva);
        assertEquals("Certificação Java Básico", certificacaoSalva.getNome());
        verify(certificacaoRepository, times(1)).save(any(Certificacao.class));
    }

    @Test
    @DisplayName("Deve retornar todas as certificações")
    void deveRetornarTodasAsCertificacoes() {
        Certificacao outraCertificacao = Certificacao.builder().id(2L).nome("Certificação Spring").build();
        when(certificacaoRepository.findAll()).thenReturn(Arrays.asList(certificacao, outraCertificacao));

        List<Certificacao> certificacoes = certificacaoService.findAll();

        assertNotNull(certificacoes);
        assertEquals(2, certificacoes.size());
        verify(certificacaoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma certificação por ID quando encontrada")
    void deveRetornarCertificacaoPorId_quandoEncontrada() {
        when(certificacaoRepository.findById(1L)).thenReturn(Optional.of(certificacao));

        Optional<Certificacao> certificacaoEncontrada = certificacaoService.findById(1L);

        assertTrue(certificacaoEncontrada.isPresent());
        assertEquals(certificacao.getId(), certificacaoEncontrada.get().getId());
        verify(certificacaoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio por ID quando não encontrada")
    void deveRetornarOptionalVazioPorId_quandoNaoEncontrada() {
        when(certificacaoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Certificacao> certificacaoEncontrada = certificacaoService.findById(99L);

        assertFalse(certificacaoEncontrada.isPresent());
        verify(certificacaoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve atualizar uma certificação com sucesso")
    void deveAtualizarCertificacao_quandoEncontrada() {
        Certificacao detalhesAtualizacao = Certificacao.builder().nome("Nome Atualizado").descricao("Descrição Atualizada").build();
        when(certificacaoRepository.findById(1L)).thenReturn(Optional.of(certificacao));
        when(certificacaoRepository.save(any(Certificacao.class))).thenReturn(certificacao);

        Certificacao certificacaoAtualizada = certificacaoService.update(1L, detalhesAtualizacao);

        assertNotNull(certificacaoAtualizada);
        assertEquals("Nome Atualizado", certificacaoAtualizada.getNome());
        assertEquals("Descrição Atualizada", certificacaoAtualizada.getDescricao());
        verify(certificacaoRepository, times(1)).findById(1L);
        verify(certificacaoRepository, times(1)).save(certificacao);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar certificação inexistente")
    void deveLancarExcecaoAoAtualizar_quandoNaoEncontrada() {
        Certificacao detalhesAtualizacao = Certificacao.builder().build();
        when(certificacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            certificacaoService.update(99L, detalhesAtualizacao);
        });
        verify(certificacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar uma certificação com sucesso")
    void deveDeletarCertificacao_quandoEncontrada() {
        when(certificacaoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(certificacaoRepository).deleteById(1L);

        certificacaoService.delete(1L);

        verify(certificacaoRepository, times(1)).existsById(1L);
        verify(certificacaoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar deletar certificação inexistente")
    void deveLancarExcecaoAoDeletar_quandoNaoEncontrada() {
        when(certificacaoRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            certificacaoService.delete(99L);
        });
        verify(certificacaoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve calcular e atualizar o tempo total da certificação corretamente")
    void deveCalcularEAtualizarTempoTotal() {
        Prova prova1 = new Prova();
        prova1.setTempo(90L);
        Prova prova2 = new Prova();
        prova2.setTempo(30L);
        certificacao.setProvas(Arrays.asList(prova1, prova2));

        when(certificacaoRepository.findById(1L)).thenReturn(Optional.of(certificacao));
        when(certificacaoRepository.save(any(Certificacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Certificacao certificacaoAtualizada = certificacaoService.calcularEAtualizarTempoTotal(1L);

        assertNotNull(certificacaoAtualizada);
        assertEquals(120L, certificacaoAtualizada.getTempo());
        verify(certificacaoRepository, times(1)).findById(1L);
        verify(certificacaoRepository, times(1)).save(any(Certificacao.class));
    }
}
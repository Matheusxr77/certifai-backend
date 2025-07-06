package br.com.certifai.services;

import br.com.certifai.enums.Roles;
import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.model.*;
import br.com.certifai.repository.*;
import br.com.certifai.requests.NovaSenhaRequest;
import br.com.certifai.service.impl.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveListarTodosUsuarios() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarTodos();

        assertEquals(2, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistentePorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioService.buscarPorId(1L));
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario existente = Usuario.builder()
                .id(1L)
                .name("Fulano")
                .email("fulano@email.com")
                .password("123")
                .role(Roles.ESTUDANTE)
                .ativo(true)
                .build();

        Usuario atualizado = Usuario.builder()
                .name("Ciclano")
                .email("novo@email.com")
                .password("novaSenha")
                .role(Roles.PROFESSOR)
                .ativo(true)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmailAndIdNot("novo@email.com", 1L)).thenReturn(false);
        when(passwordEncoder.encode("novaSenha")).thenReturn("hashedNovaSenha");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.atualizar(1L, atualizado);

        assertEquals("Ciclano", resultado.getName());
        assertEquals("novo@email.com", resultado.getEmail());
        assertEquals("hashedNovaSenha", resultado.getPassword());
        assertEquals(Roles.PROFESSOR, resultado.getRole());
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@email.com");

        when(usuarioRepository.findByEmail("email@email.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorEmail("email@email.com");

        assertEquals("email@email.com", resultado.getEmail());
    }

    @Test
    void deveVerificarEmailDisponivel() {
        when(usuarioRepository.existsByEmail("livre@email.com")).thenReturn(false);
        assertTrue(usuarioService.verificarDisponibilidadeEmail("livre@email.com"));
    }

    @Test
    void deveAtivarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setAtivo(false);
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.ativarUsuario(1L);

        assertTrue(usuario.getAtivo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveDesativarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.desativarUsuario(1L);

        assertFalse(usuario.getAtivo());
        verify(usuarioRepository).save(usuario);
    }
}

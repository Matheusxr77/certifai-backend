package br.com.certifai.controller.impl;

import br.com.certifai.controller.interfaces.UsuarioApi;
import br.com.certifai.dto.UsuarioDTO;
import br.com.certifai.response.AbstractResponse;
import br.com.certifai.mappers.IUsuarioMapper;
import br.com.certifai.model.Usuario;
import br.com.certifai.requests.NovaSenhaRequest;
import br.com.certifai.service.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuarioApi {

    private final IUsuarioService usuarioService;
    private final IUsuarioMapper usuarioMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<UsuarioDTO>>> listarTodos() {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
        return ResponseEntity.ok(AbstractResponse.success(usuarios));
    }

    @Override
    public ResponseEntity<AbstractResponse<UsuarioDTO>> buscarPorId(Long id) {
        UsuarioDTO usuario = usuarioMapper.toDTO(usuarioService.buscarPorId(id));
        return ResponseEntity.ok(AbstractResponse.success(usuario));
    }

    @Override
    public ResponseEntity<AbstractResponse<UsuarioDTO>> criar(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioService.criar(usuarioMapper.toEntity(usuarioDTO));
        UsuarioDTO usuarioCriado = usuarioMapper.toDTO(usuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(usuarioCriado, "Usuário criado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<UsuarioDTO>> atualizar(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuarioMapper.toEntity(usuarioDTO));
        UsuarioDTO dtoAtualizado = usuarioMapper.toDTO(usuarioAtualizado);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Usuário atualizado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> alterarSenha(Long id, NovaSenhaRequest novaSenha) {
        usuarioService.alterarSenha(id, novaSenha.senha());
        return ResponseEntity.ok(AbstractResponse.success(null, "Senha alterada com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        usuarioService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Usuário removido com sucesso"));
    }
}

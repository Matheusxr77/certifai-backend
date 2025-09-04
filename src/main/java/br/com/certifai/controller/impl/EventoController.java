package br.com.certifai.controller.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.certifai.controller.interfaces.EventoApi;
import br.com.certifai.dto.CertificacaoDTO;
import br.com.certifai.dto.EventoDTO;
import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.mappers.IEventoMapper;
import br.com.certifai.model.Evento;
import br.com.certifai.response.AbstractResponse;
import br.com.certifai.service.impl.EventoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EventoController implements EventoApi {
    private final EventoService eventoService;
    private final IEventoMapper eventoMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<EventoDTO>>> listarTodos() {
        List<EventoDTO> eventos = eventoService.listarTodos()
                .stream()
                .map(eventoMapper::toDTO)
                .toList();

        return ResponseEntity.ok(AbstractResponse.success(eventos));
    }

    @Override
    public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                eventoMapper.toDTO(eventoService.buscarPorId(id)));
    }

    @Override
    public ResponseEntity<EventoDTO> criar(@RequestBody EventoDTO dto) {
        Evento evento = eventoMapper.toEntity(dto);
        Evento salvo = eventoService.salvar(evento);
        return ResponseEntity.ok(eventoMapper.toDTO(salvo));
    }

    @Override
    public ResponseEntity<EventoDTO> adicionarItem(
            @PathVariable Long eventoId,
            @PathVariable Long itemId) {
        Evento atualizado = eventoService.adicionarItem(eventoId, itemId);
        return ResponseEntity.ok(eventoMapper.toDTO(atualizado));
    }

    @Override
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        eventoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<EventoDTO>> listarAgendaPorUsuario(@PathVariable Long usuarioId) {
        List<EventoDTO> agenda = eventoMapper.toDtoList(
                eventoService.listarAgendaPorUsuario(usuarioId));
        return ResponseEntity.ok(agenda);
    }

    @Override
    public ResponseEntity<EventoDTO> editarEvento(@PathVariable("eventoId") Long eventoId, @RequestBody EventoDTO dto) {

        
        try {
            Evento eventoAtualizado = eventoService.editarEvento(eventoId, dto);
            return ResponseEntity.ok(eventoMapper.toDTO(eventoAtualizado));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

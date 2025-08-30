package br.com.certifai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.certifai.dto.EventoDTO;
import br.com.certifai.exception.EntidadeNaoEncontradaException;
import br.com.certifai.model.Evento;
import br.com.certifai.model.ItemChecklist;
import br.com.certifai.repository.EventoRepository;
import br.com.certifai.repository.ItemChecklistRepository;
import br.com.certifai.service.interfaces.IEventoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventoService implements IEventoService {
    private final EventoRepository eventoRepository;
    private final ItemChecklistRepository itemChecklistRepository;

    @Transactional
    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    @Transactional
    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + id));
    }

    @Transactional
    public Evento salvar(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Transactional
    public Evento adicionarItem(Long eventoId, Long itemId) {
        Evento evento = buscarPorId(eventoId);
        ItemChecklist item = itemChecklistRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado: " + itemId));

        item.setEvento(evento);
        itemChecklistRepository.save(item);

        return evento;
    }

    @Transactional
    public void excluir(Long id) {
        eventoRepository.deleteById(id);
    }

    @Transactional
    public List<Evento> listarAgendaPorUsuario(Long usuarioId) {
        // busca todos os eventos que possuem itens de um usuário específico
        return eventoRepository.findDistinctByItensUsuarioId(usuarioId);
    }

    @Transactional
    public Evento editarEvento(Long eventoId, EventoDTO dto) {
        Evento eventoExistente = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado: " + eventoId));

        if (dto.getTitulo() != null) {
            eventoExistente.setTitulo(dto.getTitulo());
        }
        if (dto.getDescricao() != null) {
            eventoExistente.setDescricao(dto.getDescricao());
        }
        if (dto.getInicio() != null) {
            eventoExistente.setInicio(dto.getInicio());
        }
        if (dto.getFim() != null) {
            eventoExistente.setFim(dto.getFim());
        }

        return eventoRepository.save(eventoExistente);
    }

}

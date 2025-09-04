package br.com.certifai.service.interfaces;

import java.util.List;

import br.com.certifai.model.Evento;

public interface IEventoService {
    List<Evento> listarTodos();
    Evento buscarPorId(Long id);
    Evento salvar(Evento evento);
    Evento adicionarItem(Long eventoId, Long itemId);
    void excluir(Long id);

}

package br.com.certifai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.certifai.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{
    List<Evento> findDistinctByItensUsuarioId(Long usuarioId);
}

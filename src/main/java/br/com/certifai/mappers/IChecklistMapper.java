package br.com.certifai.mappers;

import br.com.certifai.dto.ChecklistDTO;
import br.com.certifai.model.Certificacao;
import br.com.certifai.model.Checklist;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.UsuarioRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface IChecklistMapper {

    @Mapping(target = "certificacao", expression = "java(toCertificacao(dto.getCertificacaoId()))")
    @Mapping(target = "usuario", expression = "java(toUsuario(dto.getUsuarioId()))")
    Checklist toEntity(ChecklistDTO dto);

    @Mapping(target = "certificacaoId", source = "certificacao.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    ChecklistDTO toDTO(Checklist entity);

    default Certificacao toCertificacao(Long id) {
        if (id == null) return null;
        Certificacao c = new Certificacao();
        c.setId(id);
        return c;
    }

    default Usuario toUsuario(Long id) {
        if (id == null) return null;
        Usuario u = new Usuario();
        u.setId(id);
        return u;
    }
}
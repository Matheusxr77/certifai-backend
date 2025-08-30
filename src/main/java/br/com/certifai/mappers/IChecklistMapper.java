package br.com.certifai.mappers;

import br.com.certifai.dto.ChecklistDTO;
import br.com.certifai.model.Certificacao;
import br.com.certifai.model.Checklist;
import br.com.certifai.model.Usuario;
import br.com.certifai.repository.UsuarioRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = { ICertificacaoMapper.class, IUsuarioMapper.class })
public interface IChecklistMapper {

    @Mapping(target = "itensChecklist", ignore = true)
    @Mapping(target = "certificacao", source = "certificacaoId", qualifiedByName = "mapCertificacao")
    @Mapping(target = "usuario", source = "usuarioId")
    Checklist toEntity(ChecklistDTO dto);

    @Mapping(target = "usuarioId", source = "usuario.id")
    ChecklistDTO toDTO(Checklist entity);

    default Usuario map(Long usuarioId) {
        if (usuarioId == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        return usuario;

    }

    @Named("mapCertificacao")
    default Certificacao mapCertificacao(Long id) {
        if (id == null)
            return null;
        Certificacao certificacao = new Certificacao();
        certificacao.setId(id);
        return certificacao;
    }

}

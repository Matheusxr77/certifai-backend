package br.com.certifai.mappers;

import br.com.certifai.dto.UsuarioDTO;
import br.com.certifai.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUsuarioMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    Usuario toEntity(UsuarioDTO dto);

    UsuarioDTO toDTO(Usuario entity);
}

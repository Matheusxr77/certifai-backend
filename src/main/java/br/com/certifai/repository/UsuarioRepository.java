package br.com.certifai.repository;

import br.com.certifai.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByEmail(String email);
    Optional<Usuario> findByVerificationToken(String verificationToken);
}

package Unilibre.PicasyFijasK.repository;

import Unilibre.PicasyFijasK.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
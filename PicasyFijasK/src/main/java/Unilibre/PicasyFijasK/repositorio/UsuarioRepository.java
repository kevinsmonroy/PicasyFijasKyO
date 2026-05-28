package Unilibre.PicasyFijasK.repositorio;

import Unilibre.PicasyFijasK.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
package Unilibre.PicasyFijasK.repositorio;

import Unilibre.PicasyFijasK.entidad.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    List<Partida> findByUsuarioId(Long usuarioId);
    List<Partida> findAllByOrderByPuntajeDesc();


}
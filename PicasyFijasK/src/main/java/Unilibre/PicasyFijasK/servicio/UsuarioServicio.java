package Unilibre.PicasyFijasK.servicio;

import Unilibre.PicasyFijasK.entidad.Usuario;
import Unilibre.PicasyFijasK.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicio {

    private final UsuarioRepository repository;

    public UsuarioServicio(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario obtener(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}

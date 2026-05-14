package Unilibre.PicasyFijasK.controller;

import Unilibre.PicasyFijasK.entity.Usuario;
import Unilibre.PicasyFijasK.service.UsuarioService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // ✅ Crear usuario
    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return service.guardar(usuario);
    }

    // ✅ Listar todos
    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    // ✅ Obtener por ID
    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    // ✅ Eliminar
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Usuario eliminado correctamente";
    }

    // ✅ Subir imagen (avatar)
    @PostMapping("/upload/{id}")
    public String subirImagen(@PathVariable Long id,
                              @RequestParam("file") MultipartFile file) {

        try {
            // 📁 carpeta donde se guardará la imagen
            String carpeta = "uploads/";
            String nombreArchivo = file.getOriginalFilename();

            // crear carpeta si no existe
            Files.createDirectories(Paths.get(carpeta));

            // ruta final
            String ruta = carpeta + nombreArchivo;

            // guardar archivo
            Files.copy(file.getInputStream(), Paths.get(ruta));

            // actualizar usuario
            Usuario usuario = service.obtener(id);

            if (usuario == null) {
                return "Usuario no encontrado";
            }

            usuario.setAvatarUrl(ruta);
            service.guardar(usuario);

            return "Imagen subida correctamente: " + ruta;

        } catch (Exception e) {
            return "Error al subir imagen: " + e.getMessage();
        }
    }
}

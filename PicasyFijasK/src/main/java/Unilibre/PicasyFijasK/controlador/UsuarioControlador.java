package Unilibre.PicasyFijasK.controlador;

import Unilibre.PicasyFijasK.dto.UsuarioDTO;
import Unilibre.PicasyFijasK.entidad.Usuario;
import Unilibre.PicasyFijasK.servicio.UsuarioServicio;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private final UsuarioServicio service;

    public UsuarioControlador(UsuarioServicio service) {
        this.service = service;
    }

    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "usuario";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute UsuarioDTO dto, HttpSession session) {

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());

        if (dto.getAvatarUrl() == null || dto.getAvatarUrl().isBlank()) {
            usuario.setAvatarUrl("https://i.imgur.com/Z7AzH2c.png");
        } else {
            usuario.setAvatarUrl(dto.getAvatarUrl());
        }

        Usuario usuarioGuardado = service.guardar(usuario);


        session.setAttribute("usuarioId", usuarioGuardado.getId());

        return "redirect:/partidas/formulario";

    }



    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "CONEXION EXITOSA CON EL CONTROLADOR ";
    }



}

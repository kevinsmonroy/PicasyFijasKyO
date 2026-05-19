package Unilibre.PicasyFijasK.controller;

import Unilibre.PicasyFijasK.dto.UsuarioDTO;
import Unilibre.PicasyFijasK.entity.Usuario;
import Unilibre.PicasyFijasK.service.UsuarioService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "usuario";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute UsuarioDTO dto) {

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setAvatarUrl(dto.getAvatarUrl());

        Usuario usuarioGuardado = service.guardar(usuario);

        return "redirect:/partidas/nueva?usuarioId=" + usuarioGuardado.getId();
    }


    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "CONEXION EXITOSA CON EL CONTROLADOR ";
    }



}

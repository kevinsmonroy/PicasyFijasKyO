package Unilibre.PicasyFijasK.controller;

import Unilibre.PicasyFijasK.dto.IntentoDTO;
import Unilibre.PicasyFijasK.dto.ResultadoDTO;
import Unilibre.PicasyFijasK.entity.Partida;
import Unilibre.PicasyFijasK.entity.Usuario;
import Unilibre.PicasyFijasK.service.PartidaService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/partidas")
public class PartidaController {

    private final PartidaService service;

    public PartidaController(PartidaService service) {
        this.service = service;
    }

    // CREAR FORMULARIO PARTIDA
    @GetMapping("/nueva")
    public String nuevaPartida(@RequestParam Long usuarioId, Model model) {

        Partida partida = new Partida();

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        partida.setUsuario(usuario);

        model.addAttribute("partida", partida);

        return "crear_partida";
    }

    //  CREAR PARTIDA
    @PostMapping("/crear")
    public String crear(@ModelAttribute Partida partida) {

        Partida partidaGuardada = service.crearPartida(partida);

        return "redirect:/partidas/jugar/" + partidaGuardada.getId();
    }

    // ENTRAR A JUGAR
    @GetMapping("/jugar/{id}")
    public String jugarVista(@PathVariable Long id, Model model, HttpSession session) {

        Partida partida = service.obtenerPorId(id);

        //  NO borrar historial si ya existe
        List<ResultadoDTO> historial =
                (List<ResultadoDTO>) session.getAttribute("historial");

        if (historial == null) {
            historial = new ArrayList<>();
            session.setAttribute("historial", historial);
        }

        model.addAttribute("historial", historial);
        model.addAttribute("partidaId", id);
        model.addAttribute("intentoDTO", new IntentoDTO());
        model.addAttribute("usuario", partida.getUsuario());

        return "jugar";
    }

    // HACER INTENTO
    @PostMapping("/intentar/{id}")
    public String intentar(@PathVariable Long id,
                           @ModelAttribute IntentoDTO dto,
                           Model model,
                           HttpSession session) {

        Partida partida = service.obtenerPorId(id);

        //  VALIDACIÓN (evita el error 500)
        if (dto.getIntento() == null || dto.getIntento().isEmpty()) {
            model.addAttribute("error", "Debes ingresar un número de 4 dígitos");
            model.addAttribute("usuario", partida.getUsuario());
            model.addAttribute("partidaId", id);
            model.addAttribute("intentoDTO", new IntentoDTO());

            List<ResultadoDTO> historial =
                    (List<ResultadoDTO>) session.getAttribute("historial");

            model.addAttribute("historial", historial);

            return "jugar";
        }

        // OBTENER HISTORIAL
        List<ResultadoDTO> historial =
                (List<ResultadoDTO>) session.getAttribute("historial");

        if (historial == null) {
            historial = new ArrayList<>();
        }

        //  EJECUTAR JUEGO
        ResultadoDTO resultado = service.jugar(id, dto.getIntento());
        resultado.setIntento(dto.getIntento());

        historial.add(resultado);

        //  GUARDAR EN SESIÓN
        session.setAttribute("historial", historial);

        //  ENVIAR TODOS LOS DATOS A LA VISTA
        model.addAttribute("historial", historial);
        model.addAttribute("usuario", partida.getUsuario());
        model.addAttribute("partidaId", id);
        model.addAttribute("intentoDTO", new IntentoDTO());

        return "jugar";
    }
}

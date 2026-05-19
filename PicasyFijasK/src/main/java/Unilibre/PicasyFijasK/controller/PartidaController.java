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

    //  FORMULARIO PARTIDA
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

    //  VISTA DEL JUEGO
    @GetMapping("/jugar/{id}")
    public String jugarVista(@PathVariable Long id, Model model, HttpSession session) {

        Partida partida = service.obtenerPorId(id);

        //  Recuperar historial
        List<ResultadoDTO> historial =
                (List<ResultadoDTO>) session.getAttribute("historial");

        if (historial == null) {
            historial = new ArrayList<>();
            session.setAttribute("historial", historial);
        }

        // Recuperar error si existe
        String error = (String) session.getAttribute("error");
        session.removeAttribute("error");

        model.addAttribute("error", error);
        model.addAttribute("historial", historial);
        model.addAttribute("partidaId", id);
        model.addAttribute("intentoDTO", new IntentoDTO());
        model.addAttribute("usuario", partida.getUsuario());

        return "jugar";
    }

    //  INTENTAR (POST → PROCESAR Y REDIRIGIR)
    @PostMapping("/intentar/{id}")
    public String intentar(@PathVariable Long id,
                           @ModelAttribute IntentoDTO dto,
                           HttpSession session) {

        //  Validación
        if (dto.getIntento() == null || dto.getIntento().isBlank()) {
            session.setAttribute("error", "Debes ingresar un número de 4 dígitos");
            return "redirect:/partidas/jugar/" + id;
        }

        Partida partida = service.obtenerPorId(id);

        //  Obtener historial
        List<ResultadoDTO> historial =
                (List<ResultadoDTO>) session.getAttribute("historial");

        if (historial == null) {
            historial = new ArrayList<>();
        }

        //  Ejecutar juego
        ResultadoDTO resultado = service.jugar(id, dto.getIntento());
        resultado.setIntento(dto.getIntento());

        historial.add(resultado);

        //  Guardar en sesión
        session.setAttribute("historial", historial);

        //  REDIRECCIÓN
        return "redirect:/partidas/jugar/" + id;
    }
}

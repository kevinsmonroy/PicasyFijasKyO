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

    @GetMapping("/nueva")
    public String nuevaPartida(@RequestParam Long usuarioId, Model model) {

        Partida partida = new Partida();

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        partida.setUsuario(usuario);

        model.addAttribute("partida", partida);

        return "crear_partida";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute Partida partida) {

        Partida partidaGuardada = service.crearPartida(partida);

        return "redirect:/partidas/jugar/" + partidaGuardada.getId();
    }

    @GetMapping("/jugar/{id}")
    public String jugarVista(@PathVariable Long id, Model model, HttpSession session) {

        Partida partida = service.obtenerPorId(id);

        session.setAttribute("historial", new ArrayList<>());

        model.addAttribute("partidaId", id);
        model.addAttribute("intentoDTO", new IntentoDTO());
        model.addAttribute("usuario", partida.getUsuario());

        return "jugar";
    }




    @PostMapping("/intentar/{id}")
    public String intentar(@PathVariable Long id,
                           @ModelAttribute IntentoDTO dto,
                           Model model,
                           HttpSession session) {

        // ✅ Validar input
        if (dto.getIntento() == null || dto.getIntento().isEmpty()) {
            model.addAttribute("error", "Debes ingresar un número");
            model.addAttribute("partidaId", id);
            model.addAttribute("intentoDTO", new IntentoDTO());
            return "jugar";
        }

        // ✅ Obtener historial
        List<ResultadoDTO> historial =
                (List<ResultadoDTO>) session.getAttribute("historial");

        if (historial == null) {
            historial = new ArrayList<>();
        }

        // ✅ Jugar
        ResultadoDTO resultado = service.jugar(id, dto.getIntento());
        resultado.setIntento(dto.getIntento());

        historial.add(resultado);

        session.setAttribute("historial", historial);

        model.addAttribute("historial", historial);
        model.addAttribute("partidaId", id);
        model.addAttribute("intentoDTO", new IntentoDTO());

        return "jugar";
    }


}
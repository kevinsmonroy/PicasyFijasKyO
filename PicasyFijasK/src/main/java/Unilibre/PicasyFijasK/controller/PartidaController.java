package Unilibre.PicasyFijasK.controller;

import Unilibre.PicasyFijasK.dto.IntentoDTO;
import Unilibre.PicasyFijasK.dto.ResultadoDTO;
import Unilibre.PicasyFijasK.entity.Partida;
import Unilibre.PicasyFijasK.service.PartidaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/partidas")
public class PartidaController {

    private final PartidaService service;

    public PartidaController(PartidaService service) {
        this.service = service;
    }

    @GetMapping("/nueva")
    public String nuevaPartida(Model model) {
        model.addAttribute("partida", new Partida());
        return "crear_partida";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute Partida partida) {

        Partida partidaGuardada = service.crearPartida(partida);

        return "redirect:/partidas/jugar/" + partidaGuardada.getId();
    }

    @GetMapping("/jugar/{id}")
    public String jugarVista(@PathVariable Long id, Model model) {

        model.addAttribute("partidaId", id);
        model.addAttribute("intentoDTO", new IntentoDTO());

        return "jugar";
    }

    @PostMapping("/intentar/{id}")
    public String intentar(@PathVariable Long id,
                           @ModelAttribute IntentoDTO dto,
                           Model model) {

        ResultadoDTO resultado = service.jugar(id, dto.getIntento());

        model.addAttribute("resultado", resultado);
        model.addAttribute("partidaId", id);

        return "jugar";
    }
}
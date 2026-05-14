package Unilibre.PicasyFijasK.controller;

import Unilibre.PicasyFijasK.entity.Partida;
import Unilibre.PicasyFijasK.service.PartidaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    private final PartidaService service;

    public PartidaController(PartidaService service) {
        this.service = service;
    }

    @PostMapping
    public Partida guardar(@RequestBody Partida partida) {
        return service.guardar(partida);
    }

    @GetMapping
    public List<Partida> listar() {
        return service.listar();
    }

    @GetMapping("/ranking")
    public List<Partida> ranking() {
        return service.ranking();
    }
}

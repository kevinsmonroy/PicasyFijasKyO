package Unilibre.PicasyFijasK.controller;

import Unilibre.PicasyFijasK.service.JuegoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/juego")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping("/nuevo")
    public String nuevoJuego() {
        return juegoService.generarNumero();
    }

    @PostMapping("/evaluar")
    public String evaluar(@RequestParam String secreto,
                          @RequestParam String intento) {
        return juegoService.evaluar(secreto, intento);
    }
}
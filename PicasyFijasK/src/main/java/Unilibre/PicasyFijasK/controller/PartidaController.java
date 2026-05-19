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

    //  CREAR Y ENTRAR DIRECTAMENTE A UNA NUEVA PARTIDA (SIN FORMULARIO)
    @GetMapping("/nueva")
    public String nuevaPartida(HttpSession session) {

        Long usuarioId = (Long) session.getAttribute("usuarioId");

        //  Si no hay usuario en sesión, volver al registro
        if (usuarioId == null) {
            return "redirect:/usuarios/nuevo";
        }

        //  Limpiar datos anteriores
        session.removeAttribute("historial");
        session.removeAttribute("resultado");
        session.removeAttribute("error");

        //  Crear partida automáticamente
        Partida partida = new Partida();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        partida.setUsuario(usuario);

        Partida partidaGuardada = service.crearPartida(partida);

        //  Ir directo al juego
        return "redirect:/partidas/jugar/" + partidaGuardada.getId();
    }

    //  MOSTRAR VISTA DE JUEGO
    @GetMapping("/jugar/{id}")
    public String jugarVista(@PathVariable Long id,
                             Model model,
                             HttpSession session) {

        Partida partida = service.obtenerPorId(id);

        //  Obtener historial
        List<ResultadoDTO> historial =
                (List<ResultadoDTO>) session.getAttribute("historial");

        if (historial == null) {
            historial = new ArrayList<>();
            session.setAttribute("historial", historial);
        }

        //  Recuperar resultado (solo 1 vez)
        ResultadoDTO resultado =
                (ResultadoDTO) session.getAttribute("resultado");

        session.removeAttribute("resultado");

        //  Recuperar error (solo 1 vez)
        String error = (String) session.getAttribute("error");
        session.removeAttribute("error");

        //  Enviar datos a vista
        model.addAttribute("resultado", resultado);
        model.addAttribute("error", error);
        model.addAttribute("historial", historial);
        model.addAttribute("partidaId", id);
        model.addAttribute("intentoDTO", new IntentoDTO());
        model.addAttribute("usuario", partida.getUsuario());
        model.addAttribute("ganado", partida.isGanado());

        return "jugar";
    }

    //  PROCESAR INTENTO (PRG: REDIRECT)
    @PostMapping("/intentar/{id}")
    public String intentar(@PathVariable Long id,
                           @ModelAttribute IntentoDTO dto,
                           HttpSession session) {

        //  Validar input
        if (dto.getIntento() == null || !dto.getIntento().matches("\\d{4}")) {
            session.setAttribute("error", "Debe ingresar 4 números válidos");
            return "redirect:/partidas/jugar/" + id;
        }

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
        session.setAttribute("resultado", resultado);

        //  Redireccionar
        return "redirect:/partidas/jugar/" + id;
    }
}

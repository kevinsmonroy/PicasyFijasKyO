package Unilibre.PicasyFijasK.controlador;

import Unilibre.PicasyFijasK.dto.IntentoDTO;
import Unilibre.PicasyFijasK.dto.ResultadoDTO;
import Unilibre.PicasyFijasK.entidad.Partida;
import Unilibre.PicasyFijasK.entidad.Usuario;
import Unilibre.PicasyFijasK.servicio.PartidaServicio;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/partidas")
public class PartidaControlador {

    private final PartidaServicio service;

    public PartidaControlador(PartidaServicio service) {
        this.service = service;
    }

    //  CREAR PARTIDA DESDE FORMULARIO (CON NIVEL)
    @PostMapping("/crear")
    public String crear(@ModelAttribute Partida partida,
                        HttpSession session) {

        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/usuarios/nuevo";
        }

        //  ASIGNAR USUARIO DESDE SESIÓN
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        partida.setUsuario(usuario);

        //  VALIDACIÓN DE SEGURIDAD
        if (partida.getMaxIntentos() == null || partida.getMaxIntentos() == 0) {
            partida.setMaxIntentos(10);
        }

        //  LIMPIAR SESIÓN
        session.removeAttribute("historial");
        session.removeAttribute("resultado");
        session.removeAttribute("error");

        //  CREAR PARTIDA
        Partida partidaGuardada = service.crearPartida(partida);

        return "redirect:/partidas/jugar/" + partidaGuardada.getId();
    }
    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {

        model.addAttribute("partida", new Partida());

        return "crear_partida";
    }

    //  NUEVA PARTIDA RÁPIDA (SIN FORMULARIO)
    @GetMapping("/nueva")
    public String nuevaPartida(@RequestParam(required = false) Integer maxIntentos,
                               HttpSession session) {

        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/usuarios/nuevo";
        }

        session.removeAttribute("historial");
        session.removeAttribute("resultado");
        session.removeAttribute("error");

        Partida partida = new Partida();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        partida.setUsuario(usuario);


        if (maxIntentos == null || maxIntentos == 0) {
            partida.setMaxIntentos(10); // default
        } else {
            partida.setMaxIntentos(maxIntentos);
        }

        Partida partidaGuardada = service.crearPartida(partida);

        return "redirect:/partidas/jugar/" + partidaGuardada.getId();
    }

    //  VISTA
    @GetMapping("/jugar/{id}")
    public String jugarVista(@PathVariable Long id,
                             Model model,
                             HttpSession session) {

        Partida partida = service.obtenerPorId(id);

        List<ResultadoDTO> historial =
                (List<ResultadoDTO>) session.getAttribute("historial");

        if (historial == null) {
            historial = new ArrayList<>();
            session.setAttribute("historial", historial);
        }

        ResultadoDTO resultado =
                (ResultadoDTO) session.getAttribute("resultado");
        session.removeAttribute("resultado");

        String error = (String) session.getAttribute("error");
        session.removeAttribute("error");

        boolean perdido = (!partida.isGanado()
                && partida.getIntentos() >= partida.getMaxIntentos());

        model.addAttribute("perdido", perdido);
        model.addAttribute("resultado", resultado);
        model.addAttribute("error", error);
        model.addAttribute("historial", historial);
        model.addAttribute("partidaId", id);
        model.addAttribute("intentoDTO", new IntentoDTO());
        model.addAttribute("usuario", partida.getUsuario());
        model.addAttribute("ganado", partida.isGanado());
        model.addAttribute("partida", partida);

        return "jugar";
    }

    // INTENTAR
    @PostMapping("/intentar/{id}")
    public String intentar(@PathVariable Long id,
                           @ModelAttribute IntentoDTO dto,
                           HttpSession session) {

        if (dto.getIntento() == null || !dto.getIntento().matches("\\d{4}")) {
            session.setAttribute("error", "Debe ingresar 4 números válidos");
            return "redirect:/partidas/jugar/" + id;
        }

        List<ResultadoDTO> historial =
                (List<ResultadoDTO>) session.getAttribute("historial");

        if (historial == null) {
            historial = new ArrayList<>();
        }

        ResultadoDTO resultado = service.jugar(id, dto.getIntento());
        resultado.setIntento(dto.getIntento());

        historial.add(resultado);

        session.setAttribute("historial", historial);
        session.setAttribute("resultado", resultado);

        return "redirect:/partidas/jugar/" + id;
    }
}


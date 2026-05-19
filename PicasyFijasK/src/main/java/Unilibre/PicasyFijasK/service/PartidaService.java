package Unilibre.PicasyFijasK.service;

import Unilibre.PicasyFijasK.dto.ResultadoDTO;
import Unilibre.PicasyFijasK.entity.Partida;
import Unilibre.PicasyFijasK.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    private final PartidaRepository repository;
    private final JuegoService juegoService;

    public PartidaService(PartidaRepository repository, JuegoService juegoService) {
        this.repository = repository;
        this.juegoService = juegoService;
    }

    public Partida crearPartida(Partida partida) {

        // generar número secreto automáticamente
        String numeroSecreto = juegoService.generarNumero();

        partida.setNumeroSecreto(numeroSecreto);
        partida.setIntentos(0);
        partida.setGanado(false);
        partida.setPuntaje(0);


        //  NIVEL (MUY IMPORTANTE)

        if (partida.getMaxIntentos() == null || partida.getMaxIntentos() == 0) {
            partida.setMaxIntentos(10); // fallback seguro
        }



        return repository.save(partida);
    }

    public ResultadoDTO jugar(Long partidaId, String intento) {

        Partida partida = repository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        // usar la lógica del juego
        ResultadoDTO resultado = juegoService.evaluar(
                partida.getNumeroSecreto(),
                intento
        );

        // aumentar intentos

// aumentar intentos
        partida.setIntentos(partida.getIntentos() + 1);

//  validar si ya alcanzó el límite de intentos
        if (partida.getIntentos() >= partida.getMaxIntentos()) {

            // si NO ganó todavía → perdió
            if (resultado.getFijas() < 4) {
                partida.setGanado(false);

                //
                //
            }
        }



        // verificar si ganó
        if (resultado.getFijas() == 4) {
            partida.setGanado(true);
        }

        // calcular puntaje

        int puntaje = (partida.getMaxIntentos() - partida.getIntentos() + 1) * 10;

        if (puntaje < 0) puntaje = 0;


        partida.setPuntaje(puntaje);

        // guardar cambios
        repository.save(partida);

        // completar resultado
        resultado.setIntentos(partida.getIntentos());
        resultado.setGanado(partida.isGanado());

        return resultado;
    }


    public List<Partida> listar() {
        return repository.findAll();
    }

    public List<Partida> ranking() {
        return repository.findAll()
                .stream()
                .sorted((a, b) -> b.getPuntaje() - a.getPuntaje())
                .toList();
    }
    public Partida obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada con ID: " + id));
    }

}

package Unilibre.PicasyFijasK.service;

import Unilibre.PicasyFijasK.entity.Partida;
import Unilibre.PicasyFijasK.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    private final PartidaRepository repository;

    public PartidaService(PartidaRepository repository) {
        this.repository = repository;
    }

    public Partida guardar(Partida partida) {

        int puntaje = 100 - (partida.getIntentos() * 10);
        if (puntaje < 0) puntaje = 0;

        partida.setPuntaje(puntaje);

        return repository.save(partida);
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
}

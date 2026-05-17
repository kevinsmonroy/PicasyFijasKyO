package Unilibre.PicasyFijasK.service;

import Unilibre.PicasyFijasK.dto.ResultadoDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JuegoService {

    public String generarNumero() {
        List<Integer> numeros = new ArrayList<>();
        Random random = new Random();

        while (numeros.size() < 4) {
            int num = random.nextInt(10);
            if (!numeros.contains(num)) {
                numeros.add(num);
            }
        }

        StringBuilder resultado = new StringBuilder();
        for (int n : numeros) {
            resultado.append(n);
        }

        return resultado.toString();
    }

    public ResultadoDTO evaluar(String secreto, String intento) {
        int picas = 0;
        int fijas = 0;

        for (int i = 0; i < intento.length(); i++) {
            char actual = intento.charAt(i);

            if (actual == secreto.charAt(i)) {
                fijas++;
            } else if (secreto.indexOf(actual) != -1) {
                picas++;
            }
        }

        return new ResultadoDTO(picas, fijas, 0, false);
    }
}
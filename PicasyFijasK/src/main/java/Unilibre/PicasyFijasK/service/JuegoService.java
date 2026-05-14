package Unilibre.PicasyFijasK.service;

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

    public String evaluar(String secreto, String intento) {
        int picas = 0;
        int fijas = 0;

        for (int i = 0; i < intento.length(); i++) {

            if (intento.charAt(i) == secreto.charAt(i)) {
                fijas++;
            } else if (secreto.contains("" + intento.charAt(i))) {
                picas++;
            }
        }

        return "Picas: " + picas + " - Fijas: " + fijas;
    }
}
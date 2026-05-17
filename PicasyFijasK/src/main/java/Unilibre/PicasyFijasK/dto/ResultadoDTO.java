package Unilibre.PicasyFijasK.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoDTO {

    private int picas;
    private int fijas;
    private int intentos;
    private boolean ganado;
}
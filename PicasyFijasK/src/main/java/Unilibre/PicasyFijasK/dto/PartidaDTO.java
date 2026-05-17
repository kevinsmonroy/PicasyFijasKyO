package Unilibre.PicasyFijasK.dto;
import  lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PartidaDTO {
    private Long id;
    private int intentos;
    private boolean ganado;
    private int puntaje;
    private Long usuarioId;
}

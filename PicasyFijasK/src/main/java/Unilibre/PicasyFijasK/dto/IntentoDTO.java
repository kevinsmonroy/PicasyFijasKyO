package Unilibre.PicasyFijasK.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntentoDTO {

    @NotBlank(message = "Debe ingresar un número")
    private String intento;
}

package Unilibre.PicasyFijasK.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "partidas")
@Table(name = "PARTIDAS")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroSecreto;
    private int intentos;
    private boolean ganado;
    private int puntaje;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
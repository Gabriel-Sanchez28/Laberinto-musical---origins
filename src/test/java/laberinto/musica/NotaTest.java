package laberinto.musica;

import laberinto.matematicas.Fracciones;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotaTest {
    @Test
    public void probarDuracionConPuntillo() {
        Nota nota = new Nota(
                "Do",
                4,
                "natural",
                new Fracciones(1, 4),
                1
        );
        Fracciones duracion = nota.obtenerDuracion();
        assertEquals(3, duracion.getNumerador());
        assertEquals(8, duracion.getDenominador());
    }

}
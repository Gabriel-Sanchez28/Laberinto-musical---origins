package laberinto.musica;

import laberinto.matematicas.Fracciones;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartituraTest {

    @Test
    public void probarCompasCompleto() {
        Partitura partitura = new Partitura(
                "Obra de prueba",
                "Compositor",
                4,
                4
        );
        partitura.agregarNota(
                new Nota("Do", 4, "natural", new Fracciones(1, 4), 0)
        );
        partitura.agregarNota(
                new Nota("Re", 4, "natural", new Fracciones(1, 4), 0)
        );
        partitura.agregarNota(
                new Nota("Mi", 4, "natural", new Fracciones(1, 4), 0)
        );
        partitura.agregarNota(
                new Nota("Fa", 4, "natural", new Fracciones(1, 4), 0)
        );
        assertTrue(partitura.estaCompasCompleto());
    }

    @Test
    public void probarExcesoDeDuracion() {
        Partitura partitura = new Partitura(
                "Obra de prueba",
                "Compositor",
                4,
                4
        );
        for (int i = 0; i < 4; i++) {
            partitura.agregarNota(
                    new Nota("Do", 4, "natural", new Fracciones(1, 4), 0)
            );
        }
        assertThrows(IllegalArgumentException.class, () -> {
            partitura.agregarNota(
                    new Nota("Sol", 4, "natural", new Fracciones(1, 4), 0)
            );
        });
    }
}
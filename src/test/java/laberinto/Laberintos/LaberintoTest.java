package laberinto.Laberintos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LaberintoTest {
    private final int FILAS = 10;
    private final int COLUMNAS = 10;
    private Laberinto laberinto;
    @BeforeEach
    void setUp() {
        // Creamos una instancia fresca antes de cada test
        laberinto = new Laberinto(FILAS, COLUMNAS);
    }

    @Test
    void generarNuevoMapa() {

    }

    @Test
    void siguienteNivel() {
    }

    @Test
    void obtenerCelda() {
        Laberinto laberinto = new Laberinto(FILAS,COLUMNAS);
        assertEquals(0, laberinto.obtenerCelda(2, 3),
                "La celda de inicio (1,1) debería retornar siempre 0");
    }

    @Test
    void esTransitable() {
        boolean transitable = laberinto.esTransitable(3,3);
        assertEquals(true, transitable,
                "La celda de inicio (1,1) debería retornar siempre true");
    }

    @Test
    void esMeta() {
    }

    @Test
    void obtenerFilas() {
    }

    @Test
    void obtenerColumnas() {
    }

    @Test
    void obtenerNivel() {
    }
}
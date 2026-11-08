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
        assertDoesNotThrow(() -> {
            this.laberinto.generarNuevoMapa();
        }, "el metodo no funciona correctamente");
    }

    @Test
    void siguienteNivel() {
        assertDoesNotThrow(() -> {
            this.laberinto.siguienteNivel();
        }, "el metodo no funciona correctamente");
        int nivel = laberinto.obtenerNivel();
        assertEquals(2,nivel);
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
        boolean meta = laberinto.esMeta(3,3);
        assertEquals(false, meta);
    }

    @Test
    void obtenerFilas() {
        int fila = laberinto.obtenerFilas();
        assertEquals(10,fila);
    }

    @Test
    void obtenerColumnas() {
        int columna = laberinto.obtenerColumnas();
        assertEquals(10,columna);
    }

    @Test
    void obtenerNivel() {
        int nivel = laberinto.obtenerNivel();
        assertEquals(1,nivel);
    }
}
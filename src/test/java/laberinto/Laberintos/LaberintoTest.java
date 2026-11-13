package laberinto.Laberintos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LaberintoTest {
    private final int filas = 10;
    private final int columnas = 10;
    private Laberinto laberinto;
    @BeforeEach
    void setUp() {
        // Creamos una instancia fresca antes de cada test
        laberinto = new Laberinto(filas, columnas);
    }

    @Test
    void generarNuevoMapaTest() {
        assertDoesNotThrow(() -> {
            this.laberinto.generarNuevoMapa();
        }, "el metodo no funciona correctamente");
    }

    @Test
    void generarNuevoMapaBordesTest(){
        for (int i = 0; i < filas; i++) {
            assertEquals(1, laberinto.obtenerCelda(i, 0), "Borde izquierdo en fila " + i + " debe ser 1");
            assertEquals(1, laberinto.obtenerCelda(i, columnas - 1), "Borde derecho en fila " + i + " debe ser 1");
        }
        for (int j = 0; j < columnas; j++) {
            assertEquals(1, laberinto.obtenerCelda(0, j), "Borde superior en columna " + j + " debe ser 1");
            assertEquals(1, laberinto.obtenerCelda(filas - 1, j), "Borde inferior en columna " + j + " debe ser 1");
        }
    }

    @Test
    void generarNuevoMapaInicioYMetaTest(){
        assertEquals(0, laberinto.obtenerCelda(1, 1), "El punto de inicio [1][1] debe ser 0");
        assertEquals(3, laberinto.obtenerCelda(filas - 2, columnas - 2), "La meta debe estar en [filas-2][columnas-2] con valor 3");
    }

    @Test
    void generarNuevoMapaEspacioInteriorTest(){
        for (int j = 1; j < columnas - 1; j++) {
            assertEquals(0, laberinto.obtenerCelda(1, j), "El corredor horizontal en (1, " + j + ") debe estar limpio (0)");
        }
        for (int i = 1; i < filas - 1; i++) {
            if (i==8){
                int subi=1;
                assertEquals(0, laberinto.obtenerCelda(subi, columnas - 2), "El corredor vertical en (" + i + ", penúltima) debe estar limpio (0)");
            }else {
                assertEquals(0, laberinto.obtenerCelda(i, columnas - 2), "El corredor vertical en (" + i + ", penúltima) debe estar limpio (0)");
            }
        }
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
        Laberinto laberinto = new Laberinto(filas,columnas);
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
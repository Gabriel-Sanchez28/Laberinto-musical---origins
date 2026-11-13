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
    void siguienteNivelTest() {
        for (int i = 1; i < 11; i++) {
            int nivel = laberinto.obtenerNivel();
            assertEquals(i,nivel);
            laberinto.siguienteNivel();
        }
    }

    @Test
    void obtenerCeldaNegativasTest() {
        assertEquals(1, laberinto.obtenerCelda(-1, 5), "Fila negativa (-1) debe retornar 1");
        assertEquals(1, laberinto.obtenerCelda(5, -1), "Columna negativa (-1) debe retornar 1");
        assertEquals(1, laberinto.obtenerCelda(-5, -5), "Ambas coordenadas negativas deben retornar 1");
    }

    @Test
    void obtenerCeldaLimitesTest(){
        assertEquals(1, laberinto.obtenerCelda(filas, 5), "Fila igual al límite superior debe retornar 1");
        assertEquals(1, laberinto.obtenerCelda(5, columnas), "Columna igual al límite superior debe retornar 1");
        assertEquals(1, laberinto.obtenerCelda(filas + 3, columnas + 3), "Coordenadas muy por encima del límite deben retornar 1");
    }

    @Test
    void obtenerCeldaValidasTest(){
        assertEquals(0, laberinto.obtenerCelda(1, 1), "La celda [1][1] debe retornar 0");
        assertEquals(3, laberinto.obtenerCelda(filas - 2, columnas - 2), "La celda de la meta debe retornar 3");
        assertEquals(1, laberinto.obtenerCelda(0, 0), "La celda [0][0] debe retornar 1");
    }

    @Test
    void esTransitableTest() {
        boolean transitable = laberinto.esTransitable(1,1);
        assertEquals(0,laberinto.obtenerCelda(1,1));
        assertEquals(true, transitable,"La celda de inicio (1,1) debería retornar siempre true");
        boolean transitable2 = laberinto.esTransitable(8,8);
        assertEquals(3,laberinto.obtenerCelda(8,8));
        assertEquals(true, transitable,"La celda de inicio (8,8) debería retornar siempre true");
    }

    @Test
    void esTransitableNoTransitableTest() {
        boolean transitable = laberinto.esTransitable(0,0);
        assertEquals(1,laberinto.obtenerCelda(0,0));
        assertEquals(false, transitable,"La celda de inicio (0,0) debería retornar siempre true");
    }

    @Test
    void esMetaTest() {
        boolean meta = laberinto.esMeta(8,8);
        assertEquals(true, meta);
    }

    @Test
    void NoEsMetaTest() {
        boolean meta = laberinto.esMeta(3,8);
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
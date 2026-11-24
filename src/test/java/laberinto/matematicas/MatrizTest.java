package laberinto.matematicas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrizTest {
    @Test
    public void probarSumaMatrices() {
        int[][] datosA = {{1, 2}, {3, 4}};
        int[][] datosB = {{2, 1}, {1, 2}};
        Matriz A = new Matriz(datosA);
        Matriz B = new Matriz(datosB);
        Matriz resultado = A.sumar(B);
        assertEquals(3, resultado.obtenerElemento(0, 0));
        assertEquals(3, resultado.obtenerElemento(0, 1));
        assertEquals(4, resultado.obtenerElemento(1, 0));
        assertEquals(6, resultado.obtenerElemento(1, 1));
    }

    @Test
    public void probarMultiplicacionMatrices() {
        int[][] datosA = {{1, 2}, {3, 4}};
        int[][] datosB = {{2, 1}, {1, 2}};
        Matriz A = new Matriz(datosA);
        Matriz B = new Matriz(datosB);
        Matriz resultado = A.multiplicar(B);
        assertEquals(4, resultado.obtenerElemento(0, 0));
        assertEquals(5, resultado.obtenerElemento(0, 1));
        assertEquals(10, resultado.obtenerElemento(1, 0));
        assertEquals(11, resultado.obtenerElemento(1, 1));
    }

    @Test
    public void probarDeterminante() {
        int[][] datos = {{1, 2}, {3, 4}};
        Matriz matriz = new Matriz(datos);
        assertEquals(-2, matriz.obtenerDeterminante());
    }
}
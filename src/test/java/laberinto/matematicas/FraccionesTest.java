package laberinto.matematicas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FraccionesTest {
    @Test
    public void probarSuma() {
        Fracciones f1 = new Fracciones(1, 2);
        Fracciones f2 = new Fracciones(1, 4);
        Fracciones resultado = f1.sumar(f2);
        assertEquals(3, resultado.getNumerador());
        assertEquals(4, resultado.getDenominador());
    }

    @Test
    public void probarSimplificacion() {
        Fracciones fraccion = new Fracciones(6, 8);
        assertEquals(3, fraccion.getNumerador());
        assertEquals(4, fraccion.getDenominador());
    }

    @Test
    public void probarDivisionEntreCero() {
        Fracciones f1 = new Fracciones(1, 2);
        Fracciones f2 = new Fracciones(0, 1);
        assertThrows(ArithmeticException.class, () -> {f1.dividir(f2);});
    }
}
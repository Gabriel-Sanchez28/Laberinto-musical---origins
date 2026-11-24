package laberinto.matematicas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EcuacionTest {
    @Test
    public void probarResolucion() {
        Ecuacion ecuacion = new Ecuacion(3, 5, 20);
        assertEquals(5, ecuacion.resolver());
        assertTrue(ecuacion.verificarRespuesta(5));
    }

    @Test
    public void probarCoeficienteCero() {
        assertThrows(IllegalArgumentException.class, () -> {new Ecuacion(0, 5, 20);});
    }

}
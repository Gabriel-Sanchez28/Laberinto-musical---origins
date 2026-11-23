package laberinto.musica;

import laberinto.matematicas.Fracciones;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestorPartituraTest {
    @Test
    public void probarRecuperacionDeFragmentos() {
        Partitura partitura = new Partitura(
                "Obra del Laberinto",
                "Compositor de prueba",
                4,
                4
        );
        GestorPartitura gestor = new GestorPartitura(partitura);
        FragmentoMusical fragmento = new FragmentoMusical(
                1,
                new Nota("Do", 4, "natural", new Fracciones(1, 4), 0)
        );
        gestor.agregarFragmento(fragmento);
        assertTrue(gestor.recuperarFragmento(1));
        assertFalse(gestor.recuperarFragmento(1));
        assertEquals(1, gestor.obtenerCantidadRecuperada());
        assertTrue(fragmento.estaRecuperado());
    }

}
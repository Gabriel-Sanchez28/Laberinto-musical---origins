package laberinto.juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JuegoTest {
    @Test
    public void probarInicioPartida() {
        Juego juego = new Juego("Leonardo");
        assertEquals(EstadoJuego.JUGANDO, juego.obtenerEstado());
        assertFalse(juego.partidaTerminada());
        assertEquals(3, juego.obtenerJugador().getVida());
    }

    @Test
    public void probarDerrota() {
        Juego juego = new Juego("Leonardo");
        juego.moverJugador(0, 1);
        assertTrue(juego.hayPuertaPendiente());
        System.out.println("vidad"+juego.obtenerJugador().getVida());
        juego.resolverReto("999/1");
        System.out.println("vidad"+juego.obtenerJugador().getVida());
        juego.resolverReto("999/1");
        System.out.println("vidad"+juego.obtenerJugador().getVida());
        juego.resolverReto("999/1");
        System.out.println("vidad"+juego.obtenerJugador().getVida());
        assertEquals(0, juego.obtenerJugador().getVida());
        assertEquals(EstadoJuego.DERROTA, juego.obtenerEstado());
        assertTrue(juego.partidaTerminada());
        assertFalse(juego.resolverReto(juego.obtenerRespuestaPrueba()));
    }

    @Test
    public void probarMetaSinObraCompleta() {
        Juego juego = new Juego("Leonardo");
        resolverPuerta(juego);
        resolverPuerta(juego);
        juego.moverJugador(0, 1);
        juego.moverJugador(0, 1);
        for (int i = 0; i < 4; i++) {juego.moverJugador(1, 0);}
        assertEquals(2, juego.obtenerNivel());
        assertEquals(EstadoJuego.JUGANDO, juego.obtenerEstado());
        assertFalse(juego.obraCompleta());
        assertFalse(juego.partidaTerminada());
    }

    @Test
    public void probarVictoriaConObraCompleta() {
        Juego juego = new Juego("Leonardo");
        resolverPuerta(juego);
        resolverPuerta(juego);
        juego.moverJugador(0, 1);
        juego.moverJugador(0, 1);
        for (int i = 0; i < 4; i++) {juego.moverJugador(1, 0);}
        assertEquals(2, juego.obtenerNivel());
        resolverPuerta(juego);
        resolverPuerta(juego);
        resolverPuerta(juego);
        juego.moverJugador(0, 1);
        for (int i = 0; i < 4; i++) {juego.moverJugador(1, 0);}
        assertTrue(juego.obraCompleta());
        assertEquals(EstadoJuego.VICTORIA, juego.obtenerEstado());
        assertTrue(juego.partidaTerminada());
    }

    private void resolverPuerta(Juego juego) {
        assertFalse(juego.moverJugador(0, 1));
        assertTrue(juego.hayPuertaPendiente());
        String respuesta = juego.obtenerRespuestaPrueba();
        assertTrue(juego.resolverReto(respuesta));
    }
}
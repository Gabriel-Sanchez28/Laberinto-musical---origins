package laberinto.juego;

import laberinto.Laberintos.Laberinto;

public class Juego {
    private Jugador jugador;
    private Laberinto laberinto;
    private EstadoJuego estado;

    public Jugador getJugador() {
        return jugador;
    }

    public Laberinto getLaberinto() {
        return laberinto;
    }

    public EstadoJuego getEstado() {
        return estado;
    }

    public Juego(Jugador jugador, Laberinto laberinto) {
        this.jugador = jugador;
        this.laberinto = laberinto;
        this.estado = EstadoJuego.LABERINTO;
    }

    public void iniciar(){}
    public void mostrarMenu(){}
    public void seleccionarObra(){}
    public void explorar(){}
    public void procesarPuzzle(){}
    public void actualizarJuego(){}
    public void finalizar(){}

}

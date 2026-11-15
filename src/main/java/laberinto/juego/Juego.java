package laberinto.juego;

import laberinto.Laberintos.Laberinto;

public class Juego {
    private Jugador jugador;
    private Laberinto laberinto;
    private EstadoJuego estado = EstadoJuego.LABERINTO;

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
    //validar que se este en el labretino
    public boolean intentarMover(int deltaFila, int deltaColumna) {
        if (estado != EstadoJuego.LABERINTO) {return false;}
        if (Math.abs(deltaFila) + Math.abs(deltaColumna) != 1) {return false;}

        int fila = jugador.getFila() + deltaFila;
        int columna = jugador.getColumna() + deltaColumna;
        if (!laberinto.esTransitable(fila, columna)) {return false;}

        jugador.cambiarPosicion(fila, columna);
        return true;
    }


    public void iniciar(){}
    public void mostrarMenu(){}
    public void seleccionarObra(){}
    public void explorar(){}
    public void procesarPuzzle(){}
    public void actualizarJuego(){}
    public void finalizar(){}

}

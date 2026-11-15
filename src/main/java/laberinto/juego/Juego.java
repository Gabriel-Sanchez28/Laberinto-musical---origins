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

    public boolean jugadorEnMeta() {
        return laberinto.esMeta(jugador.getFila(), jugador.getColumna());
    }
    // ... (tus variables y constructor actual se quedan igual) ...

    public static Juego iniciar(String nombreJugador){
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
        throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
    }
        //Crear al jugador
        Jugador jugador = new Jugador(nombreJugador.trim());

        //Crear laberinto nicel 1
        Laberinto laberinto = new Laberinto(10, 10);

        return new Juego(jugador, laberinto);
    }
    public void mostrarMenu(){}
    public void seleccionarObra(){}
    public void explorar(){}
    public void procesarPuzzle(){}
    public void actualizarJuego(){}
    public void finalizar(){}

}

package laberinto.juego;

import laberinto.Laberintos.Laberinto;
import laberinto.Laberintos.Puerta;
import laberinto.matematicas.Fracciones;
import laberinto.musica.*;
import laberinto.puzzles.GestorEjercicios;
import laberinto.puzzles.Puzzle;

public class Juego {
    private Jugador jugador;
    private Laberinto laberinto;
    private EstadoJuego estado;
    private static final int filaInicio = 1;
    private static final int columnaInicio = 1;
    private GestorEjercicios gestorEjercicios;
    private Puzzle puzzleActual;
    private Puerta puertaActual;
    private GestorPartitura gestorPartitura;

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
        puzzleActual = null;
        puertaActual = null;
        inicializarPartitura();
        this.estado = EstadoJuego.JUGANDO;
    }
    //validar que se este en el labretino
    public boolean intentarMover(int deltaFila, int deltaColumna) {
        if (estado != EstadoJuego.JUGANDO) {return false;}
        if (puertaActual != null) {return false;}
        if (Math.abs(deltaFila) + Math.abs(deltaColumna) != 1) {return false;}

        int fila = jugador.getFila() + deltaFila;
        int columna = jugador.getColumna() + deltaColumna;
        //PRUEBA DE UBICACION DE LA PUERTA
        if (laberinto.esPuerta(5, 5)) {
            puertaActual = laberinto.obtenerPuerta(5, 5);
            puzzleActual = gestorEjercicios.crearEjercicio(laberinto.obtenerNivel());
            return false;
        }
        if (!laberinto.esTransitable(fila, columna)) {return false;}
        if (obraCompleta()) {
            jugador.cambiarPosicion(fila, columna);
            estado = EstadoJuego.VICTORIA;
            return true;
        }
        jugador.cambiarPosicion(fila, columna);
        return true;
    }

    public boolean hayPuertaPendiente() {return puertaActual != null;}

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

    public void actualizarJuego(){
        laberinto.siguienteNivel();
        jugador.cambiarPosicion(filaInicio, columnaInicio);
    }

    private void inicializarPartitura() {
        Partitura partitura = new Partitura(
                "Obra del Laberinto",
                "Compositor de prueba",
                4,
                4
        );
        gestorPartitura = new GestorPartitura(partitura);
        gestorPartitura.agregarFragmento(
                new FragmentoMusical(1,
                        new Nota("Do", 4, "natural",
                                new Fracciones(1, 4), 0))
        );
        gestorPartitura.agregarFragmento(
                new FragmentoMusical(2,
                        new Nota("Re", 4, "natural",
                                new Fracciones(1, 4), 0))
        );
        gestorPartitura.agregarFragmento(
                new FragmentoMusical(3,
                        new Nota("Mi", 4, "natural",
                                new Fracciones(1, 4), 0))
        );
        gestorPartitura.agregarFragmento(
                new FragmentoMusical(4,
                        new Nota("Fa", 4, "natural",
                                new Fracciones(1, 4), 0))
        );
    }
    public void finalizar(){}

    public boolean resolverReto(String respuesta) {
        if (estado != EstadoJuego.JUGANDO) {return false;}
        if (puzzleActual == null || puertaActual == null) {return false;}
        if (puzzleActual.verificarRespuesta(respuesta)) {
            jugador.sumarPuntos(puzzleActual.obtenerPuntos());
            jugador.aumentarRacha();
            jugador.completarTarea();
            int fila = puertaActual.obtenerFila();
            int columna = puertaActual.obtenerColumna();
            laberinto.abrirPuerta(fila, columna);
            jugador.cambiarPosicion(fila, columna);
            recuperarSiguienteFragmento();
            puzzleActual = null;
            puertaActual = null;
            return true;
        } else {
            jugador.perderVida();
            jugador.reiniciarRacha();
            if (!jugador.estaVivo()) {estado = EstadoJuego.DERROTA;}
            return false;
        }
    }

    private boolean recuperarSiguienteFragmento() {
        for (FragmentoMusical fragmento :
                gestorPartitura.getFragmentos()) {
            if (!fragmento.estaRecuperado()) {return gestorPartitura.recuperarFragmento(fragmento.getId());
            }
        }
        return false;
    }

    public String obtenerProgresoMusical() {
        int recuperados = gestorPartitura.obtenerCantidadRecuperada();
        int total = gestorPartitura.getFragmentos().size();
        return "Fragmentos musicales recuperados: " + recuperados + "/" + total;
    }

    public boolean obraCompleta() {return gestorPartitura.estaObraCompleta();}

    public GestorPartitura obtenerGestorPartitura() {return gestorPartitura;}

    public Jugador obtenerJugador() {return jugador;}

    public Laberinto obtenerLaberinto() {return laberinto;}

    public int obtenerNivel() {return laberinto.obtenerNivel();}

    public boolean partidaTerminada() {
        return estado == EstadoJuego.VICTORIA || estado == EstadoJuego.DERROTA;
    }

    public String obtenerRespuestaPrueba() {
        if (puzzleActual == null) {return "";}
        return puzzleActual.obtenerRespuestaCorrecta();
    }

    public EstadoJuego obtenerEstado() {return estado;}

    public void reproducirObraCompleta() {
        if (!obraCompleta()) {
            System.out.println("La obra musical aun no esta completa.");
            return;
        }
        Reproductor reproductor = new Reproductor();
        reproductor.reproducirPartitura(
                gestorPartitura.getPartitura(),
                120
        );
    }

}

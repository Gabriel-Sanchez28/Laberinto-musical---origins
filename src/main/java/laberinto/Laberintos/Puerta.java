package laberinto.Laberintos;

import laberinto.puzzles.Puzzle;

public class Puerta {
    private int fila;
    private int columna;
    private boolean abierta;

    public Puerta(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.abierta = false;
    }

    public int obtenerFila() {return fila;}
    public int obtenerColumna() {return columna;}
    public boolean estaAbierta() {return abierta;}
    public void abrir() {abierta = true;}
}

package laberinto.Laberintos;

import laberinto.puzzles.Puzzle;

public class Puerta {
    private int fila;
    private int columna;
    private boolean abierta;

    private EstadoPuerta estado = EstadoPuerta.CERRADA;
    private double progresoAnimacion = 0.0;
    private static final double DURACION_ANIMACION = 0.5;

    public Puerta(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.abierta = false;
    }

    public int obtenerFila() {return fila;}
    public int obtenerColumna() {return columna;}
    public EstadoPuerta getEstado() { return estado; }
    public double getProgresoAnimacion() { return progresoAnimacion; }
    public boolean estaAbierta() {return abierta;}
    public void abrir() {abierta = true;
        estado = EstadoPuerta.ABRIENDO;
        progresoAnimacion = 0.0;
    }
    public void actualizarAnimacion(double dt) {
        if (estado == EstadoPuerta.ABRIENDO) {
            progresoAnimacion += dt / DURACION_ANIMACION;
            if (progresoAnimacion >= 1.0) {
                progresoAnimacion = 1.0;
                estado = EstadoPuerta.ABIERTA;
            }
        }
    }
}

package laberinto.puzzles;

public class Puzzle {
    private String enunciado;
    private int dificultad;
    private int puntos;

    public Puzzle(String enunciado, int dificultad, int puntos) {
        this.enunciado = enunciado;
        this.dificultad = dificultad;
        this.puntos = puntos;
    }

    public String obtenerEnunciado() {return enunciado;}
    public int obtenerDificultad() {return dificultad;}
    public int obtenerPuntos() {return puntos;}
    public abstract boolean verificarRespuesta(String respuesta);
    public abstract String obtenerRespuestaCorrecta();
}

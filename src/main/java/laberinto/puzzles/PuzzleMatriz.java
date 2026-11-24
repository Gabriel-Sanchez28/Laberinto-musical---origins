package laberinto.puzzles;

import laberinto.matematicas.Matriz;

public class PuzzleMatriz extends Puzzle{
    private Matriz matriz1;
    private Matriz matriz2;
    private Matriz resultado;

    public PuzzleMatriz(int[][] matriz1, int[][] matriz2, int dificultad, int puntos) {
        super("Resuelve la suma de las dos matrices.", dificultad, puntos);
        this.matriz1 = new Matriz(matriz1);
        this.matriz2 = new Matriz(matriz2);
        resultado = this.matriz1.sumar(this.matriz2);
    }

    public String obtenerMatrices() {return "Matriz A:\n" + matriz1 + "\nMatriz B:\n" + matriz2;}

    @Override
    public boolean verificarRespuesta(String respuesta) {
        try {
            String[] filasRespuesta = respuesta.trim().split(";");
            if (filasRespuesta.length != resultado.getFilas()) {return false;}
            for (int i = 0; i < resultado.getFilas(); i++) {
                String[] valores = filasRespuesta[i].trim().split(",");
                if (valores.length != resultado.getColumnas()) {return false;}
                for (int j = 0; j < resultado.getColumnas(); j++) {
                    int valor = Integer.parseInt(valores[j].trim());
                    if (valor != resultado.obtenerElemento(i, j)) {return false;}
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String obtenerRespuestaCorrecta() {
        String respuesta = "";
        for (int i = 0; i < resultado.getFilas(); i++) {
            for (int j = 0; j < resultado.getColumnas(); j++) {
                respuesta += resultado.obtenerElemento(i, j);
                if (j < resultado.getColumnas() - 1) {respuesta += ",";}
            }
            if (i < resultado.getFilas() - 1) {respuesta += ";";}
        }
        return respuesta;
    }
}

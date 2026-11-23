package laberinto.puzzles;

import java.util.Random;

public class GestorEjercicios {
    private Random random;
    public GestorEjercicios() {random = new Random();}
    public laberinto.puzzles.Puzzle crearEjercicio(int nivel) {
        if (nivel <= 2) {
            return crearPuzzleFraccion(nivel);
        } else {
            return crearPuzzleMatriz(nivel);
        }
    }

    private PuzzleFracciones crearPuzzleFraccion(int nivel) {
        int denominador = random.nextInt(3) + 2;
        int numerador1 = random.nextInt(denominador - 1) + 1;
        int numerador2 = random.nextInt(denominador - 1) + 1;
        String operacion = "+";
        if (random.nextBoolean()) {
            operacion = "-";
            if (numerador1 < numerador2) {
                int auxiliar = numerador1;
                numerador1 = numerador2;
                numerador2 = auxiliar;
            }
        }
        return new PuzzleFracciones(
                numerador1, denominador,
                numerador2, denominador,
                operacion, nivel, nivel * 10
        );
    }

    private PuzzleMatriz crearPuzzleMatriz(int nivel) {
        int[][] matrizA = new int[2][2];
        int[][] matrizB = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                matrizA[i][j] = random.nextInt(5) + 1;
                matrizB[i][j] = random.nextInt(5) + 1;
            }
        }
        return new PuzzleMatriz(matrizA, matrizB, nivel, nivel * 10);
    }
}

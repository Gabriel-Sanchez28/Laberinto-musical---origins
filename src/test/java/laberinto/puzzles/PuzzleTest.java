package laberinto.puzzles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleTest {
    @Test
    public void probarPuzzleFracciones() {
        PuzzleFracciones puzzle = new PuzzleFracciones(
                1, 2,
                1, 4,
                "+",
                1,
                10
        );
        assertTrue(puzzle.verificarRespuesta("3/4"));
        assertFalse(puzzle.verificarRespuesta("1/2"));
    }

    @Test
    public void probarPuzzleMatrices() {
        int[][] A = {{1, 2}, {3, 4}};
        int[][] B = {{2, 1}, {1, 2}};
        PuzzleMatriz puzzle = new PuzzleMatriz(A, B, 2, 20);
        assertTrue(puzzle.verificarRespuesta("3,3;4,6"));
        assertFalse(puzzle.verificarRespuesta("1,2;3,4"));
    }

}
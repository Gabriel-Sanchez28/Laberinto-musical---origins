package laberinto.Laberintos;

import laberinto.puzzles.Puzzle;

public class Puerta {
    private Puzzle puzzle;
    private boolean abierta;

    public boolean isAbierta() {
        return abierta;
    }

    public boolean resolverPuzzle(Puzzle puzzle){
        if(puzzle==null){
            boolean resultado = true;
            return resultado;
        }else {
            boolean resultado = false;
            return resultado;
        }
    }

    public void abrir(){
        boolean abierta = isAbierta();
    }
}

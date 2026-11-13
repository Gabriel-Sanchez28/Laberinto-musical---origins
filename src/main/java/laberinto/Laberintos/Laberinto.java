package laberinto.Laberintos;

import java.util.Random;

public class Laberinto {
    private int[][] mapa;
    private int nivel;
    private int filas;
    private int columnas;

    public Laberinto(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.mapa = new int[filas][columnas];
        this.nivel = 1;
        generarNuevoMapa();
    }

    public void generarNuevoMapa() {
        Random random = new Random();
        //creacion de bordes
        for (int i = 0; i < filas; i++) { //filas
            for (int j = 0; j < columnas; j++) { //columnas
                if (i == 0 || i == filas - 1 || j == 0 || j == columnas - 1) {
                    mapa[i][j] = 1; //pared
                } else {mapa[i][j] = 0;} //espacio libre
            }
        }

        //obstacylos
        int cantidadObstaculos = nivel + 2;
        for (int i = 0; i < cantidadObstaculos; i++) {
            int fila = random.nextInt(filas - 2) + 1;
            int columna = random.nextInt(columnas - 2) + 1;
            if (fila != 1 && columna != columnas - 2) {
                mapa[fila][columna] = 1;
            }
        }
        //limpieza de corredor
        for (int j = 1; j < columnas - 1; j++) {mapa[1][j] = 0;}
        for (int i = 1; i < filas - 1; i++) {mapa[i][columnas - 2] = 0;}
        mapa[1][1] = 0; // inicio
        mapa[filas - 2][columnas - 2] = 3; // meta
    }

    public void siguienteNivel() {
        nivel++;
        generarNuevoMapa();
    }

    public int obtenerCelda(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {return 1;}
        return mapa[fila][columna];
    }

    public boolean esTransitable(int fila, int columna) {
        int celda = obtenerCelda(fila, columna);
        return celda == 0 || celda == 3;
    }

    public boolean esMeta(int fila, int columna) {
        return obtenerCelda(fila, columna) == 3;
    }

    public int obtenerFilas() {
        return filas;
    }

    public int obtenerColumnas() {
        return columnas;
    }

    public int obtenerNivel() {
        return nivel;
    }
}

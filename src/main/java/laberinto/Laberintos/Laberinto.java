package laberinto.Laberintos;

import java.util.*;

public class Laberinto {
    public static final int PISO = 0;
    public static final int PARED = 1;
    public static final int PUERTA = 2;
    public static final int META = 3;

    private int[][] mapa;
    private int nivel;
    private int filas;
    private int columnas;
    private ArrayList<Puerta> puertas;
    private final Random random = new Random();

    /** Las dimensiones deben ser impares para poder tallar pasillos; si no, se suma 1. */
    public Laberinto(int filas, int columnas) {
        if (filas < 5 || columnas < 5) {throw new IllegalArgumentException("El laberinto minimo es de 5x5.");}
        this.filas = (filas % 2 == 0) ? filas + 1 : filas;
        this.columnas = (columnas % 2 == 0) ? columnas + 1 : columnas;
        this.mapa = new int[this.filas][this.columnas];
        this.nivel = 1;
        this.puertas = new ArrayList<Puerta>();
        generarNuevoMapa();
    }

    public void generarNuevoMapa() {
        puertas.clear();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {mapa[i][j] = PARED;}
        }
        tallarPasillos();
        mapa[filas - 2][columnas - 2] = META;
        colocarPuertaDeLaMeta();
    }

    // Recursive backtracker (con pila): visita las celdas impares y abre el muro que hay entre ellas
    private void tallarPasillos() {
        int[][] saltos = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
        Deque<int[]> pila = new ArrayDeque<>();
        mapa[1][1] = PISO;
        pila.push(new int[]{1, 1});
        while (!pila.isEmpty()) {
            int[] actual = pila.peek();
            List<int[]> opciones = new ArrayList<>();
            for (int[] salto : saltos) {
                int f = actual[0] + salto[0];
                int c = actual[1] + salto[1];
                if (f >= 1 && f <= filas - 2 && c >= 1 && c <= columnas - 2 && mapa[f][c] == PARED) {
                    opciones.add(salto);
                }
            }
            if (opciones.isEmpty()) {
                pila.pop();
            } else {
                int[] salto = opciones.get(random.nextInt(opciones.size()));
                mapa[actual[0] + salto[0] / 2][actual[1] + salto[1] / 2] = PISO;
                mapa[actual[0] + salto[0]][actual[1] + salto[1]] = PISO;
                pila.push(new int[]{actual[0] + salto[0], actual[1] + salto[1]});
            }
        }
    }

    // La puerta va en la casilla anterior a la meta dentro del camino que lleva a ella
    private void colocarPuertaDeLaMeta() {
        List<int[]> camino = caminoAMeta(1, 1);
        int[] casilla = camino.get(camino.size() - 2);
        crearPuerta(casilla[0], casilla[1]);
    }

    private void crearPuerta(int fila, int columna) {
        Puerta puerta = new Puerta(fila, columna);
        puertas.add(puerta);
        mapa[fila][columna] = PUERTA;
    }

    /**
     * Camino mas corto desde una casilla hasta la meta (ambas incluidas), pasando por puertas
     * como si estuvieran abiertas. Devuelve una lista vacia si no hay camino.
     */
    public List<int[]> caminoAMeta(int filaOrigen, int columnaOrigen) {
        int[][] anterior = new int[filas * columnas][];
        boolean[][] visitada = new boolean[filas][columnas];
        Deque<int[]> cola = new ArrayDeque<>();
        int[][] pasos = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        if (obtenerCelda(filaOrigen, columnaOrigen) == PARED) {return new ArrayList<>();}
        cola.add(new int[]{filaOrigen, columnaOrigen});
        visitada[filaOrigen][columnaOrigen] = true;
        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            if (esMeta(actual[0], actual[1])) {
                List<int[]> camino = new ArrayList<>();
                int[] paso = actual;
                while (paso != null) {
                    camino.add(paso);
                    paso = anterior[paso[0] * columnas + paso[1]];
                }
                Collections.reverse(camino);
                return camino;
            }
            for (int[] d : pasos) {
                int f = actual[0] + d[0];
                int c = actual[1] + d[1];
                if (obtenerCelda(f, c) != PARED && !visitada[f][c]) {
                    visitada[f][c] = true;
                    anterior[f * columnas + c] = actual;
                    cola.add(new int[]{f, c});
                }
            }
        }
        return new ArrayList<>();
    }

    public Puerta obtenerPuerta(int fila, int columna) {
        for (Puerta puerta : puertas) {
            if (puerta.obtenerFila() == fila && puerta.obtenerColumna() == columna) {return puerta;}
        }
        return null;
    }

    public List<Puerta> obtenerPuertas() {return new ArrayList<>(puertas);}

    public boolean esPuerta(int fila, int columna) {return obtenerCelda(fila, columna) == PUERTA;}

    public boolean esPuertaCerrada(int fila, int columna) {
        Puerta puerta = obtenerPuerta(fila, columna);
        return puerta != null && !puerta.estaAbierta();
    }

    // La celda sigue marcada como puerta (para poder dibujarla abierta), pero deja de bloquear el paso
    public void abrirPuerta(int fila, int columna) {
        Puerta puerta = obtenerPuerta(fila, columna);
        if (puerta != null) {puerta.abrir();}
    }

    public void siguienteNivel() {
        nivel++;
        generarNuevoMapa();
    }

    public int obtenerCelda(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {return PARED;}
        return mapa[fila][columna];
    }

    public boolean esTransitable(int fila, int columna) {
        int celda = obtenerCelda(fila, columna);
        if (celda == PISO || celda == META) {return true;}
        if (celda == PUERTA) {return !esPuertaCerrada(fila, columna);}
        return false;
    }

    public boolean esMeta(int fila, int columna) {return obtenerCelda(fila, columna) == META;}
    public int obtenerFilas() {return filas;}
    public int obtenerColumnas() {return columnas;}
    public int obtenerNivel() {return nivel;}
    public boolean todasLasPuertasAbiertas() {
        for (Puerta puerta : puertas) {
            if (!puerta.estaAbierta()) {return false;}
        }
        return true;
    }

}

package laberinto.matematicas;

public class Matriz {
    private int[][] datos;
    private int filas;
    private int columnas;

    public Matriz(int[][] datos) {
        if (datos == null || datos.length == 0 || datos[0] == null || datos[0].length == 0) {
            throw new IllegalArgumentException("Matriz no valida.");
        }
        filas = datos.length;
        columnas = datos[0].length;
        this.datos = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            if (datos[i] == null || datos[i].length != columnas) {
                throw new IllegalArgumentException("Todas las filas deben tener el mismo numero de columnas.");
            }
            for (int j = 0; j < columnas; j++) {
                this.datos[i][j] = datos[i][j];
            }
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int obtenerElemento(int fila, int columna) {
        return datos[fila][columna];
    }

    public Matriz sumar(Matriz otra) {
        if (filas != otra.filas || columnas != otra.columnas) {
            throw new IllegalArgumentException("Las matrices deben tener las mismas dimensiones.");
        }
        int[][] resultado = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                resultado[i][j] = datos[i][j] + otra.datos[i][j];
            }
        }
        return new Matriz(resultado);
    }

    public Matriz restar(Matriz otra) {
        if (filas != otra.filas || columnas != otra.columnas) {
            throw new IllegalArgumentException("Las matrices deben tener las mismas dimensiones.");
        }
        int[][] resultado = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                resultado[i][j] = datos[i][j] - otra.datos[i][j];
            }
        }
        return new Matriz(resultado);
    }

    public Matriz multiplicar(Matriz otra) {
        if (columnas != otra.filas) {
            throw new IllegalArgumentException("Las dimensiones no permiten multiplicar las matrices.");
        }
        int[][] resultado = new int[filas][otra.columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < otra.columnas; j++) {
                for (int k = 0; k < columnas; k++) {
                    resultado[i][j] += datos[i][k] * otra.datos[k][j];
                }
            }
        }
        return new Matriz(resultado);
    }

    public int obtenerDeterminante() {
        if (filas != columnas) {
            throw new IllegalArgumentException("El determinante requiere una matriz cuadrada.");
        }
        return calcularDeterminante(datos);
    }

    private int calcularDeterminante(int[][] matriz) {
        int dimension = matriz.length;
        if (dimension == 1) {
            return matriz[0][0];
        }
        if (dimension == 2) {
            return matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0];
        }
        int determinante = 0;
        for (int j = 0; j < dimension; j++) {
            int[][] menor = new int[dimension - 1][dimension - 1];
            for (int fila = 1; fila < dimension; fila++) {
                int columnaMenor = 0;
                for (int columna = 0; columna < dimension; columna++) {
                    if (columna != j) {
                        menor[fila - 1][columnaMenor] = matriz[fila][columna];
                        columnaMenor++;
                    }
                }
            }
            int signo = (j % 2 == 0) ? 1 : -1;
            determinante += signo * matriz[0][j] * calcularDeterminante(menor);
        }
        return determinante;
    }

    @Override
    public String toString() {
        String texto = "";
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                texto += datos[i][j] + "\t";
            }
            texto += "\n";
        }
        return texto;
    }
}

package laberinto.Laberintos;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LaberintoApp extends JFrame {

    // Definimos las dimensiones del laberinto y el nivel actual
    private int filas = 10;
    private int columnas = 10;
    private int nivel = 1;
    private int[][] mapa = new int[filas][columnas];

    public LaberintoApp() {
        setTitle("Visualizador de Mapa / Laberinto");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. ¡IMPORTANTE! Generamos el mapa antes de pintar el panel
        generarNuevoMapa();

        // Panel personalizado para pintar la matriz
        JPanel panelMapa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int anchoCelda = getWidth() / columnas;
                int altoCelda = getHeight() / filas;

                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        // Pintar según el valor del mapa
                        if (mapa[i][j] == 1) {
                            g.setColor(new Color(40, 40, 40)); // Paredes (Gris oscuro)
                        } else if (mapa[i][j] == 3) {
                            g.setColor(new Color(46, 204, 113)); // Meta (Verde)
                        } else {
                            g.setColor(Color.WHITE); // Camino libre (Blanco)
                        }

                        // Rellenar la celda
                        g.fillRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda);

                        // Dibujar cuadrícula para que se distinga mejor
                        g.setColor(new Color(220, 220, 220));
                        g.drawRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda);
                    }
                }

                // Pintar el punto de inicio de un color especial (Azul) en [1][1]
                g.setColor(new Color(52, 152, 219));
                g.fillRect(1 * anchoCelda, 1 * altoCelda, anchoCelda, altoCelda);
                g.setColor(Color.BLACK);
                g.drawRect(1 * anchoCelda, 1 * altoCelda, anchoCelda, altoCelda);
            }
        };

        add(panelMapa);
    }

    // Tu método original para generar el mapa
    public void generarNuevoMapa() {
        Random random = new Random();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (i == 0 || i == filas - 1 || j == 0 || j == columnas - 1) {
                    mapa[i][j] = 1;
                } else {
                    mapa[i][j] = 0;
                }
            }
        }

        int cantidadObstaculos = nivel + 2;
        for (int i = 0; i < cantidadObstaculos; i++) {
            int fila = random.nextInt(filas - 2) + 1;
            int columna = random.nextInt(columnas - 2) + 1;
            if (fila != 1 && columna != columnas - 2) {
                mapa[fila][columna] = 1;
            }
        }
        for (int j = 1; j < columnas - 1; j++) { mapa[1][j] = 0; }
        for (int i = 1; i < filas - 1; i++) { mapa[i][columnas - 2] = 0; }
        mapa[1][1] = 0;
        mapa[filas - 2][columnas - 2] = 3;
    }

    // Getters necesarios por si necesitas usarlos en tus pruebas unitarias
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public int[][] getMapa() { return mapa; }

    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new LaberintoApp().setVisible(true);
        });
    }
}

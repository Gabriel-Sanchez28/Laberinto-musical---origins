package laberinto.juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

public class Control implements KeyListener {
    private final static int teclado = 256;
    private final boolean[] teclas = new boolean[teclado];

    private boolean arriba, abajo, izquierda, derecha;

    public boolean isArriba() {
        return arriba;
    }

    public boolean isAbajo() {
        return abajo;
    }

    public boolean isIzquierda() {
        return izquierda;
    }

    public boolean isDerecha() {
        return derecha;
    }

    public void actualizar() {
        arriba = teclas[KeyEvent.VK_W];
        abajo = teclas[KeyEvent.VK_S];
        izquierda = teclas[KeyEvent.VK_A];
        derecha = teclas[KeyEvent.VK_D];
    }

    //tecla tecleada xdd
    public void keyTyped(KeyEvent e) {

    }

    //tecla pulsada
    public void keyPressed(KeyEvent e) {
        teclas[e.getKeyCode()] = true;
    }

    //tecla liberada
    public void keyReleased(KeyEvent e) {
        teclas[e.getKeyCode()] = false;
    }

    static void main(String[] args) {
        JFrame ventana = new JFrame("Prueba de Control con Arreglo - Laberinto");
        ventana.setSize(600, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);

        PanelJuego panel = new PanelJuego();
        ventana.add(panel);

        ventana.setVisible(true);
    }
}

//probador grafico hecho con IA
class PanelJuego extends JPanel implements ActionListener {
    private Control control;
    private Timer timer;
    private int x = 275;
    private int y = 275;
    private final int velocidad = 4;

    public PanelJuego() {
        control = new Control();

        // Configuraciones de foco y eventos de teclado
        this.addKeyListener(control);
        this.setFocusable(true);
        this.setBackground(Color.DARK_GRAY);

        // Bucle de juego a ~60 FPS (cada 16ms)
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. CLAVE: Llamar a tu método actualizar para refrescar las teclas presionadas
        control.actualizar();

        // 2. Mover el círculo según los booleanos evaluados
        if (control.isArriba()) y -= velocidad;
        if (control.isAbajo()) y += velocidad;
        if (control.isIzquierda()) x -= velocidad;
        if (control.isDerecha()) x += velocidad;

        // Limitar dentro de la ventana
        if (x < 0) x = 0;
        if (x > getWidth() - 40) x = getWidth() - 40;
        if (y < 0) y = 0;
        if (y > getHeight() - 40) y = getHeight() - 40;

        // Redibujar la pantalla
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Usa W, A, S, D para verificar tu clase Control", 20, 30);

        // Dibujar el círculo controlable
        g.setColor(Color.RED);
        g.fillOval(x, y, 40, 40);
        g.setColor(Color.WHITE);
        g.drawOval(x, y, 40, 40);
    }
}


package laberinto.gui;

import laberinto.juego.Juego;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Ventana de la partida: contiene el PanelLaberinto. */
public class VentanaJuego extends JFrame {

    public VentanaJuego(Juego juego) {
        super("Laberinto musical - Origins");
        PanelLaberinto panel = new PanelLaberinto(juego);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        // el teclado solo funciona si el panel tiene el foco
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {panel.requestFocusInWindow();}
        });
    }
}

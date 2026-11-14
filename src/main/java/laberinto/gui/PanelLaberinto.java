package laberinto.gui;

import laberinto.Laberintos.Laberinto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelLaberinto extends JPanel implements ActionListener {
    // Tamaños (en pixeles)
    static final int celda = 56;
    static final int altoBarraSup = 64;
    private static final int celdasPortextura = 5;

    //Movimiento por casilla
    private static final double segundosCelda =0.18;

    //Tipos de celda
    private static final int pared = 1;
    private static final  int meta = 3;



    @Override
    public void actionPerformed(ActionEvent e) {


    }
}

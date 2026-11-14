package laberinto.gui;

import laberinto.Laberintos.Laberinto;
import laberinto.juego.Control;
import laberinto.juego.Juego;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static javax.swing.plaf.basic.DragRecognitionSupport.mousePressed;

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

    private final Juego juego;
    private final Control control = new Control();

    //FPS 16ms
    private final Timer timer = new Timer(16, this);

    //guardar en memoria
    private final BufferedImage texturaPiso;
    private final BufferedImage posFrente;
    private final BufferedImage posEspalda;
    private final BufferedImage posLado;

    // Capa estatica en memoria
    private BufferedImage capaMapa;
    private int nivelDibujado = -1; // nivel actual
    private int metaFila = -1;
    private int metaColumna = -1;

    // Estado de la animacion
    private boolean enMovimiento;
    private int origenFila;
    private int origenColumna;
    private double progreso;
    private Direccion mirada = Direccion.ABAJO;
    private long ultimoTick;
    private final long inicioNanos = System.nanoTime();

    // Cartel de nivel
    private String bannerTitulo = "";
    private String bannerSubtitulo = "";
    private double bannerRestante;

    public static BufferedImage cargarImagen(String ruta){
        try (InputStream lugar = PanelLaberinto.class.getResourceAsStream(ruta)) {
            if(lugar == null){ return null; }else{return ImageIO.read(lugar);}
        } catch (IOException e) {
            return null;
        }
    }

    public PanelLaberinto(Juego juego) {
        this.juego = juego;
        //cargar diseños
        BufferedImage piso = cargarImagen("/img/piso.png");
        this.posEspalda = cargarImagen("/img/jugador_espalda");
        this.posFrente = cargarImagen("/img/jugador_frente");
        this.posLado = cargarImagen("/img/jugador_lado");

        //dimensiones del laberinto
        Laberinto laberinto =juego.getLaberinto();
        setPreferredSize(new Dimension(laberinto.obtenerColumnas() * celda,
                laberinto.obtenerFilas() * celda + altoBarraSup));
        setBackground(Gui.FONDO_BORDE);

        setFocusable(true);
        addKeyListener(control);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        mostrarBanner("Nivel " + laberinto.obtenerNivel(), "WASD o flechas para moverte", 3.0);
    }

    private void mostrarBanner(String titulo, String subtitulo, double segundos) {
        bannerTitulo = titulo;
        bannerSubtitulo = subtitulo;
        bannerRestante = segundos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }
}

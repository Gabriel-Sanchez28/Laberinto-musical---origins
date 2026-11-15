package laberinto.gui;

import laberinto.Laberintos.Laberinto;
import laberinto.juego.Control;
import laberinto.juego.Juego;
import laberinto.juego.Jugador;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


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
    private final long inicioNanos = System.nanoTime(); //iniciador de tiempo en nano segundos

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

    //Dibujo de diseño
    public void paintComponent(Graphics g){
        super.paintComponent(g); //limpiar
        Graphics2D g2 = (Graphics2D) g.create();
        //Renderizado
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        //Optimizacion
        Laberinto laberinto = juego.getLaberinto();
        if (capaMapa == null || nivelDibujado != laberinto.obtenerNivel()) {
            reconstruirMapa();
        }
        //cronometro de animacion
        double segundos = (System.nanoTime() - inicioNanos) / 1e9;

        //ajuste espacio de juego
        g2.translate(0, altoBarraSup);
        g2.drawImage(capaMapa, 0, 0, null);
        if (metaFila >= 0) {dibujarMeta(g2, segundos);}
        dibujarJugador(g2);
        //ubicacion del mensaje de nivel
        dibujarBanner(g2, capaMapa.getWidth(), capaMapa.getHeight());
        g2.translate(0, -altoBarraSup);

        dibujarBarraSuperior(g2, getWidth());
        g2.dispose(); //liberar recursos
    }


    private void reconstruirMapa() {
        Laberinto laberinto = juego.getLaberinto();
        int filas = laberinto.obtenerFilas();
        int columnas = laberinto.obtenerColumnas();
        int ancho = columnas * celda;
        int alto = filas * celda;

        //lienzo
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //texturas no cargadas
        if (texturaPiso != null) {
            int lado = celdasPortextura * celda; //tamaño
            for (int y = 0; y < alto; y += lado) {
                for (int x = 0; x < ancho; x += lado) {
                    g.drawImage(texturaPiso, x, y, null);
                }
            }
        }else { //si hay error en la imagen
            g.setColor(new Color(0xE0B27A));
            g.fillRect(0, 0, ancho, alto);
        }

        //creacion de paredes y meta
        metaFila = -1;
        metaColumna = -1;
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                int celda = laberinto.obtenerCelda(fila, columna);
                int x = columna * celda; //traduce columnas en pixeles
                int y = fila * celda; //traduce filas en pixeles
                if (celda == pared) { //paredes o sombra
                    // sombra que la pared
                    if (laberinto.obtenerCelda(fila + 1, columna) != pared) {
                        g.setPaint(new GradientPaint(0, y + celda, new Color(0, 0, 0, 95),
                                0, y + celda + 12, new Color(0, 0, 0, 0)));
                        g.fillRect(x, y + celda, celda, 12);
                    }
                    dibujarPared(g, x, y);
                } else if (celda == meta) {
                    metaFila = fila;
                    metaColumna = columna;
                }
            }
        }
        g.dispose();//liberar recursos

        capaMapa = imagen; //mapa listo
        nivelDibujado = laberinto.obtenerNivel(); //actualizacion del nivel en el banner
    }

    private void dibujarPared(Graphics2D g, int x, int y) {
        int altoLadrillo = celda / 4;
        Color luz = new Color(0x6E4C4C);
        //lienzo
        g.setColor(Gui.FONDO_CENTRO);
        g.fillRect(x, y, celda, celda);

        //filas horizontales
        for (int i = 0; i < 4; i++) {
            int ladirlloY = y + i * altoLadrillo;
            g.setColor(luz);
            g.fillRect(x, ladirlloY + 2, celda, 1);
            g.setColor(Gui.FONDO_BORDE);
            g.fillRect(x, ladirlloY, celda, 2);
            //escalonado
            int desfase;
            if (i%2==0){
                desfase = 0;
            }else {
                desfase = celda/4;
            }
            for (int ladrilloX = x + desfase; ladrilloX < x + celda; ladrilloX += celda / 2) {
                g.fillRect(ladrilloX, ladirlloY, 2, altoLadrillo);
            }
        }
        // cara superior iluminada y base oscura
        g.setPaint(new GradientPaint(0, y, new Color(255, 220, 200, 70), 0, y + 14, new Color(255, 220, 200, 0)));
        g.fillRect(x, y, celda, 14);
        g.setColor(Gui.FONDO_BORDE);
        g.fillRect(x, y + celda - 3, celda, 3);
    }

    private void dibujarMeta(Graphics2D g, double segundos) {
        float cx = metaColumna * celda + celda / 2f;
        float cy = metaFila * celda + celda / 2f;
        //oscilacion
        float pulso = (float) (0.5 + 0.5 * Math.sin(segundos * 3));
        float radio = celda * 0.62f + pulso * 4f;

        //aura
        Color centro = new Color(Gui.GEMA_CLARA.getRed(), Gui.GEMA_CLARA.getGreen(), Gui.GEMA_CLARA.getBlue(), 210);
        Color borde = new Color(Gui.GEMA_CLARA.getRed(), Gui.GEMA_CLARA.getGreen(), Gui.GEMA_CLARA.getBlue(), 0);
        g.setPaint(new RadialGradientPaint(new Point2D.Float(cx, cy), radio,
                new float[]{0f, 1f}, new Color[]{centro, borde}));
        g.fill(new Ellipse2D.Float(cx - radio, cy - radio, radio * 2, radio * 2));

        g.setColor(Gui.GEMA_CLARA);
        g.fill(rombo(cx, cy, 12 + pulso * 2));
        g.setColor(Gui.GEMA_OSCURA);
        g.setStroke(new BasicStroke(2f));
        g.draw(rombo(cx, cy, 12 + pulso * 2));
        g.setColor(Gui.CREMA);
        g.fill(rombo(cx, cy, 5));
    }

    //creacion del icono de diamante
    private static Path2D.Float rombo(float cx, float cy, float r) {
        Path2D.Float p = new Path2D.Float();
        p.moveTo(cx, cy - r);
        p.lineTo(cx + r * 0.75f, cy);
        p.lineTo(cx, cy + r);
        p.lineTo(cx - r * 0.75f, cy);
        p.closePath();
        return p;
    }

    private void dibujarJugador(Graphics2D g) {
        //Posicion de jugador
        Jugador jugador = juego.getJugador();
        double fila = jugador.getFila();
        double columna = jugador.getColumna();

        //movimiento del jugador
        if (enMovimiento) {
            fila = origenFila + (fila - origenFila) * progreso;
            columna = origenColumna + (columna - origenColumna) * progreso;
        }

        //centrado en la casilla
        double centroX = columna * celda + celda / 2.0;
        double pies = fila * celda + celda - 6;
        double salto = enMovimiento ? Math.abs(Math.sin(progreso * Math.PI)) * 3 : 0; //salto al caminar

        //sombra
        g.setColor(new Color(0, 0, 0, 70));
        g.fill(new Ellipse2D.Double(centroX - 13, pies - 5, 26, 9));

        BufferedImage sprite = segunMirada();
        if (sprite != null) {
            int alto = (int) (celda * 1.1); // todas las vistas se dibujan a la misma altura
            int ancho = Math.round(alto * sprite.getWidth() / (float) sprite.getHeight());
            int x = (int) Math.round(centroX - ancho / 2.0);
            int y = (int) Math.round(pies - alto - salto) + 2;
            if (mirada == Direccion.DERECHA) {
                g.drawImage(sprite, x + ancho, y, -ancho, alto, null); // ancho negativo = espejo
            } else {
                g.drawImage(sprite, x, y, ancho, alto, null);
            }
        }  else { // circulo de emergencia para poder seguir probando
            g.setColor(Gui.ROJO_CLARO);
            g.fill(new Ellipse2D.Double(centroX - 14, pies - 34 - salto, 28, 34));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }
}

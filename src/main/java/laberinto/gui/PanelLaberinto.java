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

    // Carpeta de las imagenes dentro de src/main/resources ("/" = directamente en resources)
    private static final String rutaImagenes = "/";

    // Los PNG del personaje son lienzos de 500x500 con margen transparente. Estas medidas (como fraccion del
    // alto del lienzo) sirven para escalar las 3 vistas por igual y apoyar las botas en el suelo.
    private static final double personajeAlto = 0.76; // de la cabeza a las botas
    private static final double piesAltura = 0.886;   // fila donde terminan las botas

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
            if(lugar == null){
                System.err.println("No se encontro la imagen " + ruta + " (revisa src/main/resources)");
                return null;
            }
            return ImageIO.read(lugar);
        } catch (IOException e) {
            System.err.println("No se pudo leer la imagen " + ruta + ": " + e.getMessage());
            return null;
        }
    }

    //escala un sprite una sola vez (al cargar) para dibujarlo luego 1 a 1, sin deformarlo
    private static BufferedImage prepararSprite(BufferedImage original) {
        if (original == null) {return null;}
        double escala = (celda * 1.1) / (original.getHeight() * personajeAlto);
        int ancho = (int) Math.round(original.getWidth() * escala);
        int alto = (int) Math.round(original.getHeight() * escala);
        return escalarSuave(original, ancho, alto);
    }

    public PanelLaberinto(Juego juego) {
        this.juego = juego;
        //cargar diseños
        BufferedImage piso = cargarImagen(rutaImagenes + "piso.png");
        if (piso == null){
            this.texturaPiso = null;
        }else {
            this.texturaPiso = escalarSuave(piso, celdasPortextura * celda, celdasPortextura * celda);
        }
        this.posEspalda = prepararSprite(cargarImagen(rutaImagenes + "jugador_espalda.png"));
        this.posFrente = prepararSprite(cargarImagen(rutaImagenes + "jugador_frente.png"));
        this.posLado = prepararSprite(cargarImagen(rutaImagenes + "jugador_lado.png"));

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
        mostrarBanner("Nivel " + laberinto.obtenerNivel(), "WASD para moverte", 3.0);
    }

    public void addNotify() {
        super.addNotify();
        ultimoTick = System.nanoTime();
        timer.start();
        requestFocusInWindow();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long ahora = System.nanoTime();
        double dt = Math.min((ahora - ultimoTick) / 1e9, 0.05); // evita saltos si la ventana se congela
        ultimoTick = ahora;
        actualizar(dt);
        repaint();
    }

    private void actualizar(double dt) {
        control.actualizar();
        if (bannerRestante > 0) {
            bannerRestante -= dt;
        }

        if (enMovimiento) {
            progreso += dt / segundosCelda;
            if (progreso >= 1) {
                enMovimiento = false;
                progreso = 0;
                if (juego.jugadorEnMeta()) {
                    // Provisional: cuando existan los puzzles, aqui debe pasar al estado PUZZLE
                    //juego.avanzarNivel();
                    mostrarBanner("Nivel " + juego.getLaberinto().obtenerNivel(), "", 1.4);
                }
            }
        }

        //si no esta caminando, leer las teclas e intentar dar el siguiente paso
        if (!enMovimiento) {
            int deltaFila = 0;
            int deltaColumna = 0;
            if (control.isArriba()) {deltaFila = -1;}
            else if (control.isAbajo()) {deltaFila = 1;}
            else if (control.isIzquierda()) {deltaColumna = -1;}
            else if (control.isDerecha()) {deltaColumna = 1;}

            if (deltaFila != 0 || deltaColumna != 0) {
                //gira hacia donde se pulso, aunque haya una pared delante
                if (deltaFila < 0) {mirada = Direccion.ARRIBA;}
                else if (deltaFila > 0) {mirada = Direccion.ABAJO;}
                else if (deltaColumna < 0) {mirada = Direccion.IZQUIERDA;}
                else {mirada = Direccion.DERECHA;}

                Jugador jugador = juego.getJugador();
                int filaAnterior = jugador.getFila();
                int columnaAnterior = jugador.getColumna();
                if (juego.intentarMover(deltaFila, deltaColumna)) {
                    origenFila = filaAnterior;
                    origenColumna = columnaAnterior;
                    progreso = 0;
                    enMovimiento = true;
                }
            }
        }
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

    private void dibujarBarraSuperior(Graphics2D g, int ancho) {
        Jugador jugador = juego.getJugador();

        g.setPaint(new GradientPaint(0, 0, Gui.FONDO_CENTRO, 0, altoBarraSup, Gui.FONDO_BORDE));
        g.fillRect(0, 0, ancho, altoBarraSup);
        g.setColor(Gui.COBRE_CLARO);
        g.fillRect(0, altoBarraSup - 3, ancho, 3);

        // fila 1: nombre y nivel
        g.setFont(Gui.FUENTE_ETIQUETA);
        g.setColor(Gui.SALMON);
        g.drawString(jugador.getNombre(), 14, 26);
        String nivel = "Nivel " + juego.getLaberinto().obtenerNivel();
        g.drawString(nivel, ancho - 14 - g.getFontMetrics().stringWidth(nivel), 26);

        // fila 2: vidas y estadisticas
        int casillas = Math.max(3, jugador.getVida());
        for (int i = 0; i < casillas; i++) {
            dibujarCorazon(g, 14 + i * 26, 34, 20, i < jugador.getVida());
        }
        String estadisticas = "Puntos " + jugador.getPuntuacion()
                + "   Racha " + jugador.getRachaSinErrores()
                + "   Fragmentos " + jugador.getTareasCompletadas();
        g.setFont(Gui.FUENTE_VALOR.deriveFont(14f));
        g.setColor(Gui.CREMA);
        g.drawString(estadisticas, ancho - 14 - g.getFontMetrics().stringWidth(estadisticas), 50);
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
                int tipo = laberinto.obtenerCelda(fila, columna); //0 piso, 1 pared, 3 meta
                int x = columna * celda; //traduce columnas en pixeles
                int y = fila * celda; //traduce filas en pixeles
                if (tipo == pared) { //paredes o sombra
                    // sombra que la pared
                    if (laberinto.obtenerCelda(fila + 1, columna) != pared) {
                        g.setPaint(new GradientPaint(0, y + celda, new Color(0, 0, 0, 95),
                                0, y + celda + 12, new Color(0, 0, 0, 0)));
                        g.fillRect(x, y + celda, celda, 12);
                    }
                    dibujarPared(g, x, y);
                } else if (tipo == meta) {
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
            // el sprite ya viene escalado (prepararSprite): se dibuja 1 a 1 con las botas sobre "pies"
            int ancho = sprite.getWidth();
            int alto = sprite.getHeight();
            int x = (int) Math.round(centroX - ancho / 2.0);
            int y = (int) Math.round(pies - salto - alto * piesAltura) + 2;
            if (mirada == Direccion.IZQUIERDA) { // el PNG de lado mira a la derecha; para la izquierda se espeja
                g.drawImage(sprite, x + ancho, y, -ancho, alto, null); // ancho negativo = espejo
            } else {
                g.drawImage(sprite, x, y, ancho, alto, null);
            }
        }  else { // circulo de emergencia para poder seguir probando
            g.setColor(Gui.ROJO_CLARO);
            g.fill(new Ellipse2D.Double(centroX - 14, pies - 34 - salto, 28, 34));
        }
    }
    //ajuste de mirada de personaje
    private BufferedImage segunMirada() {
        BufferedImage posicion = switch (mirada) {
            case ABAJO -> posFrente;
            case ARRIBA -> posEspalda;
            case IZQUIERDA, DERECHA -> posLado;
        };
        if (posicion != null){
            return posicion;
        }else {
            return posLado;
        }
    }

    private void dibujarBanner(Graphics2D g, int ancho, int alto) {
        if (bannerRestante <= 0) {return;}
        float opacidad = (float) Math.min(1.0, bannerRestante / 0.5);

        int cajaAncho = 340;
        int cajaAlto = bannerSubtitulo.isEmpty() ? 64 : 92;
        int x = (ancho - cajaAncho) / 2;
        int y = (alto - cajaAlto) / 2;

        Composite anterior = g.getComposite();
        g.setComposite(AlphaComposite.SrcOver.derive(opacidad));
        g.setColor(new Color(Gui.FONDO_BORDE.getRed(), Gui.FONDO_BORDE.getGreen(), Gui.FONDO_BORDE.getBlue(), 225));
        g.fillRoundRect(x, y, cajaAncho, cajaAlto, 22, 22);
        g.setColor(Gui.COBRE_CLARO);
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(x, y, cajaAncho, cajaAlto, 22, 22);

        g.setFont(Gui.FUENTE_TITULO);
        g.setColor(Gui.CREMA);
        centrarTexto(g, bannerTitulo, ancho / 2, y + 42);
        if (!bannerSubtitulo.isEmpty()) {
            g.setFont(Gui.FUENTE_VALOR.deriveFont(15f));
            g.setColor(Gui.SALMON);
            centrarTexto(g, bannerSubtitulo, ancho / 2, y + 72);
        }
        g.setComposite(anterior);
    }

    private static void centrarTexto(Graphics2D g, String texto, int centroX, int baseY) {
        g.drawString(texto, centroX - g.getFontMetrics().stringWidth(texto) / 2, baseY);
    }
    //vidas
    private static void dibujarCorazon(Graphics2D g, float x, float y, float s, boolean lleno) {
        Path2D.Float corazon = new Path2D.Float();
        corazon.moveTo(x + s / 2, y + s);
        corazon.curveTo(x - s * 0.25f, y + s * 0.55f, x + s * 0.05f, y - s * 0.15f, x + s / 2, y + s * 0.28f);
        corazon.curveTo(x + s * 0.95f, y - s * 0.15f, x + s * 1.25f, y + s * 0.55f, x + s / 2, y + s);
        corazon.closePath();
        if (lleno) {
            g.setColor(Gui.ROJO_CLARO);
            g.fill(corazon);
        }
        g.setColor(lleno ? Gui.ROJO_OSCURO : Gui.COBRE_OSCURO);
        g.setStroke(new BasicStroke(1.6f));
        g.draw(corazon);
    }
    //escalar imagenes
    private static BufferedImage escalarSuave(BufferedImage origen, int ancho, int alto) {
        BufferedImage actual = origen;
        int w = origen.getWidth();
        int h = origen.getHeight();
        while (w / 2 >= ancho && h / 2 >= alto) {
            w /= 2;
            h /= 2;
            actual = escalarUnPaso(actual, w, h);
        }
        return escalarUnPaso(actual, ancho, alto);
    }

    //cambiar tamaño
    private static BufferedImage escalarUnPaso(BufferedImage origen, int ancho, int alto) {
        BufferedImage destino = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB); //lienzo
        Graphics2D g = destino.createGraphics(); //pincel
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); //una sola vez
        g.drawImage(origen, 0, 0, ancho, alto, null);
        g.dispose();
        return destino;
    }

}

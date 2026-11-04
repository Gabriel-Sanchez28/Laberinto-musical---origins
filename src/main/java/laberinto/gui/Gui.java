package laberinto.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class Gui extends JFrame {

    // Colores
    static final Color FONDO_CENTRO  = new Color(0x4A3535);
    static final Color FONDO_BORDE   = new Color(0x231616);
    static final Color ROJO_CLARO    = new Color(0xEE6C55);
    static final Color ROJO_OSCURO   = new Color(0xAE3426);
    static final Color COBRE_CLARO   = new Color(0xF29A84);
    static final Color COBRE_OSCURO  = new Color(0x7A2117);
    static final Color GEMA_CLARA    = new Color(0x8CF7A8);
    static final Color GEMA_OSCURA   = new Color(0x0F6B30);
    static final Color CREMA         = new Color(0xFFF1E4);
    static final Color SALMON        = new Color(0xFFB4A0);

    // Fuentes
    static final Font FUENTE_TITULO   = new Font("Georgia", Font.BOLD, 26);
    static final Font FUENTE_ETIQUETA = new Font("Georgia", Font.BOLD, 17);
    static final Font FUENTE_VALOR    = new Font("Georgia", Font.PLAIN, 17);
    static final Font FUENTE_BOTON    = new Font("Georgia", Font.BOLD, 18);

    // Componentes
    private JPanel principal;
    private JPanel titulo;
    private JPanel tabla;
    private JPanel panelBotones;

    private JLabel nombre;
    private JTextField textField1; // campo del nombre
    private JLabel vidas;
    private JLabel vidasValor; // muestra el número de vidas
    private JLabel puntuacion;// valor de puntuacion
    private JLabel puntuacionValor;
    private JLabel rachaSinErrores;
    private JLabel rachaValor; //racha sin errores
    private JLabel tareasComp;
    private JLabel tareasValor; //valor de tareas completas
    private JLabel obraSelec;
    private JLabel obraValor; //obra seleccionada

    private JButton buttonComenzar;
    private JButton buttonSalida;


    public Gui() {
        configuracion();
    }

    //crear componentes
    private void createUIComponents() {
        principal = new PanelFondo();
        titulo = new TituloOrnamental("LABERINTO MUSICAL - ORIGINS");
        tabla = new PanelTabla();
        buttonComenzar = new BotonOrnamental("COMENZAR");
        buttonSalida = new BotonOrnamental("SALIR");
    }

    //configuracion de la ventana
    private void configuracion() {
        setTitle("Laberinto musical - Origins");
        titulo.setBorder(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(principal);
        estilizarComponentes();
        setSize(660, 530);
        setResizable(false);
        setLocationRelativeTo(null);

        buttonComenzar.addActionListener(e -> comenzar()); //ajustar al metodo para iniciar el juego
        buttonSalida.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Gracias por jugar",
                    "Salir", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            System.exit(0);
        });
    }

    // Colores, fuentes y márgenes de componentes
    private void estilizarComponentes() {
        principal.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));
        tabla.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));
        panelBotones.setOpaque(false);

        for (JLabel etiqueta : new JLabel[]{nombre, vidas, puntuacion, rachaSinErrores, tareasComp, obraSelec}) {
            etiqueta.setFont(FUENTE_ETIQUETA);
            etiqueta.setForeground(SALMON);
        }
        for (JLabel valor : new JLabel[]{vidasValor, puntuacionValor, rachaValor, tareasValor, obraValor}) {
            valor.setFont(FUENTE_VALOR);
            valor.setForeground(CREMA);
        }
        estilizarCampo(textField1);

        //ajustar para que sean cambiantes con un metodo de verificacion
        vidasValor.setText("3");
        puntuacionValor.setText("0");
        rachaValor.setText("0");
        tareasValor.setText("0 / 0");
        obraValor.setText("Ninguna");
    }

    //estilo campo de nombre
    private static void estilizarCampo(JTextField campo) {
        campo.setFont(FUENTE_VALOR);
        campo.setForeground(CREMA);
        campo.setCaretColor(COBRE_CLARO);
        campo.setBackground(new Color(0x1E1111));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COBRE_CLARO, 2),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
    }

    // Acciones
    private void comenzar() {
        String nombre = getNombre();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "!Escribe tu nombre para comenzar¡",
                    "Falta el nombre", JOptionPane.WARNING_MESSAGE);
            textField1.requestFocusInWindow();
        }
        //hacer ajustes para que dirija al juego
    }

    // Métodos para actualizar los valores desde el resto del jueg (pruebas no finales)
    public String getNombre()                    { return textField1.getText().trim(); }
    public void setVidas(int vidas)              { vidasValor.setText(String.valueOf(vidas)); }
    public void setPuntuacion(int puntos)        { puntuacionValor.setText(String.valueOf(puntos)); }
    public void setRacha(int racha)              { rachaValor.setText(String.valueOf(racha)); }
    public void setTareas(int hechas, int total) { tareasValor.setText(hechas + " / " + total); }
    public void setObraSeleccionada(String obra) { obraValor.setText(obra); }

     static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gui().setVisible(true));
    }

    // =====================================================================
    //  Dibujo de la banda ornamental (la usan el título y los botones)
    // =====================================================================

    private static void dibujarBanda(Graphics2D g2, int w, int h, Color arriba, Color abajo,
                                     boolean activo, String texto, Font fuente, Color colorTexto) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        double cy = h / 2.0;
        double radio = h / 2.0 - 1.5;       // tamaño de la estrella de cada extremo
        double cx = radio + 1.5;            // centro del adorno izquierdo
        double bw = w - 2 * cx;             // ancho del cuerpo rojo
        double arco = (h - 10) * 0.7;

        Color cobreClaro  = activo ? COBRE_CLARO  : new Color(0x9A8A84);
        Color cobreOscuro = activo ? COBRE_OSCURO : new Color(0x4A3C38);

        // --- Cuerpo rojo ---
        RoundRectangle2D cuerpo = new RoundRectangle2D.Double(cx, 5, bw, h - 10, arco, arco);
        g2.setPaint(new GradientPaint(0, 5, arriba, 0, h - 5, abajo));
        g2.fill(cuerpo);

        Shape clipAnterior = g2.getClip();
        g2.clip(cuerpo);
        // brillo superior
        g2.setPaint(new GradientPaint(0, 5, new Color(255, 255, 255, 90),
                0, (float) (h * 0.55), new Color(255, 255, 255, 0)));
        g2.fillRect((int) cx, 5, (int) bw, h);
        // reflejo diagonal
        double x0 = cx + bw * 0.16, skew = h * 0.5, ancho = bw * 0.26;
        Path2D.Double reflejo = new Path2D.Double();
        reflejo.moveTo(x0 + skew, 5);
        reflejo.lineTo(x0 + skew + ancho, 5);
        reflejo.lineTo(x0 + ancho, h - 5);
        reflejo.lineTo(x0, h - 5);
        reflejo.closePath();
        g2.setColor(new Color(255, 255, 255, 38));
        g2.fill(reflejo);
        // franja inferior más oscura
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRect((int) cx, (int) (h - 5 - h * 0.17), (int) bw, h);
        g2.setClip(clipAnterior);

        // --- Borde de cobre y línea interior ---
        g2.setPaint(new GradientPaint(0, 0, cobreClaro, 0, h, cobreOscuro));
        g2.setStroke(new BasicStroke(3f));
        g2.draw(cuerpo);
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(255, 230, 215, 70));
        g2.draw(new RoundRectangle2D.Double(cx + 3, 8, bw - 6, h - 16, arco * 0.8, arco * 0.8));

        // --- Adornos con gema en los extremos ---
        dibujarAdorno(g2, cx, cy, radio, activo);
        dibujarAdorno(g2, w - cx, cy, radio, activo);

        // --- Texto (se reduce solo si no cabe) ---
        Font f = fuente;
        FontMetrics fm = g2.getFontMetrics(f);
        while (fm.stringWidth(texto) > bw - 40 && f.getSize() > 10) {
            f = f.deriveFont((float) (f.getSize() - 1));
            fm = g2.getFontMetrics(f);
        }
        g2.setFont(f);
        int x = (w - fm.stringWidth(texto)) / 2;
        int y = (int) ((h - fm.getHeight()) / 2.0) + fm.getAscent();
        g2.setColor(new Color(60, 8, 4, 190));
        g2.drawString(texto, x + 1, y + 2);
        g2.setColor(colorTexto);
        g2.drawString(texto, x, y);
    }

    // Dibujo de Estrella
    private static void dibujarAdorno(Graphics2D g2, double cx, double cy, double radio, boolean activo) {
        double interior = radio * 0.52;
        Path2D.Double estrella = new Path2D.Double();
        for (int i = 0; i < 8; i++) {
            double ang = Math.PI / 4 * i - Math.PI / 2;
            double r = (i % 2 == 0) ? radio : interior;
            double px = cx + Math.cos(ang) * r, py = cy + Math.sin(ang) * r;
            if (i == 0) estrella.moveTo(px, py); else estrella.lineTo(px, py);
        }
        estrella.closePath();

        Color claro  = activo ? COBRE_CLARO  : new Color(0x9A8A84);
        Color oscuro = activo ? COBRE_OSCURO : new Color(0x4A3C38);
        g2.setPaint(new GradientPaint((float) (cx - radio), (float) (cy - radio), claro,
                (float) (cx + radio), (float) (cy + radio), oscuro));
        g2.fill(estrella);
        g2.setColor(oscuro);
        g2.setStroke(new BasicStroke(1.4f));
        g2.draw(estrella);

        // marco oscuro y gema
        g2.setColor(oscuro);
        g2.fill(rombo(cx, cy, radio * 0.50));
        Path2D.Double gema = rombo(cx, cy, radio * 0.42);
        Color gClara  = activo ? GEMA_CLARA  : new Color(0xB5ABA7);
        Color gOscura = activo ? GEMA_OSCURA : new Color(0x5E5450);
        g2.setPaint(new GradientPaint((float) (cx - radio * 0.4), (float) (cy - radio * 0.4), gClara,
                (float) (cx + radio * 0.4), (float) (cy + radio * 0.4), gOscura));
        g2.fill(gema);
        // destello
        Path2D.Double brillo = new Path2D.Double();
        brillo.moveTo(cx - radio * 0.30, cy);
        brillo.lineTo(cx, cy - radio * 0.30);
        brillo.lineTo(cx, cy - radio * 0.05);
        brillo.lineTo(cx - radio * 0.12, cy);
        brillo.closePath();
        g2.setColor(new Color(255, 255, 255, 140));
        g2.fill(brillo);
    }

    private static Path2D.Double rombo(double cx, double cy, double r) {
        Path2D.Double p = new Path2D.Double();
        p.moveTo(cx, cy - r);
        p.lineTo(cx + r, cy);
        p.lineTo(cx, cy + r);
        p.lineTo(cx - r, cy);
        p.closePath();
        return p;
    }

    // =====================================================================
    //  Componentes dibujados
    // =====================================================================

    /** Fondo marrón oscuro. */
    private static class PanelFondo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            float radio = Math.max(getWidth(), getHeight()) * 0.75f;
            g2.setPaint(new RadialGradientPaint(
                    new Point2D.Float(getWidth() / 2f, getHeight() / 2f), radio,
                    new float[]{0f, 1f}, new Color[]{FONDO_CENTRO, FONDO_BORDE}));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    /** Panel oscuro semitransparente con borde de cobre redondeado. */
    private static class PanelTabla extends JPanel {
        PanelTabla() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(20, 8, 8, 185));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
            g2.setPaint(new GradientPaint(0, 0, COBRE_CLARO, 0, getHeight(), COBRE_OSCURO));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 24, 24);
            g2.dispose();
        }
    }

    /** Banner rojo con gemas para el título. */
    private static class TituloOrnamental extends JPanel {
        private final String texto;

        TituloOrnamental(String texto) {
            this.texto = texto;
            setOpaque(false);
            setPreferredSize(new Dimension(100, 62));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            dibujarBanda(g2, getWidth(), getHeight(), ROJO_CLARO, ROJO_OSCURO,
                    true, texto, FUENTE_TITULO, CREMA);
            g2.dispose();
        }
    }

    /** Botón rojo brillante con adornos y gemas; cambia al pasar el mouse y al pulsarlo. */
    private static class BotonOrnamental extends JButton {
        BotonOrnamental(String texto) {
            super(texto);
            setFont(FUENTE_BOTON);
            setPreferredSize(new Dimension(200, 52));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            ButtonModel m = getModel();

            Color arriba = ROJO_CLARO, abajo = ROJO_OSCURO, texto = CREMA;
            if (!isEnabled()) {
                arriba = new Color(0x8C7A76); abajo = new Color(0x4F403D); texto = new Color(0xCFC3BE);
            } else if (m.isPressed()) {
                arriba = ROJO_OSCURO; abajo = ROJO_OSCURO.darker();
            } else if (m.isRollover()) {
                arriba = ROJO_CLARO.brighter(); abajo = ROJO_CLARO.darker();
            }
            dibujarBanda(g2, getWidth(), getHeight(), arriba, abajo,
                    isEnabled(), getText(), getFont(), texto);
            g2.dispose();
        }
    }
}

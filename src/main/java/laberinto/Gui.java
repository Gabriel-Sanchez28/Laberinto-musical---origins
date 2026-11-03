package laberinto;

import javax.swing.*;

public class Gui extends JFrame {
    private JPanel Principal;
    private JTextField textField1;
    private JLabel Nombre;
    private JLabel Vidas;
    private JLabel Puntuacion;
    private JLabel RachaSinErrores;
    private JLabel TareasComp;
    private JLabel Obraselec;
    private JButton buttoncomenzar;
    private JButton buttonSalir;

    public Gui() {
        configuracion();
    }

    public void configuracion() {
        setTitle("Laberinto musical - Origins");
        setContentPane(Principal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        buttonSalir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Gracias por jugar",
                    "Salir", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            System.exit(0);
        });
    }

    static void main() {
        Gui inicio = new Gui();
        inicio.setVisible(true);
    }
}

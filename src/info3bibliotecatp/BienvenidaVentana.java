/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package info3bibliotecatp;

import javax.swing.*;
import java.awt.*;

public class BienvenidaVentana extends JFrame {

    public BienvenidaVentana() {
        setTitle("Bienvenido a Bookish");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        FondoPanel fondoPanel = new FondoPanel("/info3bibliotecatp/Imagenes/bookish (2).png");
        fondoPanel.setLayout(new BorderLayout());

        JButton irABookish = new JButton("Ir a Bookish");
        irABookish.setBackground(Color.white);
        irABookish.setForeground(new Color(85, 34, 0));
        irABookish.setFont(new Font("Serif", Font.BOLD, 18));
        irABookish.addActionListener(evt -> {
            UsuarioIniciar ventana = new UsuarioIniciar();
            ventana.setVisible(true);
            this.dispose();
        });

        fondoPanel.add(irABookish, BorderLayout.SOUTH);
        setContentPane(fondoPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BienvenidaVentana().setVisible(true));
    }

    // Clase interna para fondo escalable
    class FondoPanel extends JPanel {
        private Image imagen;

        public FondoPanel(String rutaImagen) {
            ImageIcon icon = new ImageIcon(getClass().getResource(rutaImagen));
            this.imagen = icon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

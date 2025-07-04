/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package info3bibliotecatp;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 *
 * @author MSI
 */
public class UsuarioIniciar extends javax.swing.JFrame {

    /**
     * Creates new form UsuarioIniciar
     */
    public UsuarioIniciar() {
        setTitle("Bookish - Iniciar sesión");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        FondoPanel fondoPanel = new FondoPanel();
        fondoPanel.setLayout(new BorderLayout());
        setContentPane(fondoPanel);  // Fondo principal

        // Panel izquierdo (solo para ocupar espacio, vacío)
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false); 

        // Panel derecho (formulario)
        JPanel panelDerecho = new JPanel();
        panelDerecho.setOpaque(false);
        panelDerecho.setLayout(new GridBagLayout()); // centrar mejor los elementos

        // agregar los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado

        JLabel lblUsuario = new JLabel("Usuario");
        JTextField txtUsuario = new JTextField(15);
        JLabel lblContrasena = new JLabel("Contraseña");
        JPasswordField txtContrasena = new JPasswordField(15);
        JButton btnIniciar = new JButton("Iniciar Sesión");
        JButton btnRegistrar = new JButton("Registrarse");
        JLabel lblPregunta = new JLabel("¿Aún no tienes una cuenta?");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelDerecho.add(lblUsuario, gbc);
        gbc.gridy++;
        panelDerecho.add(lblContrasena, gbc);
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelDerecho.add(btnIniciar, gbc);
        gbc.gridy++;
        panelDerecho.add(lblPregunta, gbc);
        gbc.gridy++;
        panelDerecho.add(btnRegistrar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelDerecho.add(txtUsuario, gbc);
        gbc.gridy++;
        panelDerecho.add(txtContrasena, gbc);

        fondoPanel.add(panelIzquierdo, BorderLayout.CENTER);
        fondoPanel.add(panelDerecho, BorderLayout.EAST);
        
        btnIniciar.addActionListener(e -> iniciarSesion(txtUsuario.getText(), new String (txtContrasena.getPassword())));
        btnRegistrar.addActionListener (e -> registrarUsuario(txtUsuario.getText(), new String (txtContrasena.getPassword())));
    }

    class FondoPanel extends JPanel {
        private Image imagen = new ImageIcon(getClass().getResource("/info3bibliotecatp/Imagenes/bookish (5).png")).getImage();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
        private boolean iniciarSesion(String nombreUsuario, String contrasena) {
        Connection conn = Conexion.conectar();
        if (conn != null) {
            try {
                String sql = "SELECT id, es_admin FROM Usuarios WHERE nombre_usuario = ? AND \"contraseña\" = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombreUsuario.trim());
                stmt.setString(2, contrasena.trim());
                System.out.println("Usuario ingresado: '" + nombreUsuario + "'");
                System.out.println("Contraseña ingresada: '" + contrasena + "'");

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int idUsuario = rs.getInt("id");
                    boolean esAdmin = rs.getBoolean("es_admin");
                    
                    JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.");
                    
                    BibliotecaVentana ventana = new BibliotecaVentana(nombreUsuario,idUsuario,esAdmin);
                    ventana.setVisible(true);
                    this.dispose();
                    
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Nombre de usuario o contraseña incorrectos.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + e.getMessage());
            }
        }
        return false;
    }
            private void registrarUsuario(String usuario, String contrasena) {
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completá ambos campos.");
            return;
        }

        boolean registrado = GestionUsuarios.registrarUsuario(usuario, contrasena);
        if (registrado) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito!");
        } else {
            JOptionPane.showMessageDialog(this, "Ese usuario ya existe, elija otro.");
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UsuarioIniciar().setVisible(true));
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info3bibliotecatp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author MSI
 */
public class GestionUsuarios {

    public static boolean registrarUsuario(String nombre, String contrasena) {
        Connection conn = Conexion.conectar();
        if (conn != null) {
            try {
                // Verificar si el usuario ya existe
                String consulta = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
                PreparedStatement checkStmt = conn.prepareStatement(consulta);
                checkStmt.setString(1, nombre);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return false; // Ya existe
                }
                String contrasenaEncriptada = encriptarSHA256(contrasena);

                // Insertar nuevo usuario
                String sql = "INSERT INTO usuarios (nombre_usuario, \"contraseña\") VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombre);
                 stmt.setString(2, contrasenaEncriptada);
                stmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                System.out.println("Error al registrar usuario: " + e.getMessage());
            }
        }
        return false;
    }
    
    public static String encriptarSHA256(String contrasena) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(contrasena.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }
}
}

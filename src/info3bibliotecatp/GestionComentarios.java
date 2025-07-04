/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info3bibliotecatp;

/**
 *
 * @author MSI
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class GestionComentarios {

    //agregar comentariuo a base de datitos
    public static boolean insertarComentario(int idUsuario, int idLibro, String textoComentario) {
        String sql = "INSERT INTO comentarios (id_usuario, id_libro, comentario, fecha) VALUES (?, ?, ?, NOW())";
        
        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idLibro);
            stmt.setString(3, textoComentario);
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar comentario: " + e.getMessage());
            return false;
        }
    }
    
    //obtener comentarios de un libro
    public static List<String> obtenerComentariosPorLibro(int idLibro) {
        List<String> comentarios = new ArrayList<>();
        String sql = "SELECT u.nombre_usuario, c.comentario FROM comentarios c " +
                     "JOIN usuarios u ON c.id_usuario = u.id " +
                     "WHERE c.id_libro = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLibro);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre_usuario");
                String texto = rs.getString("comentario");
                comentarios.add(nombre + ": " + texto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comentarios;
    }
    
    public static void insertarPuntuacion(int idUsuario, int idLibro, int puntuacion) {
    String sql = "INSERT INTO calificaciones (id_usuario, id_libro, puntuacion) " +
                 "VALUES (?, ?, ?) " +
                 "ON CONFLICT (id_usuario, id_libro) " +
                 "DO UPDATE SET puntuacion = EXCLUDED.puntuacion;";

    try (Connection conn = Conexion.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);
        stmt.setInt(2, idLibro);
        stmt.setInt(3, puntuacion);

        stmt.executeUpdate();

        JOptionPane.showMessageDialog(null, "Puntuación guardada correctamente.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar la puntuación: " + e.getMessage());
    }
}

    public static double obtenerPromedioPuntuacion(int idLibro) {
    String sql = "SELECT AVG(puntuacion) FROM calificaciones WHERE id_libro = ?";
    double promedio = 0;

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idLibro);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            promedio = rs.getDouble(1);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return promedio;
}
}


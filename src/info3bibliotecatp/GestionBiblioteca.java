/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info3bibliotecatp;
import java.util.*;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author MSI
 */
public class GestionBiblioteca {
    public static List<Libro> cargarLibros(int idUsuario){
    List<Libro> libros = new ArrayList<>();
    Connection conn = Conexion.conectar();

    if (conn != null) {
        try {
            String sql = "SELECT l.id, l.titulo, l.autor " +
                         "FROM biblioteca_usuarios bu " +
                         "JOIN libros l ON bu.id_libro = l.id " +
                         "WHERE bu.id_usuario = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro(rs.getString("titulo"), rs.getString("autor"));
                libros.add(libro);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error al cargar libros: " + e.getMessage());
        }
        
        
    }
    return libros;
}
    public static void agregarLibro(int idUsuario, int idLibro) {
        Connection conn = Conexion.conectar();

        if (conn != null) {
            try {
                String sql = "INSERT INTO biblioteca_usuarios (id_usuario, id_libro) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idUsuario);
                stmt.setInt(2, idLibro);
                stmt.executeUpdate();

                stmt.close();
                conn.close();

            } catch (SQLException e) {
                System.out.println("Error al agregar libro: " + e.getMessage());
            }
        }
    }
    
    public static void insertarLibroEnBaseDeDatos(String titulo, String autor, String rutaPdf, String portada) {
    Connection con = Conexion.conectar();
    if (con != null) {
        try {
            String sql = "INSERT INTO libros (titulo, autor, ruta_pdf, portada) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setString(3, rutaPdf);
            stmt.setString(4, portada);
            stmt.executeUpdate();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al insertar libro: " + e.getMessage());
        }
    }
}

public static int obtenerIdLibroPorArchivo(String nombreArchivo) {
    Connection con = Conexion.conectar();
    if (con != null) {
        try {
            String sql = "SELECT id FROM libros WHERE ruta_pdf LIKE ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + nombreArchivo); // Busca por nombre de archivo
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar libro por archivo: " + e.getMessage());
        }
    }
    return -1;
}

public static boolean agregarLibroABiblioteca(int idUsuario, int idLibro) {
    Connection con = Conexion.conectar();
    if (con != null) {
        try {
            System.out.println("Intentando agregar id_libro=" + idLibro + " para id_usuario=" + idUsuario);

            // Verificar si ya está en la biblioteca
            String checkSql = "SELECT COUNT(*) FROM biblioteca_usuarios WHERE id_usuario = ? AND id_libro = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, idUsuario);
            checkStmt.setInt(2, idLibro);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                rs.close();
                checkStmt.close();
                con.close();
                return false; // ya existe
            }
            rs.close();
            checkStmt.close();

            // Insertar si no está
            String sql = "INSERT INTO biblioteca_usuarios (id_usuario, id_libro) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idLibro);
            stmt.executeUpdate();

            stmt.close();
            con.close();
            return true; // agregado correctamente
    }       catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar libro a la biblioteca: " + e.getMessage());
            return false;
    }
    }
    return false;
}
}

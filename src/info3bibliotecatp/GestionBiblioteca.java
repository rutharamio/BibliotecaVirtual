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
            String sql = "SELECT l.id, l.titulo, l.autor,l.ruta_pdf, bu.ruta_portada " +
                         "FROM biblioteca_usuarios bu " +
                         "JOIN libros l ON bu.id_libro = l.id " +
                         "WHERE bu.id_usuario = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("ruta_pdf"),
                rs.getString("ruta_portada")
            );
    
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
    
    public static boolean insertarLibroEnBaseDeDatos(String titulo, String autor, String rutaPdf, String rutaPortada) {
    String sql = "INSERT INTO libros (titulo, autor, ruta_pdf, ruta_portada) VALUES (?, ?, ?, ?)";
    
        try(Connection conn = Conexion.conectar()) {
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, autor);
            stmt.setString(3, rutaPdf);
            stmt.setString(4, rutaPortada);
            return stmt.executeUpdate()>0;
        } catch (SQLException e) {
         e.printStackTrace();
        return false;
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

public static boolean agregarLibroABiblioteca(int idUsuario, int idLibro, String rutaPortadaOriginal) {
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

            // Insertar incluyendo la portada
            String sql = "INSERT INTO biblioteca_usuarios (id_usuario, id_libro, ruta_portada) VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idLibro);
            stmt.setString(3, rutaPortadaOriginal); // ⚠️ nueva línea clave
            stmt.executeUpdate();

            stmt.close();
            con.close();
            return true; // agregado correctamente
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar libro a la biblioteca: " + e.getMessage());
            return false;
        }
    }
    return false;
}

public static boolean actualizarPortadaLibroUsuario(int idUsuario, int idLibro, String rutaImagen) {
    String sql = "UPDATE biblioteca_usuarios SET ruta_portada = ? WHERE id_usuario = ? AND id_libro = ?";
    try (Connection conn = Conexion.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, rutaImagen);
        stmt.setInt(2, idUsuario);
        stmt.setInt(3, idLibro);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public static Libro obtenerLibroPorId(int idLibro) {
    String sql = "SELECT titulo, autor, ruta_pdf, ruta_portada FROM libros WHERE id = ?";
    try (Connection conn = Conexion.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idLibro);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Libro(
    idLibro,
    rs.getString("titulo"),
    rs.getString("autor"),
    rs.getString("ruta_pdf"),
    rs.getString("ruta_portada")
);

        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public static int obtenerIdLibroPorTitulo(String titulo) {
    String sql = "SELECT id FROM libros WHERE titulo = ?";
    try (Connection conn = Conexion.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, titulo);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}

public static String obtenerRutaPortadaOriginal(int idLibro) {
    String sql = "SELECT ruta_portada FROM libros WHERE id = ?";
    try (Connection conn = Conexion.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idLibro);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("ruta_portada");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "";
}

public static Libro obtenerLibroPorTitulo(String titulo) {
    String sql = "SELECT * FROM libros WHERE titulo = ?";
    try (Connection conn = Conexion.conectar()) {
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, titulo);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String autor = rs.getString("autor");
            String rutaPdf = rs.getString("ruta_pdf");
            String rutaPortada = rs.getString("ruta_portada");
            int id = rs.getInt("id");
            return new Libro(id,titulo, autor, rutaPdf, rutaPortada);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar libro por título: " + e.getMessage());
    }
    return null;
}

public static List<Libro> obtenerTodosLosLibros() {
    List<Libro> listaLibros = new ArrayList<>();

    String sql = "SELECT * FROM libros"; 

    try (Connection conn = Conexion.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String titulo = rs.getString("titulo");
            String autor = rs.getString("autor");
            String rutaArchivo = rs.getString("ruta_archivo");
            String rutaPortada = rs.getString("ruta_portada");

            Libro libro = new Libro(id, titulo, autor, rutaArchivo, rutaPortada);
            listaLibros.add(libro);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return listaLibros;
}

public static List<Libro> obtenerLibrosDisponibles() {
    List<Libro> libros = new ArrayList<>();

    String sql = "SELECT * FROM libros";

    try (Connection conn = Conexion.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Libro libro = new Libro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("ruta_pdf"),
                rs.getString("ruta_portada")
            );
            libros.add(libro);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return libros;
}

}

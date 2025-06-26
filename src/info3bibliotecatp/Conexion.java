/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info3bibliotecatp;

/**
 *
 * @author MSI
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/Info3BibliotecaTP"; // ← nombre de tu base de datos
    private static final String USER = "postgres"; // ← usuario
    private static final String PASSWORD = "aralinda2006"; // ← tu contraseña exacta

    
        public static Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Info3BibliotecaTP",
                "postgres",
                "aralinda2006"
            );
            System.out.println("✓ Conexión exitosa a PostgreSQL");
            return conn;
        } catch (Exception e) {
            System.err.println("❌ Error al conectar: " + e.getMessage());
            return null;
        }
    }
}



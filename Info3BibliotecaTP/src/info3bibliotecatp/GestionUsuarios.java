/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info3bibliotecatp;
import java.io.*;
import java.util.*;
/**
 *
 * @author MSI
 */
public class GestionUsuarios {

    private static final String ARCHIVO = "usuarios.txt";

    public static boolean registrarUsuario(String nombre, String contrasena) throws IOException {
        List<String> usuarios = leerUsuarios();
        for (String linea : usuarios) {
        if (linea.trim().isEmpty()) continue; // <- evita líneas vacías

        String[] partes = linea.split(",");
        if (partes.length >= 2 && partes[0].equalsIgnoreCase(nombre)) {
        return false; // Ya existe
        }
       }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            bw.write(nombre + "," + contrasena);
            bw.newLine();
        }

        return true;
    }

    public static boolean verificarLogin(String nombre, String contrasena) throws IOException {
        List<String> usuarios = leerUsuarios();
        for (String linea : usuarios) {
            String[] partes = linea.split(",");
            if (partes.length == 2 && partes[0].equals(nombre) && partes[1].equals(contrasena)) {
                return true;
            }
        }
        return false;
    }

    private static List<String> leerUsuarios() throws IOException {
        File file = new File(ARCHIVO);
        if (!file.exists()) file.createNewFile();

        return java.nio.file.Files.readAllLines(file.toPath());
    }
}

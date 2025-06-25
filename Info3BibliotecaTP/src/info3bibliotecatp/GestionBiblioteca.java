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
public class GestionBiblioteca {
    public static List<Libro> cargarLibros(String nombreUsuario) throws IOException {
        List<Libro> libros = new ArrayList<>();
        File archivo = new File(nombreUsuario + "_libros.txt");

        if (!archivo.exists()) return libros;

        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length == 2) {
                libros.add(new Libro(partes[0], partes[1]));
            }
        }
        br.close();
        return libros;
    }
}

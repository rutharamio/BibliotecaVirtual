/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info3bibliotecatp;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class VerPDFCompletoComoImagen {

    public static void mostrarTodasLasPaginas(String rutaPDF) {
        try {
            // Cargar el PDF
            PDDocument documento = PDDocument.load(new File(rutaPDF));
            PDFRenderer renderer = new PDFRenderer(documento);

            // Panel vertical para apilar las imágenes
            JPanel panelPaginas = new JPanel();
            panelPaginas.setLayout(new BoxLayout(panelPaginas, BoxLayout.Y_AXIS));
            panelPaginas.setBackground(Color.WHITE);

            // Recorrer y renderizar todas las páginas
            int totalPaginas = documento.getNumberOfPages();
            for (int i = 0; i < totalPaginas; i++) {
                BufferedImage imagen = renderer.renderImageWithDPI(i, 150);
                JLabel etiqueta = new JLabel(new ImageIcon(imagen));
                etiqueta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                panelPaginas.add(etiqueta);
            }

            documento.close();

            // Mostrar en un JScrollPane
            JScrollPane scroll = new JScrollPane(panelPaginas);
            scroll.getVerticalScrollBar().setUnitIncrement(16); // suaviza el scroll

            JFrame ventana = new JFrame("PDF completo como imagen");
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.getContentPane().add(scroll);
            ventana.setSize(850, 1000);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Prueba
    public static void main(String[] args) {
        String ruta = "C:/Users/Crist/Downloads/dummy.pdf"; // Cambiá por tu archivo
        mostrarTodasLasPaginas(ruta);
    }
}



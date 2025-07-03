/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info3bibliotecatp;

/**
 *
 * @author MSI
 */
public class Libro {
    private int idLibro;
    private String titulo;
    private String autor;
    private String rutaPortada;
    private String rutaPDF;

    public Libro(int idLibro,String titulo, String autor, String rutaPDF, String rutaPortada) {
        this.idLibro=idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.rutaPortada=rutaPortada;
        this.rutaPDF= rutaPDF;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    
    public String getRutaPortada() {
    return rutaPortada;
}

    public String getRutaPDF() {
        return rutaPDF;
    }

    public void setRutaPDF(String rutaPDF) {
        this.rutaPDF = rutaPDF;
    }

    public void setRutaPortada(String rutaPortada) {
        this.rutaPortada = rutaPortada;
    }

    @Override
    public String toString() {
    if (autor.equalsIgnoreCase("Desconocido")) {
        return titulo;
    }
    return titulo + " Autor " + autor;
}
}


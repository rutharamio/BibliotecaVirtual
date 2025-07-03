/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info3bibliotecatp;

/**
 *
 * @author MSI
 */
public class Comentario {
    private int id;
    private int idUsuario;
    private int idLibro;
    private String comentario;
    private String fecha;

    public Comentario(int id, int idUsuario, int idLibro, String comentario, String fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdLibro() { return idLibro; }
    public String getComentario() { return comentario; }
    public String getFecha() { return fecha; }
}

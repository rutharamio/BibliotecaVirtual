/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package info3bibliotecatp;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;
import javax.swing.JOptionPane;
import java.util.List;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author MSI
 */
public class BibliotecaVentana extends javax.swing.JFrame {
    /**
     * Creates new form BibliotecaVentana
     */
    private String nombreUsuario;
    private int idUsuario;
    private boolean esAdmin;

    public BibliotecaVentana(String nombreUsuario, int idUsuario, boolean esAdmin) {
    jPanel1 = new FondoPanelMiBiblioteca();
    jPanel2 = new FondoPanelLibrosDisp();
    
    initComponents();
    
    jPanel1.setLayout(new BorderLayout());
    jPanel2.setLayout(new BorderLayout());

    setSize(900, 600);
    setLocationRelativeTo(null);
    
    this.listaLibrosDisponibles = new ArrayList<>();
    this.nombreUsuario = nombreUsuario;
    this.idUsuario=idUsuario;
    this.esAdmin=esAdmin;
    
    DefaultListModel<Libro> modeloBiblioteca = new DefaultListModel<>();
    jList1 = new javax.swing.JList<>();
    jList1.setModel(modeloBiblioteca);
    jScrollPane1.setViewportView(jList1);


    modeloLibros = new DefaultListModel<>();
    jList2 = new javax.swing.JList<>();
    jList2.setModel(modeloLibros);
    jScrollPane2.setViewportView(jList2);

    agregarDobleClickParaAbrirPDF(jList1); // libros disp
    agregarDobleClickParaAbrirPDF(jList2); // mi biblio
    
    jLabel1.setText("Biblioteca personal de: " + nombreUsuario);
    jButton1.setVisible(esAdmin);
    
    jLabelPortada.setPreferredSize(new Dimension(150, 200));
    jLabelPortada.setMinimumSize(new Dimension(150, 200));
    
    jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Libro libroSeleccionado = jList2.getSelectedValue();
                if (libroSeleccionado != null) {
                    System.out.println("Libro seleccionado: " + libroSeleccionado.getTitulo());

System.out.println("Ruta portada: " + libroSeleccionado.getRutaPortada());
System.out.println("¿Existe? " + new File(libroSeleccionado.getRutaPortada()).exists());

                    mostrarPortada(libroSeleccionado.getRutaPortada());
                    jLabelTitulo.setText(libroSeleccionado.getTitulo());
                }
            }
        }
    });


    
    List<Libro>libros=GestionBiblioteca.cargarLibros(idUsuario);
    mostrarLibros(libros);
    cargarLibros();
    cargarLibrosDisponibles();
    
    if (!modeloLibros.isEmpty()) {
    jList2.setSelectedIndex(0);
    Libro primerLibro = modeloLibros.getElementAt(0);
    mostrarPortada(primerLibro.getRutaPortada());
    jLabelTitulo.setText(primerLibro.getTitulo());
}

    
    jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent evt){
            if (!evt.getValueIsAdjusting()){
                mostrarDetallesLibroPersonal();
            }
        }
    });
    
System.out.println("Usuario: " + nombreUsuario);
System.out.println("esAdmin: " + esAdmin);

    }
    
    private void mostrarLibros(List<Libro> libros) {
    for (Libro libro : libros) {
        System.out.println("Título: " + libro.getTitulo());
    }
}

    private void cargarLibros() {
     try {
        java.util.List<Libro> libros = GestionBiblioteca.cargarLibros(idUsuario);
        javax.swing.DefaultListModel<Libro> modelo = new javax.swing.DefaultListModel<>();
        for (Libro libro : libros) {
            modelo.addElement(libro);
        }
        jList1.setModel((javax.swing.ListModel)modelo); // ahora jList1 contiene objetos Libro
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar libros.");
    }
}

    private void mostrarDetallesLibroPersonal() {
    Libro libroSeleccionado = jList1.getSelectedValue();

    if (libroSeleccionado != null) {
        // Mostrar título
        labeltitulo2.setText(libroSeleccionado.toString());

        // Mostrar portada
        String rutaPortada = libroSeleccionado.getRutaPortada();
        if (rutaPortada != null && !rutaPortada.isEmpty()) {
            ImageIcon icon = new ImageIcon(rutaPortada);
            Image imagen = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            jLabelportada1.setIcon(new ImageIcon(imagen));
        } else {
            jLabelportada1.setIcon(null); // limpiar si no hay imagen
        }
    }
}


    private void cargarLibrosDisponibles() {
        try (Connection conn = Conexion.conectar()) {
            listaLibrosDisponibles.clear();         // Limpia la lista lógica
            modeloLibros.clear();              // Limpia el modelo de la JList

            String sql = "SELECT id, titulo, autor, ruta_pdf, ruta_portada FROM libros";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Libro libro = new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("ruta_pdf"),
                        rs.getString("ruta_portada")
                    );
                    listaLibrosDisponibles.add(libro);
                    modeloLibros.addElement(libro);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar libros disponibles desde la base de datos.");
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabelportada1 = new javax.swing.JLabel();
        labeltitulo2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ComentarLiibro = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jButton2 = new javax.swing.JButton();
        jLabelPortada = new javax.swing.JLabel();
        jLabelTitulo = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        verComentariosLibros = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        campoBusquedaLibro = new javax.swing.JTextField();
        botonBuscarLibro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Biblioteca personal de: [usuario]");

        jButton3.setText("Cambiar portada");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        ComentarLiibro.setText("Comenta el libro");
        ComentarLiibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComentarLiibroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(jLabelportada1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labeltitulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(ComentarLiibro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(160, 160, 160))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelportada1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(labeltitulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(ComentarLiibro))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Mi biblioteca", jPanel1);

        jButton2.setText("Agregar a mi biblioteca");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabelTitulo.setText("jLabelTitulo");

        jButton1.setText("Subir PDF");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        verComentariosLibros.setText("Ver comentario del libro");
        verComentariosLibros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verComentariosLibrosActionPerformed(evt);
            }
        });

        jLabel2.setText("Libros disponibles");

        campoBusquedaLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoBusquedaLibroActionPerformed(evt);
            }
        });

        botonBuscarLibro.setText("Buscar");
        botonBuscarLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarLibroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(verComentariosLibros)
                .addGap(137, 137, 137)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(29, 29, 29))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(campoBusquedaLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(botonBuscarLibro)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(jLabelPortada, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(57, 57, 57))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(289, 289, 289))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campoBusquedaLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonBuscarLibro))
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabelPortada, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(verComentariosLibros)
                    .addComponent(jButton2))
                .addGap(21, 21, 21))
        );

        jTabbedPane1.addTab("Libros Disponibles", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 905, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
   JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Seleccionar archivo PDF");
    FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos PDF", "pdf");
    fileChooser.setFileFilter(filtro);

    int seleccion = fileChooser.showOpenDialog(this);
    if (seleccion == JFileChooser.APPROVE_OPTION) {
        File archivoSeleccionado = fileChooser.getSelectedFile();
        File carpetaDestino = new File("pdfs");
        if (!carpetaDestino.exists()) carpetaDestino.mkdirs();

        File destino = new File(carpetaDestino, archivoSeleccionado.getName());

        // SELECCIÓN DE PORTADA DESPUÉS DEL PDF
        JFileChooser imgChooser = new JFileChooser();
        imgChooser.setDialogTitle("Seleccionar imagen de portada");
        imgChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));

        int seleccionImg = imgChooser.showOpenDialog(this);
        String rutaPortada = "";

        if (seleccionImg == JFileChooser.APPROVE_OPTION) {
            File imagenSeleccionada = imgChooser.getSelectedFile();
            File carpetaPortadas = new File("portadas");
            if (!carpetaPortadas.exists()) carpetaPortadas.mkdirs();

            File destinoPortada = new File(carpetaPortadas, imagenSeleccionada.getName());
            try {
                Files.copy(imagenSeleccionada.toPath(), destinoPortada.toPath(), StandardCopyOption.REPLACE_EXISTING);
                rutaPortada = destinoPortada.getAbsolutePath();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al copiar la imagen: " + e.getMessage());
            }
        }

        try {
            Files.copy(archivoSeleccionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String titulo = archivoSeleccionado.getName().replace(".pdf", "");
            String ruta = destino.getAbsolutePath();
            String autor = "Desconocido"; 

            GestionBiblioteca.insertarLibroEnBaseDeDatos(titulo, autor, ruta, rutaPortada);

            JOptionPane.showMessageDialog(this, "PDF subido correctamente.");
            cargarLibrosDisponibles();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al subir el PDF: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    Libro libroSeleccionado = jList2.getSelectedValue();

    if (libroSeleccionado == null) {
        JOptionPane.showMessageDialog(this, "Seleccioná un libro primero.");
        return;
    }

    String nombreArchivo = new File(libroSeleccionado.getRutaPDF()).getName();
    int idLibro = GestionBiblioteca.obtenerIdLibroPorArchivo(nombreArchivo);

    
    try {
        if (idLibro != -1) {
            String rutaPortadaOriginal = GestionBiblioteca.obtenerRutaPortadaOriginal(idLibro);
            boolean agregado = GestionBiblioteca.agregarLibroABiblioteca(idUsuario, idLibro, rutaPortadaOriginal);
            if (agregado) {
                cargarLibros(); //agrega a la biblioteca personal.
                JOptionPane.showMessageDialog(this, "Libro agregado a tu biblioteca.");
            } else {
                JOptionPane.showMessageDialog(this, "Este libro ya está en tu biblioteca.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el libro en la base de datos.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al agregar el libro: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int index = jList1.getSelectedIndex();
    if (index == -1) {
        JOptionPane.showMessageDialog(this, "Seleccioná un libro de tu biblioteca primero.");
        return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Seleccionar nueva imagen de portada");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));

    int seleccion = fileChooser.showOpenDialog(this);
    if (seleccion == JFileChooser.APPROVE_OPTION) {
        File imagenSeleccionada = fileChooser.getSelectedFile();
        Libro libroSeleccionado = jList1.getSelectedValue();
        String nombreLibro = libroSeleccionado.getTitulo();
        
        int idLibro = GestionBiblioteca.obtenerIdLibroPorTitulo(nombreLibro);
        if (idLibro != -1) {
            boolean actualizado = GestionBiblioteca.actualizarPortadaLibroUsuario(idUsuario, idLibro, imagenSeleccionada.getAbsolutePath());
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Portada actualizada.");
                cargarLibros(); 
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la portada.");
            }
        }
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void verComentariosLibrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verComentariosLibrosActionPerformed
        // TODO add your handling code here:
    Libro libroSeleccionado = jList2.getSelectedValue();  

    if (libroSeleccionado != null) {
        int idLibro = libroSeleccionado.getIdLibro();
        String titulo = libroSeleccionado.getTitulo();

        ComentariosVentana ventana = new ComentariosVentana(idLibro, idUsuario, titulo);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
    } else {
        JOptionPane.showMessageDialog(this, "Selecciona un libro primero.");
    }
    }//GEN-LAST:event_verComentariosLibrosActionPerformed

    private void ComentarLiibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComentarLiibroActionPerformed
        // TODO add your handling code here:
    Libro libroSeleccionado = jList1.getSelectedValue();  

    if (libroSeleccionado != null) {
        int idLibro = libroSeleccionado.getIdLibro();
        String titulo = libroSeleccionado.getTitulo();

        ComentariosVentana ventana = new ComentariosVentana(idLibro, idUsuario, titulo);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
    } else {
        JOptionPane.showMessageDialog(this, "Selecciona un libro primero.");
    }
    }//GEN-LAST:event_ComentarLiibroActionPerformed

    private void campoBusquedaLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoBusquedaLibroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoBusquedaLibroActionPerformed

    private void botonBuscarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBuscarLibroActionPerformed
        // TODO add your handling code here:
        
     String textoBusqueda = campoBusquedaLibro.getText().trim().toLowerCase(); // ⚠️ trim agregado
    modeloLibros.clear();

    List<Libro> librosDisponibles = GestionBiblioteca.obtenerLibrosDisponibles();
    System.out.println("Texto buscado: " + textoBusqueda);
    System.out.println("Libros disponibles:");

    for (Libro libro : librosDisponibles) {
        String tituloNormalizado = libro.getTitulo().toLowerCase().trim();
        if (tituloNormalizado.contains(textoBusqueda)) {
            modeloLibros.addElement(libro);
        }
    }

    // Mostrar el primero si hay resultados
    if (!modeloLibros.isEmpty()) {
        jList2.setSelectedIndex(0);
        Libro primerLibro = modeloLibros.getElementAt(0);
        mostrarPortada(primerLibro.getRutaPortada());
        jLabelTitulo.setText(primerLibro.getTitulo());
    } else {
        jLabelPortada.setIcon(null);
        jLabelTitulo.setText("No se encontraron resultados.");
    }
    }//GEN-LAST:event_botonBuscarLibroActionPerformed

    /**
     * @param args the command line arguments
     */
    
    private void mostrarPortada(String rutaPortada) {
    System.out.println("Intentando mostrar imagen: " + rutaPortada);

    if (rutaPortada != null && !rutaPortada.isEmpty()) {
        File f = new File(rutaPortada);
        if (f.exists()) {
            ImageIcon icono = new ImageIcon(rutaPortada);
            Image imagen = icono.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            jLabelPortada.setIcon(new ImageIcon(imagen));
            jLabelPortada.repaint();
        } else {
            System.out.println(" La imagen NO existe en el disco.");
            jLabelPortada.setIcon(null);
        }
    } else {
        System.out.println(" La ruta es nula o vacía.");
        jLabelPortada.setIcon(null);
    }
}
    
private void agregarDobleClickParaAbrirPDF(JList<Libro> lista) {
    lista.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Libro libro = lista.getSelectedValue();
                if (libro != null) {
                    VerPDFCompletoComoImagen.mostrarTodasLasPaginas(libro.getRutaPDF());
                }
            }
        }
    });
}
    private List<Libro> listaLibrosDisponibles;
    private DefaultListModel<Libro> modeloLibros;
    private JList<Libro> jList1;
    private JList<Libro> jList2;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ComentarLiibro;
    private javax.swing.JButton botonBuscarLibro;
    private javax.swing.JTextField campoBusquedaLibro;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelPortada;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelportada1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labeltitulo2;
    private javax.swing.JButton verComentariosLibros;
    // End of variables declaration//GEN-END:variables
}
// Panel con fondo adaptativo para Mi Biblioteca
class FondoPanelMiBiblioteca extends JPanel {
    private Image fondo;

    public FondoPanelMiBiblioteca() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/info3bibliotecatp/Imagenes/bookish (3).png"));
        fondo = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}

// Panel con fondo adaptativo para Libros Disponibles
class FondoPanelLibrosDisp extends JPanel {
    private Image fondo;

    public FondoPanelLibrosDisp() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/info3bibliotecatp/Imagenes/bookish (6).png"));
        fondo = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}

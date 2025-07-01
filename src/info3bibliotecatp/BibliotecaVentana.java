/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package info3bibliotecatp;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.awt.Desktop;
import java.io.File;
import javax.swing.JOptionPane;
import java.util.List;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.ImageIcon;

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
    initComponents();
    
    this.nombreUsuario = nombreUsuario;
    this.idUsuario=idUsuario;
    this.esAdmin=esAdmin;
    
    
    
    jList2 = new javax.swing.JList<>();
    jList2.setModel(new javax.swing.DefaultListModel<Libro>());
    jScrollPane2.setViewportView(jList2); // si la lista está dentro de un scroll
    DefaultListModel<Libro> modelo = new DefaultListModel<>();
    jList2.setModel(modelo);


    
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
        javax.swing.DefaultListModel<String> modelo = new javax.swing.DefaultListModel<>();
        for (Libro libro : libros) {
            modelo.addElement(libro.toString());
        }
        jList1.setModel(modelo);
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar libros.");
    }
    }

private void cargarLibrosDisponibles() {
    try {
        File carpeta = new File("pdfs");
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        DefaultListModel<Libro> modelo = (DefaultListModel<Libro>) jList2.getModel(); // Usamos el modelo ya seteado

        if (archivos != null) {
            for (File archivo : archivos) {
                String nombreArchivo = archivo.getName();

                int idLibro = GestionBiblioteca.obtenerIdLibroPorArchivo(nombreArchivo);
                if (idLibro == -1) {
                    String titulo = nombreArchivo.replace(".pdf", "");
                    String ruta = archivo.getAbsolutePath();
                    String autor = "";
                    GestionBiblioteca.insertarLibroEnBaseDeDatos(titulo, autor, ruta, "");
                    idLibro = GestionBiblioteca.obtenerIdLibroPorArchivo(nombreArchivo); // Lo volvemos a obtener
                }

                // Ahora sí, buscamos y agregamos el libro
                Libro libro = GestionBiblioteca.obtenerLibroPorId(idLibro);
                if (libro != null) {
                    modelo.addElement(libro); // Agregamos el objeto Libro al modelo
                    if (modelo.size() == 1) {
                    jList2.setSelectedIndex(0); // Para que dispare el listener y se muestre la primera portada automáticamente
                    }

                }
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar libros disponibles.");
        e.printStackTrace();
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jButton2 = new javax.swing.JButton();
        jLabelPortada = new javax.swing.JLabel();
        jLabelTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Biblioteca personal de: [usuario]");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("Subir PDF");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Cambiar portada");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(107, 107, 107)
                .addComponent(jButton1)
                .addGap(33, 33, 33))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 211, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap(137, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Mi bilioteca", jPanel1);

        jButton2.setText("Agregar a mi biblioteca");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabelTitulo.setText("jLabelTitulo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(30, 30, 30))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(30, 30, 30))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelPortada, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelPortada, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTitulo)))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Libros Disponibles", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(328, 328, 328))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
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
            String autor = "Desconocido"; // Podés usar también JOptionPane para pedir el autor

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
        String nombreLibro = jList1.getSelectedValue().split(" Autor ")[0]; // extraer solo el título

        int idLibro = GestionBiblioteca.obtenerIdLibroPorTitulo(nombreLibro);
        if (idLibro != -1) {
            boolean actualizado = GestionBiblioteca.actualizarPortadaLibroUsuario(idUsuario, idLibro, imagenSeleccionada.getAbsolutePath());
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Portada actualizada.");
                cargarLibros(); // recargar si querés refrescar
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la portada.");
            }
        }
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */

    private void abrirPDF(String nombreArchivo) {
    try {
        File pdf = new File("pdfs/" + nombreArchivo);
        if (pdf.exists()) {
            Desktop.getDesktop().open(pdf);
        } else {
            JOptionPane.showMessageDialog(this, "El archivo no existe.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al abrir el PDF: " + e.getMessage());
    }
}
    
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
            System.out.println("⚠️ La imagen NO existe en el disco.");
            jLabelPortada.setIcon(null);
        }
    } else {
        System.out.println("⚠️ La ruta es nula o vacía.");
        jLabelPortada.setIcon(null);
    }
}


    private JList<Libro> jList2;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelPortada;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
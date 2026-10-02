/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataOpciones;

/**
 * editor para ubicar una ruta de directorio o archivo
 *
 * @author camilo
 */



import java.awt.Component;
import java.beans.PropertyEditorSupport;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FilePathEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        Object val = getValue();
        return val != null ? val.toString() : "";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        // OBLIGATORIO: Notifica al PropertySheet de NetBeans que el valor ha cambiado
        firePropertyChange(); 
    }

    @Override
    public boolean supportsCustomEditor() {
        return true; // Habilita el botón "..." en la celda del PropertySheet
    }

    @Override
    public Component getCustomEditor() {
        JFileChooser chooser = new JFileChooser();
        
        // Si ya hay un valor asignado, posicionar el selector en ese archivo
        String currentPath = getAsText();
        if (currentPath != null && !currentPath.trim().isEmpty()) {
            chooser.setSelectedFile(new File(currentPath));
        }

        // Filtro opcional para imágenes (ideal para logos)
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos de Imagen (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg", "gif"
        );
        chooser.setFileFilter(filter);

        // Capturar cuando el usuario presiona "Aceptar/Abrir" dentro del diálogo de NetBeans
        chooser.addActionListener(e -> {
            if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                File selectedFile = chooser.getSelectedFile();
                if (selectedFile != null) {
                    // Actualiza el valor y dispara firePropertyChange()
                    setValue(selectedFile.getAbsolutePath());
                }
            }
        });

        return chooser; // NetBeans incrustará el JFileChooser directamente en su ventana emergente
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataAyuda;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Help",
        id = "org.gcto.dataAyuda.verAbrirProy"
)
@ActionRegistration(
        iconBase = "org/gcto/dataAyuda/abrirProy.png",
        displayName = "#CTL_verAbrirProy"
)
@ActionReference(path = "Actions/verAyuda_items", position = 300, separatorBefore = 290)
@Messages("CTL_verAbrirProy=abrir Proyecto")
public final class verAbrirProy implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e)
    {
              try {
            // Cargar el PDF desde el mismo paquete
            InputStream is = getClass().getResourceAsStream("CARGAR PROYECTO.pdf");
            if (is == null) {
                return;
            }
            
            // Crear un archivo temporal para poder abrirlo
            File tempFile = File.createTempFile("CARGAR_PROYECTO", ".pdf");
            tempFile.deleteOnExit();
            
            try (OutputStream os = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
            
            // Abrir el archivo con la aplicación predeterminada
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}

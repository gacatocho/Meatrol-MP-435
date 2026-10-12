/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataInicio;

import java.io.File;
import org.gcto.dataBD.RecuperarClaseGenerica;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import org.gcto.dataBD.SalvarClaseGenerica;
import org.gcto.dataGlobal.glb;
import org.openide.modules.OnStart;
import org.openide.modules.Places;
import org.openide.util.Exceptions;

/**
 *
 * @author camilo
 */
@OnStart
public class Arranque implements Runnable
{

    @Override
    public void run()
    {
        //se carga el archivo de opciones
        // Obtiene el directorio de datos de la aplicación de forma portable
        String userHome = System.getProperty("user.home");

        // Carpeta común "dataApplications" dentro del home del usuario
        Path carpeta = Paths.get(userHome, "dataApplications");

        System.out.println("Carpeta de Opciones : " +  carpeta);

        // Crear la carpeta si no existe
        if (!Files.exists(carpeta))
        {
            try
            {
                Files.createDirectories(carpeta);
            } catch (IOException ex)
            {
                Exceptions.printStackTrace(ex);
            }
        }
        //se captura el path
        if (!RecuperarClaseGenerica.cargarObjeto("Opciones", carpeta.toString()))
        {
            System.out.println("No se reupero a Opciones " );
            //JOptionPane.showMessageDialog(null, "No se reupero a Opciones", "Recuperando opciones", JOptionPane.WARNING_MESSAGE);
            try
            {
                //si no se recupera es que es laprimera arrancada / salve una original y continue
                SalvarClaseGenerica.salvarObjeto(glb.opc);
            } catch (IOException ex)
            {
                Exceptions.printStackTrace(ex);
            }
        }

        try
        {
            glb.cargarColores();
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "No se cargaron colores - se usan los de defecto", "Recuperando colores", JOptionPane.WARNING_MESSAGE);
        }

    }

}

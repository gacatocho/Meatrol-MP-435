/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataVDM;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@Messages("CTL_eliminarCeros=elimina filas cero")
public final class eliminarCeros
{

    @ActionID(
            category = "Edit",
            id = "org.gcto.dataVDM.eliminarCeros"
    )
    @ActionRegistration(
            iconBase = "org/gcto/dataVDM/limpiarCeros.png",
            displayName = "#CTL_eliminarCeros"
    )
    @ActionReferences(
            {
                @ActionReference(path = "Menu/Edit", position = 100, separatorBefore = 50),
                @ActionReference(path = "Toolbars/Edit", position = 100)
            })

    public static final String ELIMINAR_CEROS = "EliminarCeros";

}

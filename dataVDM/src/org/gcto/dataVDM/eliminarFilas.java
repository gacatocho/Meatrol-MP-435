/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataVDM;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@Messages("CTL_eliminarFilas=eliminar filas seleccionadas")
public final class eliminarFilas
{

    @ActionID(
            category = "Edit",
            id = "org.gcto.dataVDM.eliminarFilas"
    )
    @ActionRegistration(
            iconBase = "org/gcto/dataVDM/eliminarFilas.png",
            displayName = "#CTL_eliminarFilas"
    )
    @ActionReferences(
            {
                @ActionReference(path = "Menu/Edit", position = -200, separatorBefore = -250),
                @ActionReference(path = "Toolbars/Edit", position = -100)
            })

    //con esta defincion se llama en el TOP correspondiente dentro de su ActionMap
    public static final String ELIMINAR_FILAS_SELECC = "EliminarFilasSelecc";
}

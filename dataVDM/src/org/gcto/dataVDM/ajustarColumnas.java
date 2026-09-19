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

@Messages("CTL_ajustarColumnas=ajustar columnas de tabla")
public final class ajustarColumnas
{

    @ActionID(
            category = "Edit",
            id = "org.gcto.dataVDM.ajustarColumnas"
    )
    @ActionRegistration(
            iconBase = "org/gcto/dataVDM/ajustar.png",
            displayName = "#CTL_ajustarColumnas"
    )
    @ActionReferences(
            {
                @ActionReference(path = "Menu/Edit", position = -50, separatorBefore = -100),
                @ActionReference(path = "Toolbars/Edit", position = 0)
            })

    //con esta defincion se llama en el TOP correspondiente dentro de su ActionMap
    public static final String AJUSTAR_COLUMNAS = "AjustarColumnas";

}

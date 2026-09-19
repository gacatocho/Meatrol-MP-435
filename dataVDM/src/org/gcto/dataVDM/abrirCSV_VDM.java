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

@Messages("CTL_abrirCSV_VDM=abrir CSV VDM")
public final class abrirCSV_VDM
{

    @ActionID(
            category = "File",
            id = "org.gcto.dataVDM.abrirCSV_VDM"
    )
    @ActionRegistration(
            iconBase = "org/gcto/dataVDM/abirCSV.png",
            displayName = "#CTL_abrirCSV_VDM"
    )
    @ActionReferences(
            {
                @ActionReference(path = "Menu/File", position = 600, separatorBefore = 550),
                @ActionReference(path = "Toolbars/File", position = 200)
            })
    
    public static final String ABRIR_CSV_VDM = "AbrirCsvVdm";
}

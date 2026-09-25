/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataGlobal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.gcto.dataGlobal.genInforme"
)
@ActionRegistration(
        iconBase = "org/gcto/dataGlobal/informe.png",
        displayName = "#CTL_genInforme"
)
@ActionReferences(
{
    @ActionReference(path = "Menu/File", position = 450, separatorBefore = 400),
    @ActionReference(path = "Toolbars/File", position = 100)
})
@Messages("CTL_genInforme=generar Informe")
public final class genInforme implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO implement action body
        GenerarInforme genInfo = new GenerarInforme(null, true);
        genInfo.setVisible(true);
        
    }
}

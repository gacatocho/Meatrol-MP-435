/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataInformes;

import com.openhtmltopdf.css.constants.IdentValue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.gcto.dataVDM.dataTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "org.gcto.dataGlobal.genInforme"
)
@ActionRegistration(
        displayName = "#CTL_genInforme",
        lazy = false
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/File", position = 450, separatorBefore = 400),
            @ActionReference(path = "Toolbars/File", position = 100)
        })
@Messages("CTL_genInforme=generar Informe")
public final class genInforme extends CallableSystemAction
{

    public genInforme()
    {
        setEnabled(false);
    }
    
    @Override
    public void performAction()
    {
        GenerarInforme genInfo = new GenerarInforme(null, true);
        genInfo.setVisible(true);
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_genInforme();
    }

    @Override
    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }

//    @Override
//    public void actionPerformed(ActionEvent e)
//    {
//        // TODO implement action body
//        
//    }
    @Override
    protected String iconResource()
    {
        return "org/gcto/dataInformes/informe.png";
    }
}

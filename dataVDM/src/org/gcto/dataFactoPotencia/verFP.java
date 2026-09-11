/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataFactoPotencia;

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
        category = "View",
        id = "org.gcto.dataFactoPotencia.verFP"
)
@ActionRegistration(
        displayName = "#CTL_verFP",
        lazy=false
)
@ActionReferences(
{
    @ActionReference(path = "Menu/View", position = -650, separatorBefore = -700),
    @ActionReference(path = "Toolbars/View", position = -500)
})
@Messages("CTL_verFP=ver factor de potencia")
public final class verFP extends CallableSystemAction
{

    public verFP()
    {
        setEnabled(false);
    }
    
    @Override
    public void performAction()
    {
        TopComponent dataTC = WindowManager.getDefault().findTopComponent("dataTopComponent");
        if (dataTC instanceof dataTopComponent) {
            dataTopComponent dtc = (dataTopComponent) dataTC;
            
            String[] headers = dtc.getHeaders();
            java.util.List<String[]> data = dtc.getDataList();
            
            TopComponent fpTC = WindowManager.getDefault().findTopComponent("fpTopComponent");
            if (fpTC instanceof fpTopComponent) {
                fpTopComponent ftc = (fpTopComponent) fpTC;
                ftc.setData(headers, data);
                ftc.open();
                ftc.requestActive();
            }
        }
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verFP();
    }

    @Override
    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource()
    {
        return "org/gcto/dataFactoPotencia/FP.png";
    }
    
    @Override
    protected boolean asynchronous()
    {
        return false;
    }
}

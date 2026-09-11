/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataPotActiva;

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
        id = "org.gcto.dataPotActiva.verPotActiva"
)
@ActionRegistration(
        displayName = "#CTL_verPotActiva",
        lazy=false
)
@ActionReferences(
{
    @ActionReference(path = "Menu/View", position = -400, separatorBefore = -450),
    @ActionReference(path = "Toolbars/View", position = -300)
})
@Messages("CTL_verPotActiva=ver potencia activa")
public final class verPotActiva extends CallableSystemAction
{

    @Override
    public void performAction()
    {
        TopComponent dataTC = WindowManager.getDefault().findTopComponent("dataTopComponent");
        if (dataTC instanceof dataTopComponent) {
            dataTopComponent dtc = (dataTopComponent) dataTC;
            String[] headers = dtc.getHeaders();
            java.util.List<String[]> data = dtc.getDataList();
            
            TopComponent potTC = WindowManager.getDefault().findTopComponent("potActivaTopComponent");
            if (potTC instanceof potActivaTopComponent) {
                potActivaTopComponent ptc = (potActivaTopComponent) potTC;
                ptc.setData(headers, data);
                ptc.open();
                ptc.requestActive();
            }
        }
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verPotActiva();
    }

    @Override
    public HelpCtx getHelpCtx()
    {
         return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous()
    {
        return false;
    }
    
    @Override
    public String iconResource() {
        return "org/gcto/dataPotActiva/P.png";
    }
}

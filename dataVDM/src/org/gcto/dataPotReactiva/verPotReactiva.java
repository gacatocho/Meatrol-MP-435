/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataPotReactiva;

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
        id = "org.gcto.dataPotReactiva.verPotReactiva"
)
@ActionRegistration(
        displayName = "#CTL_verPotReactiva",
        lazy=false
)
@ActionReferences(
{
    @ActionReference(path = "Menu/View", position = -500, separatorBefore = -550),
    @ActionReference(path = "Toolbars/View", position = -400)
})
@Messages("CTL_verPotReactiva=ver potencia reactiva")
public final class verPotReactiva extends CallableSystemAction
{
    public verPotReactiva()
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
            
            // Crear una NUEVA instancia
            potReactivaTopComponent ptc = new potReactivaTopComponent();
            ptc.setData(headers, data);
            ptc.open();
            ptc.requestActive();
        }
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verPotReactiva();
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
        return "org/gcto/dataPotReactiva/Q.png";
    }
}

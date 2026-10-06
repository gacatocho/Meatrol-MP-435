/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataPotAparente;

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
        id = "org.gcto.dataPotAparente.verPotAparente"
)
@ActionRegistration(
        displayName = "#CTL_verPotAparente",
        lazy=false
)
@ActionReferences(
{
    @ActionReference(path = "Menu/View", position = -300, separatorBefore = -350),
    @ActionReference(path = "Toolbars/View", position = -200)
})
@Messages("CTL_verPotAparente=ver potencia aparente")
public final class verPotAparente extends CallableSystemAction
{

    public verPotAparente()
    {
        setEnabled(false);
    }

    
    
    @Override
    public void performAction()
    {
        // 1. Buscar el componente de datos principal
        TopComponent dataTC = WindowManager.getDefault().findTopComponent("dataTopComponent");
        if (dataTC instanceof dataTopComponent) {
            dataTopComponent dtc = (dataTopComponent) dataTC;
            
            // 2. Extraer datos
            String[] headers = dtc.getHeaders();
            java.util.List<String[]> data = dtc.getDataList();
            
            // 3. Crear una NUEVA instancia
            potAparenteTopComponent ptc = new potAparenteTopComponent();
            
            // 4. Transferir datos
            ptc.setData(headers, data);
            
            // 5. Mostrar
            ptc.open();
            ptc.requestActive();
        }
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verPotAparente();
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
        return "org/gcto/dataPotAparente/S.png";
    }
    
}

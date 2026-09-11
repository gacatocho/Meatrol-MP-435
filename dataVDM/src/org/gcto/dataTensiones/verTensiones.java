/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/contextAction.java to edit this template
 */
package org.gcto.dataTensiones;

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
        id = "org.gcto.dataTensiones.verTensiones"
)
@ActionRegistration(
        displayName = "#CTL_verTensiones",
        lazy= false
)
@ActionReferences(
{
    @ActionReference(path = "Menu/View", position = -150, separatorBefore = -200),
    @ActionReference(path = "Toolbars/View", position = -100)
})
@Messages("CTL_verTensiones=ver  Tensiones")
public final class verTensiones extends CallableSystemAction
{

    public verTensiones()
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
            
            // 3. Buscar y preparar el componente de tensiones
            TopComponent tensionesTC = WindowManager.getDefault().findTopComponent("tensionesTopComponent");
            if (tensionesTC instanceof tensionesTopComponent) {
                tensionesTopComponent ttc = (tensionesTopComponent) tensionesTC;
                
                // 4. Transferir datos
                ttc.setData(headers, data);
                
                // 5. Mostrar
                ttc.open();
                ttc.requestActive();
            }
        }
    }
    
    @Override
    public String getName()
    {
        return Bundle.CTL_verTensiones();
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
        return "org/gcto/dataTensiones/verTensiones.png";
    }
}

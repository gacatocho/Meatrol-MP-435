/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataCorrientes;

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

/**
 *
 * @author camilo
 */
@ActionID(
        category = "View",
        id = "org.gcto.dataCorrientes.dataCorriente"
)
@ActionRegistration(
        displayName = "#CTL_dataCorriente",
        lazy = false
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/View", position = 0, separatorBefore = -50),
            @ActionReference(path = "Toolbars/View", position = 0)
        })
@Messages("CTL_dataCorriente=analisis de corrientes")
public final class dataCorrientes extends CallableSystemAction
{
    public dataCorrientes()
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
            corrientesTopComponent ctc = new corrientesTopComponent();
            
            // 4. Transferir datos
            ctc.setData(headers, data);
            
            // 5. Mostrar
            ctc.open();
            ctc.requestActive();
        }
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_dataCorriente();
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
        return "org/gcto/dataCorrientes/corriente.png";
    }
}

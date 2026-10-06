/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataEnergias;

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
        id = "org.gcto.dataEnergias.verEnergias"
)
@ActionRegistration(
        displayName = "#CTL_verEnergias",
        lazy = false
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/View", position = -1100, separatorBefore = -1150, separatorAfter = -1050),
            @ActionReference(path = "Toolbars/View", position = -800)
        })
@Messages("CTL_verEnergias=ver Energías")
public final class verEnergias extends CallableSystemAction
{

    public verEnergias()
    {
        setEnabled(false);
    }

    @Override
    public void performAction()
    {
        TopComponent dataTC = WindowManager.getDefault().findTopComponent("dataTopComponent");
        if (dataTC instanceof dataTopComponent)
        {
            dataTopComponent dtc = (dataTopComponent) dataTC;

            String[] headers = dtc.getHeaders();
            java.util.List<String[]> data = dtc.getDataList();

            // Crear una NUEVA instancia
            energiasTopComponent energtc = new energiasTopComponent();
            energtc.setData(headers, data);
            energtc.open();
            energtc.requestActive();
        }
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verEnergias();
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
    protected String iconResource()
    {
        return "org/gcto/dataEnergias/energia.png";
    }

}

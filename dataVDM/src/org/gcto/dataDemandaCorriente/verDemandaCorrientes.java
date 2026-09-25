/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataDemandaCorriente;

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
        id = "org.gcto.dataDemandaCorriente.verDemandaCorrientes"
)
@ActionRegistration(
        displayName = "#CTL_verDemandaCorrientes",
        lazy=false
)
@ActionReferences(
{
    @ActionReference(path = "Menu/View", position = -1250, separatorBefore = -1300),
    @ActionReference(path = "Toolbars/View", position = -900)
})
@Messages("CTL_verDemandaCorrientes=ver demanda de corrientes")
public final class verDemandaCorrientes extends CallableSystemAction
{

    public verDemandaCorrientes()
    {
        setEnabled(false);
    }

    @Override
    public void performAction()
    {
        TopComponent tc = WindowManager.getDefault().findTopComponent("demandaCorrientesTopComponent");
        if (tc != null)
        {
            tc.open();
            tc.requestActive();
            if (tc instanceof demandaCorrientesTopComponent)
            {
                ((demandaCorrientesTopComponent) tc).onRefresh();
            }
        }
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verDemandaCorrientes();
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
        return "org/gcto/dataDemandaCorriente/demandaCorriente.png";
    }
}

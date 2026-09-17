/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataGlobal;

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
        id = "org.gcto.dataGlobal.RefreshAction"
)
@ActionRegistration(
        displayName = "#CTL_RefreshAction",
        lazy = false
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/View", position = 1100),
            @ActionReference(path = "Toolbars/View", position = 1100)
        })
@Messages("CTL_RefreshAction=Refrescar Datos")
public final class RefreshAction extends CallableSystemAction
{

    public RefreshAction()
    {
        setEnabled(false);
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(evt ->
        {
            if (TopComponent.Registry.PROP_ACTIVATED.equals(evt.getPropertyName()))
            {
                updateEnabled();
            }
        });
        updateEnabled();
    }

    private void updateEnabled()
    {
        TopComponent active = WindowManager.getDefault().getRegistry().getActivated();
        setEnabled(active instanceof baseTopComponent);
    }

    @Override
    public void performAction()
    {
        TopComponent active = WindowManager.getDefault().getRegistry().getActivated();
        if (active instanceof baseTopComponent)
        {
            ((baseTopComponent) active).onRefresh();
        }
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_RefreshAction();
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
        return "org/gcto/dataGlobal/refrescar.png";
    }
    
    
    
}

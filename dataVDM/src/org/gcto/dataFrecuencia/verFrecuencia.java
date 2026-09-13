/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataFrecuencia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CallableSystemAction;

@ActionID(
        category = "View",
        id = "org.gcto.dataFrecuencia.verFrecuencia"
)
@ActionRegistration(
        displayName = "#CTL_verFrecuencia",
        lazy = false
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/View", position = -800, separatorBefore = -850),
            @ActionReference(path = "Toolbars/View", position = -600)
        })
@Messages("CTL_verFrecuencia=ver frecuencias")
public final class verFrecuencia extends CallableSystemAction
{

    @Override
    public void performAction()
    {

    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verFrecuencia();
    }

    @Override
    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource()
    {
        return "org/gcto/dataFrecuencia/Fz.png";
    }

    @Override
    protected boolean asynchronous()
    {
        return false;
    }

}

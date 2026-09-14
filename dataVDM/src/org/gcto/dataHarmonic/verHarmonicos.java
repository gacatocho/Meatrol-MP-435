/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataHarmonic;

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
        id = "org.gcto.dataHarmonic.verHarmonicos"
)
@ActionRegistration(
        displayName = "#CTL_verHarmonicos",
        lazy=false
)
@ActionReferences(
{
    @ActionReference(path = "Menu/View", position = -950, separatorBefore = -1000),
    @ActionReference(path = "Toolbars/View", position = -700)
})
@Messages("CTL_verHarmonicos=ver Armónicos")
public final class verHarmonicos extends CallableSystemAction
{

    public verHarmonicos()
    {
        setEnabled(false);
    }
    
    

    @Override
    public void performAction()
    {
       
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verHarmonicos();
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
        return "org/gcto/dataHarmonic/TH.png";
    }

    
    
    

    
}

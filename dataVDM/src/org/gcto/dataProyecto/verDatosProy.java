/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataProyecto;

import java.io.IOException;
import org.gcto.dataBD.SalvarClaseGenerica;
import org.gcto.dataGlobal.glb;
import org.openide.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CallableSystemAction;

@ActionID(
        category = "File",
        id = "org.gcto.dataGlobal.verDatosProy"
)
@ActionRegistration(
        displayName = "#CTL_verDatosProy",
        lazy = false
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/File", position = -1400, separatorBefore = -1450),
            @ActionReference(path = "Toolbars/File", position = -1000)
        })
@Messages("CTL_verDatosProy=var datos del proyecto")
public final class verDatosProy extends CallableSystemAction
{

    public verDatosProy()
    {
        setEnabled(false);
    }

    @Override
    public void performAction()
    {
        PropertySheet ps = new PropertySheet();

        DatosProy dpCop = new DatosProy();
        //editamos una copia
        glb.dp.copy(dpCop);
        
        DatosProyNode beanNodeDatosProy;

        beanNodeDatosProy = new DatosProyNode(dpCop);
        ps.setNodes(new Node[]
        {
            beanNodeDatosProy
        });
        DialogDescriptor dd = new DialogDescriptor(ps, "Edición de Datos Proyecto");
        DialogDisplayer.getDefault().notify(dd);
        
        Object value = dd.getValue();
        
        if (value==DialogDescriptor.OK_OPTION)        
        {
            try
            {
                //salve el contenido de la copia en el originañ
                dpCop.copy(glb.dp);
                //salve el original
                SalvarClaseGenerica.salvarObjeto(glb.dp);
            } catch (IOException ex)
            {
                Exceptions.printStackTrace(ex);
            }
        } else        
        {
            System.out.println("Se cancelo la edicion de datos del proyecto");
        }
      
    }

    @Override
    public String getName()
    {
        return Bundle.CTL_verDatosProy();
    }

    @Override
    public HelpCtx getHelpCtx()
    {
    return HelpCtx.DEFAULT_HELP;    
    }
    
     @Override
    protected String iconResource()
    {
        return "org/gcto/dataProyecto/verDatosProy.png";
    }
}

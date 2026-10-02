/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataProyecto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import org.gcto.dataGlobal.glb;
import org.openide.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "View",
        id = "org.gcto.dataGlobal.verDatosProy"
)
@ActionRegistration(
        iconBase = "org/gcto/dataProyecto/verDatosProy.png",
        displayName = "#CTL_verDatosProy"
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/View", position = -1400, separatorBefore = -1450),
            @ActionReference(path = "Toolbars/View", position = -1000)
        })
@Messages("CTL_verDatosProy=var datos del proyecto")
public final class verDatosProy implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e)
    {
        PropertySheet ps = new PropertySheet();

        DatosProyNode beanNodeDatosProy;

        beanNodeDatosProy = new DatosProyNode(glb.dp);
        ps.setNodes(new Node[]
        {
            beanNodeDatosProy
        });
        DialogDescriptor dd = new DialogDescriptor(ps, "Edición de Datos Proyecto");
        DialogDisplayer.getDefault().notify(dd);

    }
}

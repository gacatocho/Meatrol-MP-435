/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataOpciones;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.gcto.dataGlobal.glb;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "org.gcto.dataOpciones.verOpciones"
)
@ActionRegistration(
        iconBase = "org/gcto/dataOpciones/opciones.png",
        displayName = "#CTL_verOpciones"
)
@ActionReferences(
{
    @ActionReference(path = "Menu/Tools", position = 0, separatorBefore = -50),
    @ActionReference(path = "Toolbars/File", position = 0)
})
@Messages("CTL_verOpciones=ver Opciones")
public final class verOpciones implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // se lanza el formualrio de opciones para su edicion
        PropertySheet ps = new PropertySheet();

        OpcionesNode opcNode;

        opcNode = new OpcionesNode(glb.opc);
        ps.setNodes(new Node[]
        {
            opcNode
        });
        DialogDescriptor dd = new DialogDescriptor(ps, "Edición de Opciones del Proyecto");
        DialogDisplayer.getDefault().notify(dd);
    }
}

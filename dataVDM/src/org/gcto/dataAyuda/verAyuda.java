/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataAyuda;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
        category = "Help",
        id = "org.gcto.dataAyuda.verAyuda"
)
@ActionRegistration(
        lazy = false,
        displayName = "#CTL_verAyuda"
)
@ActionReference(path = "Menu/Help", position = 100, separatorBefore = 50)
@Messages("CTL_verAyuda=ver ayuda")
public final class verAyuda extends AbstractAction implements Presenter.Menu
{

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // No hace nada al hacer clic en el título del menú
    }

    @Override
    public JMenuItem getMenuPresenter()
    {
        // Creamos el menú con el texto y el icono
        JMenu menu = new JMenu(Bundle.CTL_verAyuda());
        menu.setIcon(ImageUtilities.loadImageIcon("org/gcto/dataAyuda/Help.png", false));
        
        // Cargamos dinámicamente las acciones de la carpeta "Actions/verAyuda_items"
        // Esta ruta no es visible en el menú principal de NetBeans
        List<? extends Action> actions = Utilities.actionsForPath("Actions/verAyuda_items");
        for (Action a : actions) {
            if (a == null) {
                menu.addSeparator();
            } else {
                menu.add(a);
            }
        }
        return menu;
    }
}

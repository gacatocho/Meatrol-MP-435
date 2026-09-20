/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/actionListener.java to edit this template
 */
package org.gcto.dataGlobal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Edit",
        id = "org.gcto.dataGlobal.blancoNegro"
)
@ActionRegistration(
        iconBase = "org/gcto/dataGlobal/blancoNegro.png",
        displayName = "#CTL_blancoNegro"
)
@ActionReferences(
{
    @ActionReference(path = "Menu/Edit", position = -350, separatorBefore = -400),
    @ActionReference(path = "Toolbars/Edit", position = -200)
})
@Messages("CTL_blancoNegro=cambiar fondo blanco <-> negro")
public final class blancoNegro implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // 1. Alternar el estado global
        glb.darkMode = !glb.darkMode;
        
        // 2. Ajustar colores dinámicos globales
        if (glb.darkMode) {
            glb.colorN = Color.WHITE;
        } else {
            glb.colorN = Color.BLACK;
        }
        
        // 3. Notificar a todos los TopComponents abiertos para que refresquen sus gráficos y tablas
        Set<TopComponent> opened = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : opened) {
            if (tc instanceof baseTopComponent) {
                ((baseTopComponent) tc).updateTheme();
            } else {
                // Fallback para otros componentes que puedan contener gráficos de forma anidada
                refreshChartsInComponent(tc);
            }
        }
    }
    
    /**
     * Busca recursivamente instancias de FastChartPanel dentro de un componente
     * y les ordena actualizar su tema.
     */
    private void refreshChartsInComponent(java.awt.Container container) {
        for (java.awt.Component comp : container.getComponents()) {
            if (comp instanceof FastChartPanel) {
                ((FastChartPanel) comp).updateTheme();
            } else if (comp instanceof java.awt.Container) {
                refreshChartsInComponent((java.awt.Container) comp);
            }
        }
    }
}

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
        category = "File",
        id = "org.gcto.dataGlobal.ExportAction"
)
@ActionRegistration(
        iconBase = "org/gcto/dataGlobal/export.png",
        displayName = "#CTL_ExportAction",
        lazy = false
)
@ActionReferences(
        {
            @ActionReference(path = "Menu/File", position = 1100),
            @ActionReference(path = "Toolbars/File", position = 1100)
        })
@Messages("CTL_ExportAction=Exportar Gráfico para Informe")
public final class ExportAction extends CallableSystemAction
{
    public ExportAction() {
        setEnabled(false);
        // Escuchar cambios en el TopComponent activado para habilitar/deshabilitar la acción
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(evt -> {
            if (TopComponent.Registry.PROP_ACTIVATED.equals(evt.getPropertyName())) {
                updateEnabled();
            }
        });
        updateEnabled();
    }

    private void updateEnabled() {
        TopComponent active = WindowManager.getDefault().getRegistry().getActivated();
        // Solo se habilita si el componente activo es un formulario de análisis (baseTopComponent)
        setEnabled(active instanceof baseTopComponent);
    }

    @Override
    public void performAction()
    {
        TopComponent active = WindowManager.getDefault().getRegistry().getActivated();
        if (active instanceof baseTopComponent)
        {
            // Ejecuta la exportación directa (forzando fondo blanco internamente)
            ((baseTopComponent) active).onExport();
        }
    }

    @Override
    public String getName() { return Bundle.CTL_ExportAction(); }

    @Override
    public HelpCtx getHelpCtx() { return HelpCtx.DEFAULT_HELP; }

    @Override
    protected boolean asynchronous() { return false; }

    @Override
    protected String iconResource()
    {
        return "org/gcto/dataGlobal/export.png";
    }
}

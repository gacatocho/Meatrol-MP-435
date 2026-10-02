package org.gcto.dataGlobal;

import org.gcto.dataBD.SalvarClaseGenerica;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.gcto.dataVDM.dataTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "org.gcto.dataGlobal.SaveProjectAction"
)
@ActionRegistration(
        iconBase = "org/gcto/dataVDM/guardar.png",
        displayName = "#CTL_SaveProjectAction",
        lazy = false
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 700),
    @ActionReference(path = "Toolbars/File", position = 300)
})
@Messages("CTL_SaveProjectAction=Guardar Proyecto")
public final class SaveProjectAction extends CallableSystemAction {

    @Override
    public void performAction() {
        executeSave(true);
    }

    /**
     * Ejecuta la lógica de guardado.
     * @param prompt Si es true, pide confirmación antes de sobreescribir.
     */
    public void executeSave(boolean prompt) {
        String ruta = glb.dp.getRutaProyecto();
        
        // Validación de ruta lógica: si es nula, vacía o el directorio no existe, es un "Guardar como"
        if (ruta == null || ruta.trim().isEmpty() || !(new File(ruta).exists())) {
            SystemAction.get(SaveProjectAsAction.class).performAction();
            return;
        }

        if (prompt) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Desea guardar los cambios en el proyecto actual?\n" + ruta,
                    "Guardar Cambios", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            // 1. Salvar Datos del Proyecto (Base de datos ODB)
            SalvarClaseGenerica.salvarObjeto(glb.dp);
            
            // 2. Salvar Opciones (Base de datos ODB)
            SalvarClaseGenerica.salvarObjeto(glb.opc);

            // 3. Salvar Datos Binarios (VDM)
            TopComponent tc = WindowManager.getDefault().findTopComponent("dataTopComponent");
            if (tc instanceof dataTopComponent) {
                File dirVDM = new File(ruta);
                File archivoVDM = new File(dirVDM, "datos.vdm");
                ((dataTopComponent) tc).saveBinary(archivoVDM);
            }
            
            if (prompt) {
                JOptionPane.showMessageDialog(null, "Proyecto guardado exitosamente.");
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            JOptionPane.showMessageDialog(null, "Error al guardar el proyecto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String getName() {
        return Bundle.CTL_SaveProjectAction();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource() {
        return "org/gcto/dataVDM/guardar.png";
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

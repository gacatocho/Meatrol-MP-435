package org.gcto.dataGlobal;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;

@ActionID(
        category = "File",
        id = "org.gcto.dataGlobal.SaveProjectAsAction"
)
@ActionRegistration(
        iconBase = "org/gcto/dataGlobal/export.png",
        displayName = "#CTL_SaveProjectAsAction",
        lazy = false
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 800),
    @ActionReference(path = "Toolbars/File", position = 400)
})
@Messages("CTL_SaveProjectAsAction=Guardar Proyecto Como...")
public final class SaveProjectAsAction extends CallableSystemAction {

    @Override
    public void performAction() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Seleccione la carpeta para guardar el proyecto");

        // Si ya hay una ruta, empezar desde ahí
        String rutaActual = glb.dp.getRutaProyecto();
        if (rutaActual != null && !rutaActual.isEmpty()) {
            File currentDir = new File(rutaActual);
            if (currentDir.exists()) {
                fc.setCurrentDirectory(currentDir.getParentFile());
            }
        }

        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fc.getSelectedFile();
            
            // Asegurarse de que el directorio existe
            if (!selectedDir.exists()) {
                selectedDir.mkdirs();
            }
            
            // Actualizar la ruta en el objeto de datos del proyecto
            glb.dp.setRutaProyecto(selectedDir.getAbsolutePath());
            
            // Ejecutar el guardado real sin preguntar de nuevo (silent save)
            SaveProjectAction saveAction = SystemAction.get(SaveProjectAction.class);
            saveAction.executeSave(false); 
            
            JOptionPane.showMessageDialog(null, "Proyecto guardado correctamente en:\n" + selectedDir.getAbsolutePath());
        }
    }

    @Override
    public String getName() {
        return Bundle.CTL_SaveProjectAsAction();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource() {
        return "org/gcto/dataVDM/guardarAs.png";
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

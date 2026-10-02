package org.gcto.dataVDM;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.gcto.dataGlobal.glb;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;
import org.openide.windows.WindowManager;

@ActionID(category = "File", id = "org.gcto.dataVDM.RecentFilesAction")
@ActionRegistration(displayName = "#CTL_RecentFilesAction", lazy = false)
@ActionReference(path = "Menu/File", position = 100)
@Messages("CTL_RecentFilesAction=Abrir recientes")
public final class RecentFilesAction extends AbstractAction implements Presenter.Menu {

    @Override
    public void actionPerformed(ActionEvent e) {
        // No se usa directamente ya que implementamos Presenter.Menu
    }

    @Override
    public JMenu getMenuPresenter() {
        JMenu menu = new JMenu(Bundle.CTL_RecentFilesAction());
        String[] recientes = glb.opc.getListaRecientes();
        boolean hasItems = false;
        
        if (recientes != null) {
            for (String path : recientes) {
                if (path != null && !path.trim().isEmpty()) {
                    JMenuItem item = new JMenuItem(path);
                    item.addActionListener(e -> {
                        glb.seletedFileVDM = new File(path);
                        dataTopComponent tc = (dataTopComponent) WindowManager.getDefault().findTopComponent("dataTopComponent");
                        if (tc != null) {
                            if (!tc.isOpened()) {
                                tc.open();
                            }
                            tc.requestActive();
                            tc.cargarProyectoVDM();
                        }
                    });
                    menu.add(item);
                    hasItems = true;
                }
            }
        }
        
        menu.setEnabled(hasItems);
        return menu;
    }
}

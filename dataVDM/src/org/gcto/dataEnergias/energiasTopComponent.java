package org.gcto.dataEnergias;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import org.gcto.dataGlobal.FastChartPanel;
import org.gcto.dataGlobal.baseTopComponent;
import org.gcto.dataGlobal.glb;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component que visualiza el análisis de Energías.
 * Optimizado para valores acumulados:
 * - Visualización con relleno de área (Area Fill).
 * - Estadísticas de Promedio Aritmético (restaurado por preferencia del usuario).
 * - Detección de Totales del sistema.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataEnergias//energias//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "energiasTopComponent",
        iconBase = "org/gcto/dataEnergias/energia.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataEnergias.energiasTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_energiasAction",
        preferredID = "energiasTopComponent"
)
@Messages(
        {
            "CTL_energiasAction=energías",
            "CTL_energiasTopComponent=Análisis de Energías",
            "HINT_energiasTopComponent=Visualización de consumo acumulado (Activa, Reactiva, Aparente)"
        })
public final class energiasTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();
    private JComboBox<String> cmbFilter;

    public energiasTopComponent()
    {
        setName(Bundle.CTL_energiasTopComponent());
        setToolTipText(Bundle.HINT_energiasTopComponent());

        // Activar modo de área para visualizar acumulados de forma profesional
        chartPanel.setAreaFillMode(true);
        setupFilterControl();
        hideStatsColumns(9, 10, 11);
    }

    private void setupFilterControl()
    {
        cmbFilter = new JComboBox<>(new String[]
        {
            "Todos", "Active (P)", "Reactive (Q)", "Apparent (S)"
        });
        cmbFilter.addActionListener(e -> mapColumns());

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 0);
        pnlSouth.add(new JLabel("Filtrar por:"), 0);
        pnlSouth.add(cmbFilter, 0);
    }

    @Override
    protected void mapColumns()
    {
        if (masterHeaders == null || masterHeaders.length == 0) return;
        
        phaseControls.clear();
        statsModel.setRowCount(0);

        List<Integer> chartIndices = new ArrayList<>();
        List<String> chartNames = new ArrayList<>();
        List<Color> chartColors = new ArrayList<>();

        String filter = cmbFilter != null ? (String) cmbFilter.getSelectedItem() : "Todos";
        String filterUpper = filter.toUpperCase();

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();
            
            // Filtrar solo columnas de energía (evitar potencias instantáneas)
            if (h.contains("ENERGY") || h.contains("WH") || h.contains("VARH") || h.contains("VAH"))
            {
                if (!filterUpper.equals("TODOS"))
                {
                    boolean match = false;
                    if (filterUpper.contains("ACTIVE")) match = (h.contains("ACTIVE") || h.contains("ACTIVA") || h.contains(" EP")) && !h.contains("REACTIVE");
                    else if (filterUpper.contains("REACTIVE")) match = h.contains("REACTIVE") || h.contains("REACTIVA") || h.contains(" EQ");
                    else if (filterUpper.contains("APPARENT")) match = h.contains("APPARENT") || h.contains("APARENTE") || h.contains(" ES");
                    
                    if (!match) continue;
                }

                Color c = Color.GRAY;

                // Detección de Totales (Prioridad visual)
                if (h.contains("SUM") || h.contains("TOTAL") || h.matches(".*EP$") || h.matches(".*EQ$") || h.matches(".*ES$")) {
                    if (h.contains("ACTIVE") || h.contains(" EP")) c = Color.MAGENTA;
                    else if (h.contains("REACTIVE") || h.contains(" EQ")) c = Color.CYAN;
                    else c = Color.ORANGE;
                }
                // Detección de Fases
                else if (h.contains("A") || h.contains("L1")) c = glb.colorA;
                else if (h.contains("B") || h.contains("L2")) c = glb.colorB;
                else if (h.contains("C") || h.contains("L3")) c = glb.colorC;

                phaseControls.add(new PhaseControl(i, masterHeaders[i], c));
                chartIndices.add(i);
                chartNames.add(masterHeaders[i]);
                chartColors.add(c);
            }
        }

        chartPanel.setSeries(chartIndices, chartNames, chartColors);
        updateStatsTableRows();
    }

    private void updateStatsTableRows()
    {
        statsModel.setRowCount(0);
        for (PhaseControl pc : phaseControls)
        {
            statsModel.addRow(new Object[]
            {
                pc.color, pc.name, true, "0.0", "-", "0.0", false, "0.0", "-", "0.0", 0, false
            });
        }
        // Restaurar cabecera original de promedio
        tblStats.getColumnModel().getColumn(5).setHeaderValue("Promedio Arit.");
        tblStats.getTableHeader().repaint();
        
        updateStatistics();
        adjustStatsTableHeight();
    }

    @Override protected void onDataLoaded() { updateChartData(); updateStatistics(); }
    @Override protected void onTimeRangeUpdated() { updateChartData(); updateStatistics(); }

    private void updateChartData()
    {
        if (masterData.isEmpty()) return;
        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));
        chartPanel.setData(masterData, sIdx, eIdx);
    }

    private void updateStatistics()
    {
        if (masterData.isEmpty() || phaseControls.isEmpty()) return;

        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));

        int rowInModel = 0;
        for (int i = 0; i < phaseControls.size(); i++)
        {
            PhaseControl pc = phaseControls.get(i);
            boolean isVisible = (boolean) statsModel.getValueAt(rowInModel, 2);
            if (!isVisible) { rowInModel++; continue; }

            int colIdx = pc.colIdx;
            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, sum = 0;
            String dateMin = "-", dateMax = "-";
            int count = 0;

            for (int r = sIdx; r <= eIdx; r++)
            {
                String[] row = masterData.get(r);
                double val = glb.parseDoubleSafe(row[colIdx]);
                if (val < min) { min = val; dateMin = row[0] + " " + row[1]; }
                if (val > max) { max = val; dateMax = row[0] + " " + row[1]; }
                sum += val; count++;
            }

            double avgArit = count > 0 ? sum / count : 0;

            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 3);
            statsModel.setValueAt(dateMin, rowInModel, 4);
            statsModel.setValueAt(String.format("%.2f", avgArit), rowInModel, 5);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 7);
            statsModel.setValueAt(dateMax, rowInModel, 8);

            rowInModel++;
        }
    }

    private static class PhaseControl
    {
        int colIdx;
        String name;
        Color color;

        PhaseControl(int colIdx, String name, Color color)
        {
            this.colIdx = colIdx;
            this.name = name;
            this.color = color;
        }
    }

    void writeProperties(java.util.Properties p) { p.setProperty("version", "1.0"); }
    void readProperties(java.util.Properties p) {}
}

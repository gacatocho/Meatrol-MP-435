/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataDemandaCorriente;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.gcto.dataGlobal.baseTopComponent;
import org.gcto.dataGlobal.glb;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component que visualiza el análisis de Demanda de Corrientes.
 * Especializado en: 
 * - Demanda por intervalo (DmI) 
 * - Picos de demanda (PDmI) 
 * - Validación de fechas de pico (ignora año 2000 de forma selectiva por fase)
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataDemandaCorriente//demandaCorriente//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "demandaCorrientesTopComponent",
        iconBase = "org/gcto/dataDemandaCorriente/demandaCorriente.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataDemandaCorriente.demandaCorrientesTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_demandaCorrienteAction",
        preferredID = "demandaCorrientesTopComponent"
)
@Messages(
        {
            "CTL_demandaCorrienteAction=demanda de corrientes",
            "CTL_demandaCorrientesTopComponent=Análisis de Demanda de Corrientes",
            "HINT_demandaCorrientesTopComponent=Visualización de demanda promedio y picos de corriente"
        })
public final class demandaCorrientesTopComponent extends baseTopComponent
{

    private final List<SeriesControl> seriesControls = new ArrayList<>();
    private JSpinner spnMinY;
    private JSpinner spnMaxY;

    public demandaCorrientesTopComponent()
    {
        setName(Bundle.CTL_demandaCorrientesTopComponent());
        setToolTipText(Bundle.HINT_demandaCorrientesTopComponent());

        setupYRangeControls();
        // Configurar tabla de estadísticas para demanda: Ocultar Min, F.Min, PromA, VerProm, F.Max (8), y extras
        hideStatsColumns(3, 4, 5, 6, 8, 9, 10, 11);
    }

    private void setupYRangeControls()
    {
        spnMinY = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 5000.0, 0.5));
        spnMaxY = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 5000.0, 1.0));
        spnMinY.setEditor(new JSpinner.NumberEditor(spnMinY, "0.0"));
        spnMaxY.setEditor(new JSpinner.NumberEditor(spnMaxY, "0.0"));
        spnMinY.addChangeListener(e -> updateManualRange());
        spnMaxY.addChangeListener(e -> updateManualRange());

        JPanel pnlY = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlY.add(new JLabel("Escala Y (A):")); pnlY.add(spnMinY);
        pnlY.add(new JLabel("Max:")); pnlY.add(spnMaxY);

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 0);
        pnlSouth.add(pnlY, 0);
    }

    private void updateManualRange()
    {
        double min = (Double) spnMinY.getValue();
        double max = (Double) spnMaxY.getValue();
        if (min < max) {
            chartPanel.setManualYRange(min, max);
        }
    }

    @Override
    protected void mapColumns()
    {
        if (masterHeaders == null || masterHeaders.length == 0) return;

        seriesControls.clear();
        statsModel.setRowCount(0);

        List<Integer> chartIndices = new ArrayList<>();
        List<String> chartNames = new ArrayList<>();
        List<Color> chartColors = new ArrayList<>();

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();

            // 1. Detectar Demanda Base (DmIA, DmIB, DmIC)
            if ((h.contains("DMIA") || h.contains("DMIB") || h.contains("DMIC")) && !h.contains("PDM") && !h.contains("_D/T"))
            {
                String phase = h.contains("DMIA") ? "A" : (h.contains("DMIB") ? "B" : "C");
                Color color = getPhaseColor(h);
                int idxDate = findColumn("PDMI" + phase + "_D/T");
                
                seriesControls.add(new SeriesControl(i, idxDate, masterHeaders[i], color));
                chartIndices.add(i);
                chartNames.add(masterHeaders[i]);
                chartColors.add(color);
            }
            
            // 2. Detectar Picos de Demanda (PDMIA, PDMIB, PDMIC)
            if ((h.contains("PDMIA") || h.contains("PDMIB") || h.contains("PDMIC")) && !h.contains("_D/T"))
            {
                String phase = h.contains("PDMIA") ? "A" : (h.contains("PDMIB") ? "B" : "C");
                Color color = getPhaseColor(h);
                int idxDate = findColumn("PDMI" + phase + "_D/T");
                
                seriesControls.add(new SeriesControl(i, idxDate, "Pico " + phase, color));
                chartIndices.add(i);
                chartNames.add("Pico " + phase);
                chartColors.add(color);
            }
        }

        chartPanel.setSeries(chartIndices, chartNames, chartColors);
        updateStatsTableRows();
        
        // Auto-escala inicial
        if (!masterData.isEmpty() && !chartIndices.isEmpty()) {
            double maxVal = 0;
            int step = Math.max(1, masterData.size() / 500);
            for (int i = 0; i < masterData.size(); i += step) {
                String[] row = masterData.get(i);
                for (int idx : chartIndices) maxVal = Math.max(maxVal, glb.parseDoubleSafe(row[idx]));
            }
            if (maxVal > 0) {
                spnMaxY.setValue(Math.ceil(maxVal * 1.2));
                updateManualRange();
            }
        }
    }

    private int findColumn(String key)
    {
        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase().replace(" ", "").replace(":", "");
            if (h.contains(key)) return i;
        }
        return -1;
    }

    private Color getPhaseColor(String h)
    {
        String hu = h.toUpperCase();
        if (hu.contains("DMIA")) return hu.contains("P") ? glb.colorAA : glb.colorA;
        if (hu.contains("DMIB")) return hu.contains("P") ? glb.colorBB : glb.colorB;
        if (hu.contains("DMIC")) return hu.contains("P") ? glb.colorCC : glb.colorC;
        return Color.GRAY;
    }

    private void updateStatsTableRows()
    {
        statsModel.setRowCount(0);
        for (SeriesControl sc : seriesControls)
        {
            statsModel.addRow(new Object[]
            {
                sc.color, sc.name, true, "-", "-", "-", false, "0.0", "-", "0.0", 0, false
            });
        }

        tblStats.getColumnModel().getColumn(7).setHeaderValue("Valor Máximo (A)");
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

        List<String[]> filteredData = new ArrayList<>(eIdx - sIdx + 1);
        for (int i = sIdx; i <= eIdx; i++)
        {
            String[] originalRow = masterData.get(i);
            String[] filteredRow = originalRow.clone();

            for (SeriesControl sc : seriesControls)
            {
                if (sc.idxDate != -1 && originalRow[sc.idxDate].startsWith("2000"))
                {
                    filteredRow[sc.colIdx] = "0.0";
                }
            }
            filteredData.add(filteredRow);
        }

        chartPanel.setData(filteredData, 0, filteredData.size() - 1);
    }

    private void updateStatistics()
    {
        if (masterData.isEmpty() || seriesControls.isEmpty()) return;

        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));

        int rowInModel = 0;
        for (SeriesControl sc : seriesControls)
        {
            boolean isVisible = (boolean) statsModel.getValueAt(rowInModel, 2);
            if (!isVisible) { rowInModel++; continue; }

            double maxVal = -1.0;

            for (int r = sIdx; r <= eIdx; r++)
            {
                String[] row = masterData.get(r);
                if (sc.idxDate != -1 && row[sc.idxDate].startsWith("2000")) continue;

                double val = glb.parseDoubleSafe(row[sc.colIdx]);
                if (val > maxVal) maxVal = val;
            }

            statsModel.setValueAt(String.format("%.2f", Math.max(0, maxVal)), rowInModel, 7);
            rowInModel++;
        }
    }

    private static class SeriesControl
    {
        int colIdx, idxDate;
        String name;
        Color color;

        SeriesControl(int ci, int idt, String n, Color c)
        {
            this.colIdx = ci;
            this.idxDate = idt;
            this.name = n;
            this.color = c;
        }
    }

    void writeProperties(java.util.Properties p) { p.setProperty("version", "1.0"); }
    void readProperties(java.util.Properties p) {}
}

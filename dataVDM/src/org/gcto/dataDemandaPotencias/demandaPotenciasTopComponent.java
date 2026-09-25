/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataDemandaPotencias;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
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
 * Top component que visualiza el análisis de Demanda de Potencias Totales.
 * Especializado en:
 * - Demanda por intervalo (DmP, DmQ, DmS)
 * - Picos de demanda (PDmP, PDmQ, PDmS)
 * - Validación de fechas de pico (ignora año 2000)
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataDemandaPotencias//demandaPotencias//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "demandaPotenciasTopComponent",
        iconBase = "org/gcto/dataDemandaPotencias/demandaPotencia.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataDemandaPotencias.demandaPotenciasTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_demandaPotenciasAction",
        preferredID = "demandaPotenciasTopComponent"
)
@Messages(
        {
            "CTL_demandaPotenciasAction=demanda de potencias",
            "CTL_demandaPotenciasTopComponent=Análisis de Demanda de Potencias",
            "HINT_demandaPotenciasTopComponent=Visualización de demanda promedio y picos de potencia total"
        })
public final class demandaPotenciasTopComponent extends baseTopComponent
{

    private final List<SeriesControl> seriesControls = new ArrayList<>();
    private JComboBox<String> cmbFilter;
    private JSpinner spnMinY;
    private JSpinner spnMaxY;
    
    public demandaPotenciasTopComponent()
    {
        setName(Bundle.CTL_demandaPotenciasTopComponent());
        setToolTipText(Bundle.HINT_demandaPotenciasTopComponent());
        
        setupFilterControl();
        setupYRangeControls();
        // Configurar tabla de estadísticas para demanda: Ocultar Min, F.Min, PromA, VerProm, F.Max (8), y extras
        hideStatsColumns(3, 4, 5, 6, 8, 9, 10, 11);
    }

    private void setupFilterControl() {
        cmbFilter = new JComboBox<>(new String[] { "Todos", "Activa (P)", "Reactiva (Q)", "Aparente (S)" });
        cmbFilter.addActionListener(e -> mapColumns());
        
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlFilter.add(new JLabel("Filtrar por:"));
        pnlFilter.add(cmbFilter);
        
        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 0);
        pnlSouth.add(pnlFilter, 0);
    }

    private void setupYRangeControls()
    {
        spnMinY = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000000.0, 10.0));
        spnMaxY = new JSpinner(new SpinnerNumberModel(1000.0, 0.0, 1000000.0, 10.0));
        spnMinY.setEditor(new JSpinner.NumberEditor(spnMinY, "0.0"));
        spnMaxY.setEditor(new JSpinner.NumberEditor(spnMaxY, "0.0"));
        spnMinY.addChangeListener(e -> updateManualRange());
        spnMaxY.addChangeListener(e -> updateManualRange());

        JPanel pnlY = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlY.add(new JLabel("Escala Y:")); pnlY.add(spnMinY);
        pnlY.add(new JLabel("Max:")); pnlY.add(spnMaxY);

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 2);
        pnlSouth.add(pnlY, 2);
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

        String filter = (String) cmbFilter.getSelectedItem();
        boolean showP = filter.equals("Todos") || filter.contains("(P)");
        boolean showQ = filter.equals("Todos") || filter.contains("(Q)");
        boolean showS = filter.equals("Todos") || filter.contains("(S)");

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase().trim();
            
            // 1. ACTIVA (P) - Detección estricta basada en claves del equipo (DmP, PDmP)
            if (showP && h.endsWith("DMP") && !h.contains("PDMP") && !h.contains("_D/T")) {
                addSeries(i, "PDMP", "Total Active Power Deamnd(W)", glb.colorA, glb.colorAA, chartIndices, chartNames, chartColors);
            }
            // 2. REACTIVA (Q) - Detección estricta (DmQ, PDmQ)
            if (showQ && h.endsWith("DMQ") && !h.contains("PDMQ") && !h.contains("_D/T")) {
                addSeries(i, "PDMQ", "Total Reactive Power Deamnd(Var)", glb.colorBB, glb.colorB, chartIndices, chartNames, chartColors);
            }
            // 3. APARENTE (S) - Detección estricta (DmS, PDmS)
            if (showS && h.endsWith("DMS") && !h.contains("PDMS") && !h.contains("_D/T")) {
                addSeries(i, "PDMS", "Total Apparent Power Deamnd(VA)", Color.MAGENTA, glb.colorCC, chartIndices, chartNames, chartColors);
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

    private void addSeries(int idxDemand, String peakKey, String label, Color colorD, Color colorP, 
                           List<Integer> indices, List<String> names, List<Color> colors) {
        
        int idxPeak = findColumn(peakKey);
        int idxDate = findColumn(peakKey + "_D/T");

        // Fila de Demanda
        seriesControls.add(new SeriesControl(idxDemand, idxDate, "Demanda " + label, colorD));
        indices.add(idxDemand);
        names.add("Demanda " + label);
        colors.add(colorD);

        // Fila de Pico
        if (idxPeak != -1) {
            seriesControls.add(new SeriesControl(idxPeak, idxDate, "Pico " + label, colorP));
            indices.add(idxPeak);
            names.add("Pico " + label);
            colors.add(colorP);
        }
    }
    
    private int findColumn(String key) {
        for (int i = 0; i < masterHeaders.length; i++) {
            String h = masterHeaders[i].toUpperCase().replace(" ", "").replace(":", "");
            if (h.contains(key)) return i;
        }
        return -1;
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
        
        tblStats.getColumnModel().getColumn(7).setHeaderValue("Pico Máximo");
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
        for (int i = sIdx; i <= eIdx; i++) {
            String[] originalRow = masterData.get(i);
            String[] filteredRow = originalRow.clone();
            
            for (SeriesControl sc : seriesControls) {
                if (sc.idxDate != -1 && originalRow[sc.idxDate].startsWith("2000")) {
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

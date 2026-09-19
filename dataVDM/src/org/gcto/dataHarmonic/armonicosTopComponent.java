/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataHarmonic;

import java.awt.BasicStroke;
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
import org.gcto.dataGlobal.FastChartPanel;
import org.gcto.dataGlobal.baseTopComponent;
import org.gcto.dataGlobal.glb;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component que visualiza el análisis de armónicos.
 * Incluye filtrado dinámico por orden (THD, H01-H50) según nomenclatura específica.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataHarmonic//armonicos//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "armonicosTopComponent",
        iconBase = "org/gcto/dataHarmonic/TH.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataHarmonic.armonicosTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_armonicosAction",
        preferredID = "armonicosTopComponent"
)
@Messages(
        {
            "CTL_armonicosAction=armónicos",
            "CTL_armonicosTopComponent=Análisis de Armónicos",
            "HINT_armonicosTopComponent=Visualización de THD y armónicos individuales"
        })
public final class armonicosTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();
    private JSpinner spnMinY;
    private JSpinner spnMaxY;
    private JComboBox<String> cmbHarmonicOrder;

    private final Color[] extendedColors = { Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY };

    public armonicosTopComponent()
    {
        setName(Bundle.CTL_armonicosTopComponent());
        setToolTipText(Bundle.HINT_armonicosTopComponent());
        
        setupHarmonicControls();
        setupYRangeControls();
        hideStatsColumns(9, 10, 11);
    }

    private void setupHarmonicControls() {
        cmbHarmonicOrder = new JComboBox<>();
        cmbHarmonicOrder.addItem("THD");
        cmbHarmonicOrder.addItem("Fundamental (H01)");
        for (int i = 2; i <= 50; i++) {
            cmbHarmonicOrder.addItem("Armónico " + String.format("%02d", i));
        }
        
        cmbHarmonicOrder.addActionListener(e -> mapColumns());

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlFilter.add(new JLabel("Ver:"));
        pnlFilter.add(cmbHarmonicOrder);
        
        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 0);
        pnlSouth.add(pnlFilter, 0);
    }

    private void setupYRangeControls()
    {
        spnMinY = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.5));
        spnMaxY = new JSpinner(new SpinnerNumberModel(20.0, 0.0, 1000.0, 0.5));
        spnMinY.setEditor(new JSpinner.NumberEditor(spnMinY, "0.0"));
        spnMaxY.setEditor(new JSpinner.NumberEditor(spnMaxY, "0.0"));
        spnMinY.addChangeListener(e -> updateManualRange());
        spnMaxY.addChangeListener(e -> updateManualRange());

        JPanel pnlY = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlY.add(new JLabel("Min Y (%):")); pnlY.add(spnMinY);
        pnlY.add(new JLabel("Max Y (%):")); pnlY.add(spnMaxY);

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL));
        pnlSouth.add(pnlY);
        updateManualRange();
    }

    private void updateManualRange()
    {
        double min = (Double) spnMinY.getValue();
        double max = (Double) spnMaxY.getValue();
        if (min < max) chartPanel.setManualYRange(min, max);
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

        String selection = (String) cmbHarmonicOrder.getSelectedItem();
        if (selection == null) selection = "THD";

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();
            boolean match = false;

            if ("THD".equals(selection)) {
                // THD Totales: "UTHD(%)", "ITHD(%)"
                // No deben tener números de orden ni indicadores de fase en el nombre de la medida
                if ((h.contains("UTHD") || h.contains("ITHD")) && !h.matches(".*THD\\d+.*") && 
                    !h.contains("HA") && !h.contains("HB") && !h.contains("HC") &&
                    !h.contains("XA") && !h.contains("XB") && !h.contains("XC")) {
                    match = true;
                }
            } else if (selection.contains("Fundamental")) {
                // Fundamental (H01): "UTHD(%) UTHA", "ITHD(%): ITHA"
                if ((h.contains("UTHD") || h.contains("ITHD")) && !h.matches(".*THD\\d+.*") &&
                    (h.contains("HA") || h.contains("HB") || h.contains("HC") ||
                     h.contains("XA") || h.contains("XB") || h.contains("XC"))) {
                    match = true;
                }
            } else {
                // Armónico N: "ITHD3(%): ITHXA"
                String nStr = selection.replaceAll("[^0-9]", "");
                String target = "THD" + Integer.parseInt(nStr);
                // Buscamos el número exacto y que sea porcentaje
                if (h.contains(target) && h.contains("(%)")) {
                    match = true;
                }
            }

            if (match)
            {
                boolean isVoltage = h.contains("UTHD") || h.contains("UTH");
                Color c = extendedColors[i % extendedColors.length];
                
                // Detección de fase (A, B, C, N)
                if (h.contains("A") || h.contains("L1")) {
                    c = isVoltage ? glb.colorA.darker() : glb.colorA;
                } else if (h.contains("B") || h.contains("L2")) {
                    c = isVoltage ? glb.colorB.darker() : glb.colorB;
                } else if (h.contains("C") || h.contains("L3")) {
                    c = isVoltage ? glb.colorC.darker() : glb.colorC;
                } else if (h.contains("N")) {
                    c = isVoltage ? glb.colorN.darker() : glb.colorN;
                }

                phaseControls.add(new PhaseControl(i, masterHeaders[i], c));
                chartIndices.add(i);
                chartNames.add(masterHeaders[i]);
                chartColors.add(c);
            }
        }

        chartPanel.setSeries(chartIndices, chartNames, chartColors);
        updateStatsTableRows();
        
        // Ajuste automático de escala Y
        if (!masterData.isEmpty() && !chartIndices.isEmpty()) {
            double maxVal = 0;
            int step = Math.max(1, masterData.size() / 500);
            for (int i = 0; i < masterData.size(); i += step) {
                String[] row = masterData.get(i);
                for (int colIdx : chartIndices) {
                    maxVal = Math.max(maxVal, glb.parseDoubleSafe(row[colIdx]));
                }
            }
            if (maxVal > 0) {
                double newMax = Math.ceil(maxVal * 1.1);
                spnMaxY.setValue(newMax);
                updateManualRange();
            }
        }
    }

    private void updateStatsTableRows()
    {
        statsModel.setRowCount(0);
        for (PhaseControl pc : phaseControls)
        {
            // Columnas: Color(0), Nombre(1), Ver(2), Min(3), F.Min(4), PromA(5), VerProm(6), Max(7), F.Max(8), PromS(9), %Nivel(10), VerS(11)
            statsModel.addRow(new Object[]
            {
                pc.color, pc.name, true, "0.0", "-", "0.0", false, "0.0", "-", "0.0", 0, false
            });
        }
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

        List<FastChartPanel.TrendLine> trendLines = new ArrayList<>();

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

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 6);
            if (showP) trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color, new BasicStroke(glb.grosLinProm, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10, 5}, 0)));

            rowInModel++;
        }
        chartPanel.setTrendLines(trendLines);
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

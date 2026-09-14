/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataFrecuencia;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
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
 * Top component que visualiza el análisis de frecuencia.
 * Ajustado para mostrar el rango de frecuencia con detalle y reglaje manual.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataFrecuencia//frecuencia//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "frecuenciaTopComponent",
        iconBase = "org/gcto/dataFrecuencia/Fz.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataFrecuencia.frecuenciaTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_frecuenciaAction",
        preferredID = "frecuenciaTopComponent"
)
@Messages(
        {
            "CTL_frecuenciaAction=frecuencia",
            "CTL_frecuenciaTopComponent=Análisis de Frecuencia",
            "HINT_frecuenciaTopComponent=Visualización detallada de frecuencia por fase"
        })
public final class frecuenciaTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();
    private JSpinner spnMinY;
    private JSpinner spnMaxY;

    public frecuenciaTopComponent()
    {
        setName(Bundle.CTL_frecuenciaTopComponent());
        setToolTipText(Bundle.HINT_frecuenciaTopComponent());
        
        setupYRangeControls();
        
        // Ocultar columnas de Promedio sobre % (7), % de Nivel (8) y Ver promedio sobre % (9)
        hideStatsColumns(7, 8, 9);
    }

    private void setupYRangeControls() {
        // Modelos con 1 decimal de precisión. Inicializamos en un rango típico de 60Hz
        spnMinY = new JSpinner(new SpinnerNumberModel(59.7, 0.0, 100.0, 0.1));
        spnMaxY = new JSpinner(new SpinnerNumberModel(60.3, 0.0, 100.0, 0.1));
        
        JSpinner.NumberEditor editorMin = new JSpinner.NumberEditor(spnMinY, "0.0");
        spnMinY.setEditor(editorMin);
        JSpinner.NumberEditor editorMax = new JSpinner.NumberEditor(spnMaxY, "0.0");
        spnMaxY.setEditor(editorMax);

        spnMinY.addChangeListener(e -> updateManualRange());
        spnMaxY.addChangeListener(e -> updateManualRange());

        JPanel pnlY = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlY.add(new JLabel("Min Y (Hz):"));
        pnlY.add(spnMinY);
        pnlY.add(new JLabel("Max Y (Hz):"));
        pnlY.add(spnMaxY);
        
        // Añadir al panel sur de la clase base (pnlSouth es protected)
        pnlSouth.add(new JSeparator(JSeparator.VERTICAL));
        pnlSouth.add(pnlY);
        
        // Aplicar rango inicial
        updateManualRange();
    }

    private void updateManualRange() {
        double min = (Double) spnMinY.getValue();
        double max = (Double) spnMaxY.getValue();
        if (min < max) {
            chartPanel.setManualYRange(min, max);
        }
    }

    @Override
    protected void mapColumns()
    {
        pnlPhaseSelection.removeAll();
        phaseControls.clear();
        statsModel.setRowCount(0);

        List<Integer> chartIndices = new ArrayList<>();
        List<String> chartNames = new ArrayList<>();
        List<Color> chartColors = new ArrayList<>();

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();

            Color c = null;
            
            if (isFrequency(h, "FA")) c = glb.colorA;
            else if (isFrequency(h, "FB")) c = glb.colorB;
            else if (isFrequency(h, "FC")) c = glb.colorC;

            if (c != null)
            {
                final int colIdx = i;
                final Color phaseColor = c;
                final String colName = masterHeaders[i];

                JPanel pnlItem = new JPanel(new BorderLayout(5, 0));
                JCheckBox chk = new JCheckBox(colName);
                chk.setSelected(true);

                JLabel lblColor = new JLabel(" ■ ");
                lblColor.setForeground(phaseColor);
                lblColor.setPreferredSize(new Dimension(25, 20));

                pnlItem.add(lblColor, BorderLayout.WEST);
                pnlItem.add(chk, BorderLayout.CENTER);
                pnlPhaseSelection.add(pnlItem);

                PhaseControl pc = new PhaseControl(chk, colIdx, colName, phaseColor);
                phaseControls.add(pc);

                chk.addActionListener(e ->
                {
                    chartPanel.setSeriesVisible(colIdx, chk.isSelected());
                    updateStatsTableRows();
                });

                chartIndices.add(colIdx);
                chartNames.add(colName);
                chartColors.add(phaseColor);
            }
        }

        chartPanel.setSeries(chartIndices, chartNames, chartColors);
        updateStatsTableRows();

        pnlPhaseSelection.revalidate();
        pnlPhaseSelection.repaint();
    }
    
    private boolean isFrequency(String h, String phase) {
        if (!h.contains("FREQUENCY")) return false;
        return h.contains(phase) || h.contains(" " + phase) || h.endsWith(phase);
    }

    private void updateStatsTableRows()
    {
        statsModel.setRowCount(0);
        for (PhaseControl pc : phaseControls)
        {
            if (pc.checkBox.isSelected())
            {
                statsModel.addRow(new Object[]
                {
                    pc.name, "0.0", "-", "0.0", false, "0.0", "-", "0.0", 0, false
                });
            }
        }
        updateStatistics();
        adjustStatsTableHeight();
    }

    @Override
    protected void onDataLoaded()
    {
        updateChartData();
        updateStatistics();
    }

    @Override
    protected void onTimeRangeUpdated()
    {
        updateChartData();
        updateStatistics();
    }
    
    private void updateChartData() {
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
            if (!pc.checkBox.isSelected()) continue;

            int colIdx = pc.colIdx;
            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;
            double sum = 0;
            String dateMin = "-";
            String dateMax = "-";
            int count = 0;

            for (int r = sIdx; r <= eIdx; r++)
            {
                String[] row = masterData.get(r);
                double val = glb.parseDoubleSafe(row[colIdx]);

                if (val < min) { min = val; dateMin = row[0] + " " + row[1]; }
                if (val > max) { max = val; dateMax = row[0] + " " + row[1]; }
                sum += val;
                count++;
            }

            double avgArit = count > 0 ? sum / count : 0;

            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 1);
            statsModel.setValueAt(dateMin, rowInModel, 2);
            statsModel.setValueAt(String.format("%.2f", avgArit), rowInModel, 3);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 5);
            statsModel.setValueAt(dateMax, rowInModel, 6);

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 4);
            if (showP) {
                trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color, 
                    new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10, 5}, 0)));
            }

            rowInModel++;
        }
        
        chartPanel.setTrendLines(trendLines);
    }

    private static class PhaseControl
    {
        JCheckBox checkBox;
        int colIdx;
        String name;
        Color color;

        PhaseControl(JCheckBox checkBox, int colIdx, String name, Color color)
        {
            this.checkBox = checkBox;
            this.colIdx = colIdx;
            this.name = name;
            this.color = color;
        }
    }

    void writeProperties(java.util.Properties p) { p.setProperty("version", "1.0"); }
    void readProperties(java.util.Properties p) {}
}

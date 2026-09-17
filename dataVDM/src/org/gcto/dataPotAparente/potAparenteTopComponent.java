/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataPotAparente;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.gcto.dataGlobal.FastChartPanel;
import org.gcto.dataGlobal.baseTopComponent;
import org.gcto.dataGlobal.glb;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component que visualiza el análisis de potencia aparente.
 * Hereda toda la infraestructura de baseTopComponent.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataPotAparente//potAparente//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "potAparenteTopComponent",
        iconBase = "org/gcto/dataPotAparente/S.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataPotAparente.potAparenteTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_potAparenteAction",
        preferredID = "potAparenteTopComponent"
)
@Messages(
        {
            "CTL_potAparenteAction=potencia aparente",
            "CTL_potAparenteTopComponent=Análisis de Potencia Aparente",
            "HINT_potAparenteTopComponent=Visualización y análisis de potencia aparente por fase y total"
        })
public final class potAparenteTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();

    public potAparenteTopComponent()
    {
        setName("Análisis de Potencia Aparente");
        setToolTipText("Visualización y análisis de potencia aparente por fase y total");
    }

    @Override
    protected void mapColumns()
    {
        phaseControls.clear();
        statsModel.setRowCount(0);

        List<Integer> chartIndices = new ArrayList<>();
        List<String> chartNames = new ArrayList<>();
        List<Color> chartColors = new ArrayList<>();

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();
            Color c = null;
            
            if (isStrictApparent(h, "SA")) c = glb.colorA;
            else if (isStrictApparent(h, "SB")) c = glb.colorB;
            else if (isStrictApparent(h, "SC")) c = glb.colorC;
            else if (isStrictApparent(h, "SSUM") || isStrictApparent(h, "S SUM")) c = Color.MAGENTA;

            if (c != null)
            {
                phaseControls.add(new PhaseControl(i, masterHeaders[i], c));
                chartIndices.add(i);
                chartNames.add(masterHeaders[i]);
                chartColors.add(c);
            }
        }

        chartPanel.setSeries(chartIndices, chartNames, chartColors);
        updateStatsTableRows();
    }
    
    private boolean isStrictApparent(String h, String key) {
        if (!h.contains("APPARENT") && !h.contains("APARENTE")) return false;
        if (h.contains("ENERGY") || h.contains("VAH")) return false;
        return h.contains(" " + key) || h.contains(": " + key) || h.endsWith(" " + key) || h.endsWith(":" + key);
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
            boolean isVisible = (boolean) statsModel.getValueAt(rowInModel, 2);
            if (!isVisible) {
                rowInModel++;
                continue;
            }

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
            int levelPercent = (int) statsModel.getValueAt(rowInModel, 10);
            double threshold = max * (levelPercent / 100.0);
            double sumAbove = 0; int countAbove = 0;

            for (int r = sIdx; r <= eIdx; r++) {
                double val = glb.parseDoubleSafe(masterData.get(r)[colIdx]);
                if (val >= threshold) { sumAbove += val; countAbove++; }
            }
            double avgAbove = countAbove > 0 ? sumAbove / countAbove : 0;

            // Actualizar Tabla (Nuevos Índices)
            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 3);
            statsModel.setValueAt(dateMin, rowInModel, 4);
            statsModel.setValueAt(String.format("%.2f", avgArit), rowInModel, 5);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 7);
            statsModel.setValueAt(dateMax, rowInModel, 8);
            statsModel.setValueAt(String.format("%.2f", avgAbove), rowInModel, 9);

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 6);
            boolean showS = (boolean) statsModel.getValueAt(rowInModel, 11);
            
            if (showP) trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color, new BasicStroke(1.8f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10, 5}, 0)));
            if (showS) trendLines.add(new FastChartPanel.TrendLine(avgAbove, pc.color, new BasicStroke(1.8f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2, 4}, 0)));

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

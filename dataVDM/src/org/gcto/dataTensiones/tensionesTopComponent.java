/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataTensiones;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import org.gcto.dataGlobal.FastChartPanel;
import org.gcto.dataGlobal.baseTopComponent;
import org.gcto.dataGlobal.glb;
import org.gcto.dataVDM.dataTopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 * Top component que visualiza el análisis de tensiones.
 * Hereda toda la infraestructura de baseTopComponent.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataTensiones//tensiones//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "tensionesTopComponent",
        iconBase = "org/gcto/dataTensiones/verTensiones.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataTensiones.tensionesTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_tensionesAction",
        preferredID = "tensionesTopComponent"
)
@Messages(
        {
            "CTL_tensionesAction=tensiones",
            "CTL_tensionesTopComponent=tensiones Window",
            "HINT_tensionesTopComponent=This is a tensiones window"
        })
public final class tensionesTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();

    public tensionesTopComponent()
    {
        setName("Análisis de Tensiones");
        setToolTipText("Visualización y análisis de tensiones de fase y compuestas");
        
        // Ocultar columnas de Promedio sobre % (7), % de Nivel (8) y Ver promedio sobre % (9)
        hideStatsColumns(7, 8, 9);
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
            
            // Tensiones de Fase
            if (isStrictVoltage(h, "UA")) c = glb.colorA;
            else if (isStrictVoltage(h, "UB")) c = glb.colorB;
            else if (isStrictVoltage(h, "UC")) c = glb.colorC;
            
            // Tensiones Compuestas (UAB, UBC, UAC)
            else if (isStrictVoltage(h, "UAB")) c = glb.colorA.darker();
            else if (isStrictVoltage(h, "UBC")) c = glb.colorB.darker();
            else if (isStrictVoltage(h, "UAC")) c = glb.colorC.darker();

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
    
    private boolean isStrictVoltage(String h, String key) {
        if (!h.contains("VOLTAGE") && !h.contains("TENSIÓN")) return false;
        return h.contains(" " + key) || h.contains(": " + key) || h.endsWith(" " + key) || h.endsWith(":" + key);
    }

    private void updateStatsTableRows()
    {
        statsModel.setRowCount(0);
        for (PhaseControl pc : phaseControls)
        {
            if (pc.checkBox.isSelected())
            {
                // Columnas: Nombre(0), Min(1), F.Min(2), PromA(3), Ver Promedio(4), Max(5), F.Max(6), Promedio sobre %(7), % de Nivel(8), Ver promedio sobre %(9)
                // Para tensiones, los índices 7, 8 y 9 están ocultos pero deben tener valores por defecto
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
        if (masterData.isEmpty() || phaseControls.isEmpty())
        {
            return;
        }

        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));

        List<FastChartPanel.TrendLine> trendLines = new ArrayList<>();
        
        int rowInModel = 0;
        for (int i = 0; i < phaseControls.size(); i++)
        {
            PhaseControl pc = phaseControls.get(i);
            if (!pc.checkBox.isSelected())
            {
                continue;
            }

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

                if (val < min)
                {
                    min = val;
                    dateMin = row[0] + " " + row[1];
                }
                if (val > max)
                {
                    max = val;
                    dateMax = row[0] + " " + row[1];
                }
                sum += val;
                count++;
            }

            double avgArit = count > 0 ? sum / count : 0;

            // Actualizar Tabla (Solo columnas visibles para tensiones)
            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 1);
            statsModel.setValueAt(dateMin, rowInModel, 2);
            statsModel.setValueAt(String.format("%.2f", avgArit), rowInModel, 3);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 5);
            statsModel.setValueAt(dateMax, rowInModel, 6);

            // Gestionar Línea de Tendencia de Promedio Aritmético
            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 4);
            if (showP) {
                trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color, 
                    new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10, 5}, 0)));
            }

            rowInModel++;
        }
        
        chartPanel.setTrendLines(trendLines);
    }

    @Override
    protected void onRefresh()
    {
        TopComponent tc = WindowManager.getDefault().findTopComponent("dataTopComponent");
        if (tc instanceof dataTopComponent)
        {
            dataTopComponent dtc = (dataTopComponent) tc;
            if (dtc.hasData())
            {
                setData(dtc.getHeaders(), dtc.getDataList());
            }
        }
    }

    @Override
    protected void onExport()
    {
        JOptionPane.showMessageDialog(this, "Exportando análisis de tensiones...");
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

    void writeProperties(java.util.Properties p)
    {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p)
    {
        String version = p.getProperty("version");
    }
}

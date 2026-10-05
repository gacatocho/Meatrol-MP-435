/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataFrecuencia;

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
 * Top component que visualiza el análisis de frecuencia. Ajustado para mostrar
 * el rango de frecuencia con detalle y reglaje manual.
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

    public frecuenciaTopComponent()
    {
        setName(Bundle.CTL_frecuenciaTopComponent());
        setToolTipText(Bundle.HINT_frecuenciaTopComponent());

        setupYRangeControls("Hz");
        
        // Ajuste inicial solicitado: 48 - 62 Hz
        if (spnMinY != null) spnMinY.setValue(48.0);
        if (spnMaxY != null) spnMaxY.setValue(62.0);
        
        hideStatsColumns(9, 10, 11);
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
            if (glb.isStrict(h, glb.FREQ_FASE_A, glb.FREQ))
            {
                c = glb.colorA;
            } else if (glb.isStrict(h, glb.FREQ_FASE_B, glb.FREQ))
            {
                c = glb.colorB;
            } else if (glb.isStrict(h, glb.FREQ_FASE_C, glb.FREQ))
            {
                c = glb.colorC;
            } else if (glb.isStrict(h, glb.FREQ_PROMEDIO, glb.FREQ))
            {
                c = glb.colorFAVG;
            }

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

    private void updateChartData()
    {
        if (masterData.isEmpty())
        {
            return;
        }
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
            boolean isVisible = (boolean) statsModel.getValueAt(rowInModel, 2);
            if (!isVisible)
            {
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

            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 3);
            statsModel.setValueAt(dateMin, rowInModel, 4);
            statsModel.setValueAt(String.format("%.2f", avgArit), rowInModel, 5);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 7);
            statsModel.setValueAt(dateMax, rowInModel, 8);

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 6);
            if (showP)
            {
                trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color, new BasicStroke(glb.grosLinProm, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
                {
                    10, 5
                }, 0)));
            }

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

    void writeProperties(java.util.Properties p)
    {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p)
    {
    }
}

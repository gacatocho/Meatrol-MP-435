/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataDemandaCorriente;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
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
 * Top component que visualiza el análisis de Demanda de Corrientes. Reformado
 * para mostrar solo DMIA, DMIB y DMIC filtrados por DEMAND. Estructura alineada
 * con frecuenciaTopComponent. Filtra datos del año 2000 por ser irrelevantes.
 * Ajusta dinámicamente el eje Y según el máximo de la selección actual.
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
            "HINT_demandaCorrientesTopComponent=Visualización de demanda de corriente por fase (DMIA, DMIB, DMIC)"
        })
public final class demandaCorrientesTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();
    private JSpinner spnMinY;
    private JSpinner spnMaxY;

    public demandaCorrientesTopComponent()
    {
        setName(Bundle.CTL_demandaCorrientesTopComponent());
        setToolTipText(Bundle.HINT_demandaCorrientesTopComponent());

        setupYRangeControls();
        hideStatsColumns(9, 10, 11);
    }

    private void setupYRangeControls()
    {
        spnMinY = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.1));
        spnMaxY = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 10000.0, 0.5));

        spnMinY.setEditor(new JSpinner.NumberEditor(spnMinY, "0.0"));
        spnMaxY.setEditor(new JSpinner.NumberEditor(spnMaxY, "0.0"));

        spnMinY.addChangeListener(e -> updateManualRange());
        spnMaxY.addChangeListener(e -> updateManualRange());

        JPanel pnlY = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlY.add(new JLabel("Min Y (A):"));
        pnlY.add(spnMinY);
        pnlY.add(new JLabel("Max Y (A):"));
        pnlY.add(spnMaxY);

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL));
        pnlSouth.add(pnlY);

        updateManualRange();
    }

    private void updateManualRange()
    {
        double min = (Double) spnMinY.getValue();
        double max = (Double) spnMaxY.getValue();
        if (min < max)
        {
            chartPanel.setManualYRange(min, max);
        }
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

            if ((glb.isStrict(h, glb.CURRENT_DEMAND_FASE_A, glb.DEMAND_I)) && !h.contains("P"))
            {
                c = glb.colorA;
            } else if ((glb.isStrict(h, glb.CURRENT_DEMAND_FASE_B, glb.DEMAND_I)) && !h.contains("P"))
            {
                c = glb.colorB;
            } else if ((glb.isStrict(h, glb.CURRENT_DEMAND_FASE_C, glb.DEMAND_I)) && !h.contains("P"))
            {
                c = glb.colorC;
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

        double globalMaxInSelection = 0; // Para el auto-ajuste dinámico del eje Y

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

                // Filtro año 2000: si la fecha empieza por 2000, ignoramos el dato para estadísticas
                if (row[0].startsWith("2000"))
                {
                    continue;
                }

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

            if (count > 0)
            {
                if (max > globalMaxInSelection)
                {
                    globalMaxInSelection = max;
                }
            } else
            {
                min = 0;
                max = 0;
                dateMin = "-";
                dateMax = "-";
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

        // Auto-ajuste dinámico del eje Y basado en la selección actual (Máximo + 10%)
        if (globalMaxInSelection > 0)
        {
            double newMaxY = globalMaxInSelection * 1.1;
            // Solo actualizamos si hay un cambio significativo para evitar parpadeos
            if (Math.abs((Double) spnMaxY.getValue() - newMaxY) > 0.01)
            {
                spnMaxY.setValue(newMaxY);
            }
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

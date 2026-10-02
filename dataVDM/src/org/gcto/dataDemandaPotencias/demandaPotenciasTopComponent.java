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
import org.gcto.dataEnergias.energiasTopComponent;
import org.gcto.dataGlobal.FastChartPanel;
import org.gcto.dataGlobal.baseTopComponent;
import org.gcto.dataGlobal.glb;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component que visualiza el análisis de Demanda de Potencias Totales.
 * Especializado en: - Demanda por intervalo (DmP, DmQ, DmS) - Picos de demanda
 * (PDmP, PDmQ, PDmS) - Validación de fechas de pico (ignora año 2000)
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

    private final List<PhaseControl> phaseControls = new ArrayList<>();
    // private final List<SeriesControl> seriesControls = new ArrayList<>();
    private JComboBox<String> cmbFilter;
    private JSpinner spnMinY;
    private JSpinner spnMaxY;

    public demandaPotenciasTopComponent()
    {
        setName(Bundle.CTL_demandaPotenciasTopComponent());
        setToolTipText(Bundle.HINT_demandaPotenciasTopComponent());

        setupFilterControl();
        setupYRangeControls();
        // Configurar tabla de estadísticas para demanda: Ocultar Min, F.Min, PromA, VerProm, y extras
        hideStatsColumns(3, 4, 5, 6,  9, 10, 11);
    }

    private void setupFilterControl()
    {
        cmbFilter = new JComboBox<>(new String[]
        {
            "Todos", "Activa (P)", "Reactiva (Q)", "Aparente (S)"
        });
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
        pnlY.add(new JLabel("Escala Y:"));
        pnlY.add(spnMinY);
        pnlY.add(new JLabel("Max:"));
        pnlY.add(spnMaxY);

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 2);
        pnlSouth.add(pnlY, 2);
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
        if (masterHeaders == null || masterHeaders.length == 0)
        {
            return;
        }

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

            Color c = Color.GRAY;

            // Filtrar solo columnas de energía (evitar potencias instantáneas)
            if (h.contains(glb.DEMAND_P) && !h.contains("PDM"))
            {
                boolean match = false;

                if (filterUpper.contains("(P)"))
                {
                    match = (h.contains(glb.TOTAL_ACTIVE_POWER_DEMAND));
                    c = glb.colorAA;
                } else if (filterUpper.contains("(Q)"))
                {
                    match = h.contains(glb.TOTAL_REACTIVE_POWER_DEMAND);
                    c = glb.colorBB;
                } else if (filterUpper.contains("(S)"))
                {
                    match = h.contains(glb.TOTAL_APPARENT_POWER_DEMAND);
                    c = glb.colorCC;
                } else if (filterUpper.contains("TODOS"))
                {
                    if (h.contains(glb.TOTAL_ACTIVE_POWER_DEMAND))
                    {
                        match=true;
                         c = glb.colorAA;
                    }
                    if (h.contains(glb.TOTAL_REACTIVE_POWER_DEMAND))
                    {
                        match=true;
                         c = glb.colorBB;
                    }
                    if (h.contains(glb.TOTAL_APPARENT_POWER_DEMAND))
                    {
                        match=true;
                         c = glb.colorCC;
                    }
                    
                }

                //si no hay columnas continue
                if (!match)
                {
                    continue;
                }

                phaseControls.add(new PhaseControl(i, masterHeaders[i], c));
                chartIndices.add(i);
                chartNames.add(masterHeaders[i]);
                chartColors.add(c);
            }

            chartPanel.setSeries(chartIndices, chartNames, chartColors);
            updateStatsTableRows();
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

//    private static class SeriesControl
//    {
//
//        int colIdx, idxDate;
//        String name;
//        Color color;
//
//        SeriesControl(int ci, int idt, String n, Color c)
//        {
//            this.colIdx = ci;
//            this.idxDate = idt;
//            this.name = n;
//            this.color = c;
//        }
//    }
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

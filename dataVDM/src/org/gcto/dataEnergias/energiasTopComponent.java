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
 * Top component que visualiza el análisis de Energías. Filtra automáticamente
 * las columnas que contienen la palabra "ENERGY". Permite filtrar por grupos:
 * Active, Reactive y Apparent.
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
            "HINT_energiasTopComponent=Visualización de parámetros de energía (Active, Reactive, Apparent)"
        })
public final class energiasTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();
    private JComboBox<String> cmbFilter;

    public energiasTopComponent()
    {
        setName(Bundle.CTL_energiasTopComponent());
        setToolTipText(Bundle.HINT_energiasTopComponent());

        chartPanel.setAreaFillMode(true);
        setupFilterControl();
        hideStatsColumns(9, 10, 11);
    }

    private void setupFilterControl()
    {
        cmbFilter = new JComboBox<>(new String[]
        {
            "Todos", "Active", "Reactive", "Apparent"
        });
        cmbFilter.addActionListener(e ->
        {
            mapColumns();
            updateChartData();
            updateStatistics();
        });

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL));
        pnlSouth.add(new JLabel("Filtrar por:"));
        pnlSouth.add(cmbFilter);
    }

    @Override
    protected void mapColumns()
    {
        phaseControls.clear();
        statsModel.setRowCount(0);

        List<Integer> chartIndices = new ArrayList<>();
        List<String> chartNames = new ArrayList<>();
        List<Color> chartColors = new ArrayList<>();

        String filter = cmbFilter != null ? (String) cmbFilter.getSelectedItem() : "Todos";
        String filterUpper = filter.toUpperCase();

//        // Paleta extendida para cubrir las 9 columnas mencionadas
//        Color[] palette = {
//            glb.colorA, glb.colorB, glb.colorC,
//            Color.CYAN, Color.MAGENTA, Color.ORANGE,
//            Color.PINK, Color.LIGHT_GRAY, Color.WHITE
//        };
        //       int colorIdx = 0;
        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();
            if (h.contains("ENERGY"))
            {
                // Aplicar filtro de grupo con soporte para español y abreviaturas técnicas
                if (!filterUpper.equals("TODOS"))
                {
                    boolean match = false;
                    if (filterUpper.equals("ACTIVE"))
                    {
                        match = (h.contains("ACTIVE") || h.contains("ACTIVA") || h.contains(" EP")) && !h.contains("REACTIVE");
                    } else if (filterUpper.equals("REACTIVE"))
                    {
                        match = h.contains("REACTIVE") || h.contains("REACTIVA") || h.contains(" EQ");
                    } else if (filterUpper.equals("APPARENT"))
                    {
                        match = h.contains("APPARENT") || h.contains("APARENTE") || h.contains(" ES");
                    }
                    if (!match)
                    {
                        continue;
                    }
                }

//                Color c = palette[colorIdx % palette.length];
                Color c;

                // Detección robusta de fase para asignar color estándar (A, B, C)
                if (h.contains("EPA") || h.contains("EQA") || h.contains("ESA") || h.contains("L1") || h.contains("PHASE A") || h.contains(" FA"))
                {
                    c = glb.colorA;
                } else if (h.contains("EPB") || h.contains("EQB") || h.contains("ESB") || h.contains("L2") || h.contains("PHASE B") || h.contains(" FB"))
                {
                    c = glb.colorB;
                } else if (h.contains("EPC") || h.contains("EQC") || h.contains("ESC") || h.contains("L3") || h.contains("PHASE C") || h.contains(" FC"))
                {
                    c = glb.colorC;
                } else
                {
                    //colorIdx++; // Solo variamos el color de la paleta si no es una fase identificada
                    c = Color.ORANGE;
                }

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
            // Columnas: Color(0), Nombre(1), Ver(2), Min(3), F.Min(4), PromA(5), VerProm(6), Max(7), F.Max(8)...
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

            // Formateamos a 2 decimales para energía
            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 3);
            statsModel.setValueAt(dateMin, rowInModel, 4);
            statsModel.setValueAt(String.format("%.2f", avgArit), rowInModel, 5);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 7);
            statsModel.setValueAt(dateMax, rowInModel, 8);

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 6);
            if (showP)
            {
                trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color,
                        new BasicStroke(glb.grosLinProm, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
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

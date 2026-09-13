/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataFactoPotencia;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
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
 * Top component que visualiza el análisis de Factor de Potencia. Incluye FPA,
 * FPB, FPC y el PF Average calculado (Psum/Ssum). Refinado para visualización
 * técnica IND/CAP basada en el signo de Q.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataFactoPotencia//fp//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "fpTopComponent",
        iconBase = "org/gcto/dataFactoPotencia/FP.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataFactoPotencia.fpTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_fpAction",
        preferredID = "fpTopComponent"
)
@Messages(
        {
            "CTL_fpAction=factor de potencia",
            "CTL_fpTopComponent=Análisis de Factor de Potencia",
            "HINT_fpTopComponent=Visualización de Factor de Potencia por fase y promedio global"
        })
public final class fpTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();

    public fpTopComponent()
    {
        setName(Bundle.CTL_fpTopComponent());
        setToolTipText(Bundle.HINT_fpTopComponent());

        // Activar modo técnico de Factor de Potencia (Eje partido IND/CAP)
        chartPanel.setPowerFactorMode(true);

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
        List<Integer> signIndices = new ArrayList<>();
        List<String> chartNames = new ArrayList<>();
        List<Color> chartColors = new ArrayList<>();

        // 1. Localizar columnas de Reactiva (Q) para el signo
        int idxQA = -1, idxQB = -1, idxQC = -1, idxQSum = -1;
        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();
            if (isStrict(h, "QA", "REACTIVE"))
            {
                idxQA = i;
            } else if (isStrict(h, "QB", "REACTIVE"))
            {
                idxQB = i;
            } else if (isStrict(h, "QC", "REACTIVE"))
            {
                idxQC = i;
            } else if (h.contains("REACTIVE") && (h.contains("QSUM") || h.contains("Q SUM")))
            {
                idxQSum = i;
            }
        }

        // 2. Mapear Factores de Potencia
        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();
            Color c = null;
            int signIdx = -1;

            if (isStrictFP(h, "PFA"))
            {
                c = glb.colorA;
                signIdx = idxQA;
            } else if (isStrictFP(h, "PFB"))
            {
                c = glb.colorB;
                signIdx = idxQB;
            } else if (isStrictFP(h, "PFC"))
            {
                c = glb.colorC;
                signIdx = idxQC;
            } else if (isStrictFP(h, "AVERAGE") || h.contains("PF AVERAGE"))
            {
                c = Color.WHITE;
                signIdx = -1; // PF Average ya viene signado desde dataTopComponent
            }

            if (c != null)
            {
                final int colIdx = i;
                final int finalSignIdx = signIdx;
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
                signIndices.add(finalSignIdx);
                chartNames.add(colName);
                chartColors.add(phaseColor);
            }
        }

        chartPanel.setSeriesWithSign(chartIndices, signIndices, chartNames, chartColors);
        updateStatsTableRows();

        pnlPhaseSelection.revalidate();
        pnlPhaseSelection.repaint();
    }

    private boolean isStrict(String h, String key, String type)
    {
        if (!h.contains(type))
        {
            return false;
        }
        if (h.contains("ENERGY") || h.contains("VARH"))
        {
            return false;
        }
        return h.contains(" " + key) || h.contains(": " + key) || h.endsWith(" " + key) || h.endsWith(":" + key);
    }

    private boolean isStrictFP(String h, String key)
    {
        if (!h.contains("POWER FACTOR"))
        {
            return false;
        }
        String k = key.toUpperCase();
        if (k.equals("PFA"))
        {
            return h.contains("PFA")  || h.contains(" L1") ;
        }
        if (k.equals("PFB"))
        {
            return h.contains("PFB") || h.contains(" L2") ;
        }
        if (k.equals("PFC"))
        {
            return h.contains("PFC")  || h.contains(" L3") ;
        }
        if (k.equals("AVERAGE"))
        {
            return h.contains("AVERAGE") || h.contains("PROMEDIO") || h.contains("PF AVERAGE");
        }
        return h.contains(" " + key) || h.contains(": " + key) || h.endsWith(" " + key) || h.endsWith(":" + key);
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

            statsModel.setValueAt(String.format("%.3f", Math.abs(min)), rowInModel, 1);
            statsModel.setValueAt(dateMin, rowInModel, 2);
            statsModel.setValueAt(String.format("%.3f", Math.abs(avgArit)), rowInModel, 3);
            statsModel.setValueAt(String.format("%.3f", Math.abs(max)), rowInModel, 5);
            statsModel.setValueAt(dateMax, rowInModel, 6);

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 4);
            if (showP)
            {
                trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color,
                        new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
                        {
                            10, 5
                }, 0)));
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
        super.onExport();
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

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    void writeProperties(java.util.Properties p)
    {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p)
    {
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataPotReactiva;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import net.java.balloontip.TableCellBalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;
import org.gcto.dataGlobal.FastChartPanel;
import org.gcto.dataGlobal.baseTopComponent;
import org.gcto.dataGlobal.glb;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component que visualiza el análisis de potencia reactiva. Incluye
 * comparativa entre Q medida y Q calculada, Distorsión y Calidad IEEE 1459.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataPotReactiva//potReactiva//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "potReactivaTopComponent",
        iconBase = "org/gcto/dataPotReactiva/Q.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataPotReactiva.potReactivaTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_potReactivaAction",
        preferredID = "potReactivaTopComponent"
)
@Messages(
        {
            "CTL_potReactivaAction=potencia reactiva",
            "CTL_potReactivaTopComponent=Análisis de Potencia Reactiva",
            "HINT_potReactivaTopComponent=Análisis comparativo de potencia reactiva, distorsión y calidad"
        })
public final class potReactivaTopComponent extends baseTopComponent
{

    private final List<PhaseControlQ> phaseControls = new ArrayList<>();
    private TableCellBalloonTip currentBalloon = null;

    public potReactivaTopComponent()
    {
        setName(Bundle.CTL_potReactivaTopComponent());
        setToolTipText(Bundle.HINT_potReactivaTopComponent());

        chartPanel.setSymmetricY(true);
        chartPanel.setShowIndCapLabels(true);

        // Añadir columnas especiales (Índices 12 y 13 tras el rediseño)
        statsModel.addColumn("Distorsión %");
        statsModel.addColumn("Clasif. Calidad");

        setupTableEditors();

        tblStats.getColumnModel().getColumn(12).setPreferredWidth(100); // Distorsión
        tblStats.getColumnModel().getColumn(13).setPreferredWidth(160); // Clasificación

        tblStats.getTableHeader().addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                int col = tblStats.columnAtPoint(e.getPoint());
                if (col == 12)
                {
                    tblStats.getTableHeader().setToolTipText("Factor de Potencia de Distorsión y Relación D/S");
                } else
                {
                    tblStats.getTableHeader().setToolTipText(null);
                }
            }
        });

        setupBalloonTips();
    }

    private void setupBalloonTips()
    {
        tblStats.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                int row = tblStats.rowAtPoint(e.getPoint());
                int col = tblStats.columnAtPoint(e.getPoint());

                if (row != -1 && col == 13)
                { // Nueva columna de Clasificación
                    Object val = tblStats.getValueAt(row, col);
                    if (val != null && !val.toString().equals("-"))
                    {
                        int idx = glb.clasificacion.indexOf(val.toString());
                        if (idx != -1 && idx < glb.tipsClasificacion.size())
                        {
                            showBalloon(row, col, glb.tipsClasificacion.get(idx));
                            return;
                        }
                    }
                }
                hideBalloon();
            }
        });

        tblStats.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseExited(MouseEvent e)
            {
                hideBalloon();
            }
        });
    }

    private void showBalloon(int row, int col, String text)
    {
        if (currentBalloon != null)
        {
            if (currentBalloon.isVisible() && currentBalloon.getAttachedRectangle().equals(tblStats.getCellRect(row, col, true)))
            {
                return;
            }
            currentBalloon.closeBalloon();
        }
        RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, new Color(40, 40, 40), Color.GRAY);
        JLabel lbl = new JLabel("<html><div style='width:260px; padding:8px; color:white; font-family:sans-serif; font-size:10pt;'>" + text + "</div></html>");
        currentBalloon = new TableCellBalloonTip(tblStats, lbl, row, col, style, TableCellBalloonTip.Orientation.RIGHT_ABOVE, TableCellBalloonTip.AttachLocation.ALIGNED, 15, 15, false);
        currentBalloon.setVisible(true);
    }

    private void hideBalloon()
    {
        if (currentBalloon != null)
        {
            currentBalloon.closeBalloon();
            currentBalloon = null;
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

        int idxSA = -1, idxSB = -1, idxSC = -1, idxSSum = -1;
        int idxQA_med = -1, idxQB_med = -1, idxQC_med = -1, idxQSum_med = -1;
        int idxQA_calc = -1, idxQB_calc = -1, idxQC_calc = -1, idxQSum_calc = -1;

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();
            if (glb.isStrict(h, glb.APPARENT_POWER_FASE_A, glb.APPARENT_POWER))
            {
                idxSA = i;
            } else if (glb.isStrict(h, glb.APPARENT_POWER_FASE_B, glb.APPARENT_POWER))
            {
                idxSB = i;
            } else if (glb.isStrict(h, glb.APPARENT_POWER_FASE_C, glb.APPARENT_POWER))
            {
                idxSC = i;
            } else if (glb.isStrict(h, glb.APPARENT_POWER_SUM, glb.APPARENT_POWER))
            {
                idxSSum = i;
            }

            if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_A, glb.REACTIVE_POWER))
            {
                idxQA_med = i;
            } else if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_B, glb.REACTIVE_POWER))
            {
                idxQB_med = i;
            } else if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_C, glb.REACTIVE_POWER))
            {
                idxQC_med = i;
            } else if (glb.isStrict(h, glb.REACTIVE_POWER_SUM, glb.REACTIVE_POWER))
            {
                idxQSum_med = i;
            }

            if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_A, glb.REACTIVE_POWER_CALC))
            {
                idxQA_calc = i;
            } else if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_B, glb.REACTIVE_POWER_CALC))
            {
                idxQB_calc = i;
            } else if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_C, glb.REACTIVE_POWER_CALC))
            {
                idxQC_calc = i;
            } else if (glb.isStrict(h, glb.REACTIVE_POWER_SUM, glb.REACTIVE_POWER_CALC))
            {
                idxQSum_calc = i;
            }

        }

        addPhaseSeries("QA medido", idxQA_med, idxQA_calc, idxSA, glb.colorA, false, chartIndices, chartNames, chartColors);
        addPhaseSeries("QB medido", idxQB_med, idxQB_calc, idxSB, glb.colorB, false, chartIndices, chartNames, chartColors);
        addPhaseSeries("QC medido", idxQC_med, idxQC_calc, idxSC, glb.colorC, false, chartIndices, chartNames, chartColors);
        addPhaseSeries("QSum medido", idxQSum_med, idxQSum_calc, idxSSum, Color.CYAN, false, chartIndices, chartNames, chartColors);

        addPhaseSeries("QA Calculada", idxQA_calc, -1, -1, new Color(255, 165, 0), true, chartIndices, chartNames, chartColors);
        addPhaseSeries("QB Calculada", idxQB_calc, -1, -1, new Color(0, 191, 255), true, chartIndices, chartNames, chartColors);
        addPhaseSeries("QC Calculada", idxQC_calc, -1, -1, new Color(255, 20, 147), true, chartIndices, chartNames, chartColors);
        addPhaseSeries("QSum Calculada", idxQSum_calc, -1, -1, new Color(50, 205, 50), true, chartIndices, chartNames, chartColors);

        chartPanel.setSeries(chartIndices, chartNames, chartColors);
        updateStatsTableRows();
    }

    private void addPhaseSeries(String name, int colIdx, int idxCalc, int idxS, Color color, boolean isCalc, List<Integer> indices, List<String> names, List<Color> colors)
    {
        if (colIdx == -1)
        {
            return;
        }
        phaseControls.add(new PhaseControlQ(colIdx, idxCalc, idxS, name, color, isCalc));
        indices.add(colIdx);
        names.add(name);
        colors.add(color);
    }

    private void updateStatsTableRows()
    {
        statsModel.setRowCount(0);
        for (PhaseControlQ pc : phaseControls)
        {
            // Columnas: Color(0), Nombre(1), Ver(2), Min(3), F.Min(4), PromA(5), VerProm(6), Max(7), F.Max(8), PromS(9), %Nivel(10), VerS(11), Dist(12), Clasif(13)
            statsModel.addRow(new Object[]
            {
                pc.color, pc.name, true, "0.0", "-", "0.0", false, "0.0", "-", "0.0", 0, false, "-", "-"
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
        for (PhaseControlQ pc : phaseControls)
        {
            boolean isVisible = (boolean) statsModel.getValueAt(rowInModel, 2);
            if (!isVisible)
            {
                rowInModel++;
                continue;
            }

            double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, absMax = 0;
            double sumMed = 0, sumCalc = 0, sumS = 0;
            String dateMin = "-", dateMax = "-";
            int count = 0;

            for (int r = sIdx; r <= eIdx; r++)
            {
                String[] row = masterData.get(r);
                double valMed = glb.parseDoubleSafe(row[pc.colIdx]);
                if (!pc.isCalculated)
                {
                    sumCalc += (pc.idxCalc != -1) ? glb.parseDoubleSafe(row[pc.idxCalc]) : 0;
                    sumS += (pc.idxS != -1) ? glb.parseDoubleSafe(row[pc.idxS]) : 0;
                }
                if (valMed < min)
                {
                    min = valMed;
                    dateMin = row[0] + " " + row[1];
                }
                if (valMed > max)
                {
                    max = valMed;
                    dateMax = row[0] + " " + row[1];
                }
                if (Math.abs(valMed) > absMax)
                {
                    absMax = Math.abs(valMed);
                }
                sumMed += valMed;
                count++;
            }

            double avgMed = count > 0 ? sumMed / count : 0;
            double avgCalc = count > 0 ? sumCalc / count : 0;
            double avgS = count > 0 ? sumS / count : 1.0;

            int levelPercent = (int) statsModel.getValueAt(rowInModel, 10);
            double threshold = absMax * (levelPercent / 100.0);
            double sumAbove = 0;
            int countAbove = 0;

            for (int r = sIdx; r <= eIdx; r++)
            {
                double val = glb.parseDoubleSafe(masterData.get(r)[pc.colIdx]);
                if (Math.abs(val) >= threshold)
                {
                    sumAbove += val;
                    countAbove++;
                }
            }
            double avgAbove = countAbove > 0 ? sumAbove / countAbove : 0;

            // Actualizar Tabla (Nuevos Índices)
            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 3);
            statsModel.setValueAt(dateMin, rowInModel, 4);
            statsModel.setValueAt(String.format("%.2f", avgMed), rowInModel, 5);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 7);
            statsModel.setValueAt(dateMax, rowInModel, 8);
            statsModel.setValueAt(String.format("%.2f", avgAbove), rowInModel, 9);

            if (!pc.isCalculated && pc.idxCalc != -1 && pc.idxS != -1)
            {
                double dVal = Math.sqrt(Math.max(0, Math.pow(avgCalc, 2) - Math.pow(avgMed, 2)));
                double distPercent = (avgS != 0) ? (dVal / avgS) * 100 : 0;
                String clasif = glb.clasificacion.get(0);
                if (distPercent >= 30)
                {
                    clasif = glb.clasificacion.get(3);
                } else if (distPercent >= 15)
                {
                    clasif = glb.clasificacion.get(2);
                } else if (distPercent >= 5)
                {
                    clasif = glb.clasificacion.get(1);
                }
                statsModel.setValueAt(String.format("%.1f%%", distPercent), rowInModel, 12);
                statsModel.setValueAt(clasif, rowInModel, 13);
            } else
            {
                statsModel.setValueAt("-", rowInModel, 12);
                statsModel.setValueAt("-", rowInModel, 13);
            }

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 6);
            boolean showS = (boolean) statsModel.getValueAt(rowInModel, 11);
            if (showP)
            {
                trendLines.add(new FastChartPanel.TrendLine(avgMed, pc.color, new BasicStroke(glb.grosLinProm, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
                {
                    10, 5
                }, 0)));
            }
            if (showS)
            {
                trendLines.add(new FastChartPanel.TrendLine(avgAbove, pc.color, new BasicStroke(glb.grosLinProm, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
                {
                    2, 4
                }, 0)));
            }

            rowInModel++;
        }
        chartPanel.setTrendLines(trendLines);
        tblStats.repaint();
    }

    private static class PhaseControlQ
    {

        int colIdx, idxCalc, idxS;
        String name;
        Color color;
        boolean isCalculated;

        PhaseControlQ(int colIdx, int idxCalc, int idxS, String name, Color color, boolean isCalculated)
        {
            this.colIdx = colIdx;
            this.idxCalc = idxCalc;
            this.idxS = idxS;
            this.name = name;
            this.color = color;
            this.isCalculated = isCalculated;
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

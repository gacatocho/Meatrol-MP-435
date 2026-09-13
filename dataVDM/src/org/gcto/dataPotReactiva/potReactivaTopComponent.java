/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataPotReactiva;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
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
 * Top component que visualiza el análisis de potencia reactiva.
 * Incluye comparativa entre Q medida y Q calculada, Distorsión y Calidad IEEE 1459.
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
        
        // Activar escalado simétrico para ver claramente + y - (Inductivo/Capacitivo)
        chartPanel.setSymmetricY(true);
        // Mostrar rótulos IND / CAP
        chartPanel.setShowIndCapLabels(true);
        
        // Añadir columnas especiales a la tabla de estadísticas
        statsModel.addColumn("Distorsión %");
        statsModel.addColumn("Clasif. Calidad");
        
        // Re-aplicamos los editores de la clase base
        setupTableEditors();
        
        // Ajustar anchos de columnas especiales
        tblStats.getColumnModel().getColumn(10).setPreferredWidth(100); // Distorsión
        tblStats.getColumnModel().getColumn(11).setPreferredWidth(160); // Clasificación
        
        // Configurar ToolTip específico para el encabezado de la columna de Distorsión
        tblStats.getTableHeader().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = tblStats.columnAtPoint(e.getPoint());
                if (col == 10) {
                    tblStats.getTableHeader().setToolTipText("Factor de Potencia de Distorsión y Relación D/S");
                } else {
                    tblStats.getTableHeader().setToolTipText(null);
                }
            }
        });
        
        setupBalloonTips();
    }

    private void setupBalloonTips() {
        tblStats.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = tblStats.rowAtPoint(e.getPoint());
                int col = tblStats.columnAtPoint(e.getPoint());
                
                if (row != -1 && col == 11) {
                    Object val = tblStats.getValueAt(row, col);
                    if (val != null && !val.toString().equals("-")) {
                        int idx = glb.clasificacion.indexOf(val.toString());
                        if (idx != -1 && idx < glb.tipsClasificacion.size()) {
                            showBalloon(row, col, glb.tipsClasificacion.get(idx));
                            return;
                        }
                    }
                }
                hideBalloon();
            }
        });
        
        tblStats.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hideBalloon();
            }
        });
    }

    private void showBalloon(int row, int col, String text) {
        if (currentBalloon != null) {
            if (currentBalloon.isVisible() && currentBalloon.getAttachedRectangle().equals(tblStats.getCellRect(row, col, true))) {
                return; 
            }
            currentBalloon.closeBalloon();
        }
        
        RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, new Color(40, 40, 40), Color.GRAY);
        JLabel lbl = new JLabel("<html><div style='width:260px; padding:8px; color:white; font-family:sans-serif; font-size:10pt;'>" + text + "</div></html>");
        
        currentBalloon = new TableCellBalloonTip(tblStats, lbl, row, col, style, 
                TableCellBalloonTip.Orientation.RIGHT_ABOVE, 
                TableCellBalloonTip.AttachLocation.ALIGNED, 15, 15, false);
        currentBalloon.setVisible(true);
    }

    private void hideBalloon() {
        if (currentBalloon != null) {
            currentBalloon.closeBalloon();
            currentBalloon = null;
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

        int idxSA = -1, idxSB = -1, idxSC = -1, idxSSum = -1;
        int idxQA_med = -1, idxQB_med = -1, idxQC_med = -1, idxQSum_med = -1;
        int idxQA_calc = -1, idxQB_calc = -1, idxQC_calc = -1, idxQSum_calc = -1;

        for (int i = 0; i < masterHeaders.length; i++) {
            String h = masterHeaders[i].toUpperCase();
            
            if (isStrict(h, "SA", "APPARENT")) idxSA = i;
            else if (isStrict(h, "SB", "APPARENT")) idxSB = i;
            else if (isStrict(h, "SC", "APPARENT")) idxSC = i;
            else if (isStrict(h, "SSUM", "APPARENT") || isStrict(h, "S SUM", "APPARENT")) idxSSum = i;
            
            if (isStrict(h, "QA", "REACTIVE") && !h.contains("CALC")) idxQA_med = i;
            else if (isStrict(h, "QB", "REACTIVE") && !h.contains("CALC")) idxQB_med = i;
            else if (isStrict(h, "QC", "REACTIVE") && !h.contains("CALC")) idxQC_med = i;
            else if ((h.contains("QSUM") || h.contains("Q SUM")) && h.contains("REACTIVE") && !h.contains("CALC")) idxQSum_med = i;
            
            if (h.contains("REACTIVEPOWERCALC") && h.contains("QA")) idxQA_calc = i;
            else if (h.contains("REACTIVEPOWERCALC") && h.contains("QB")) idxQB_calc = i;
            else if (h.contains("REACTIVEPOWERCALC") && h.contains("QC")) idxQC_calc = i;
            else if (h.contains("REACTIVEPOWERCALC") && h.contains("QSUM")) idxQSum_calc = i;
        }

        // Añadir series medidas
        addPhaseSeries("QA medido", idxQA_med, idxQA_calc, idxSA, glb.colorA, false, chartIndices, chartNames, chartColors);
        addPhaseSeries("QB medido", idxQB_med, idxQB_calc, idxSB, glb.colorB, false, chartIndices, chartNames, chartColors);
        addPhaseSeries("QC medido", idxQC_med, idxQC_calc, idxSC, glb.colorC, false, chartIndices, chartNames, chartColors);
        addPhaseSeries("QSum medido", idxQSum_med, idxQSum_calc, idxSSum, Color.CYAN, false, chartIndices, chartNames, chartColors);
        
        // Añadir series calculadas (seleccionadas por defecto para enriquecer la comparativa)
        addPhaseSeries("QA Calculada", idxQA_calc, -1, -1, new Color(255, 165, 0), true, chartIndices, chartNames, chartColors); // Naranja vibrante
        addPhaseSeries("QB Calculada", idxQB_calc, -1, -1, new Color(0, 191, 255), true, chartIndices, chartNames, chartColors); // Deep Sky Blue
        addPhaseSeries("QC Calculada", idxQC_calc, -1, -1, new Color(255, 20, 147), true, chartIndices, chartNames, chartColors); // Deep Pink
        addPhaseSeries("QSum Calculada", idxQSum_calc, -1, -1, new Color(50, 205, 50), true, chartIndices, chartNames, chartColors); // Lime Green

        chartPanel.setSeries(chartIndices, chartNames, chartColors);
        updateStatsTableRows();

        pnlPhaseSelection.revalidate();
        pnlPhaseSelection.repaint();
    }
    
    private void addPhaseSeries(String name, int colIdx, int idxCalc, int idxS, Color color, boolean isCalc, List<Integer> indices, List<String> names, List<Color> colors) {
        if (colIdx == -1) return;
        
        JPanel pnlItem = new JPanel(new BorderLayout(5, 0));
        JCheckBox chk = new JCheckBox(name);
        chk.setSelected(true); // Enriquecemos el formulario mostrando todo al inicio

        JLabel lblColor = new JLabel(" ■ ");
        lblColor.setForeground(color);
        lblColor.setPreferredSize(new Dimension(25, 20));

        pnlItem.add(lblColor, BorderLayout.WEST);
        pnlItem.add(chk, BorderLayout.CENTER);
        pnlPhaseSelection.add(pnlItem);

        PhaseControlQ pc = new PhaseControlQ(chk, colIdx, idxCalc, idxS, name, color, isCalc);
        phaseControls.add(pc);

        chk.addActionListener(e -> {
            chartPanel.setSeriesVisible(colIdx, chk.isSelected());
            updateStatsTableRows();
        });

        indices.add(colIdx);
        names.add(name);
        colors.add(color);
    }

    private boolean isStrict(String h, String key, String type) {
        if (!h.contains(type)) return false;
        if (h.contains("ENERGY") || h.contains("VARH") || h.contains("VAH")) return false;
        return h.contains(" " + key) || h.contains(": " + key) || h.endsWith(" " + key) || h.endsWith(":" + key);
    }

    private void updateStatsTableRows()
    {
        statsModel.setRowCount(0);
        for (PhaseControlQ pc : phaseControls)
        {
            if (pc.checkBox.isSelected())
            {
                // Columnas: Nombre(0), Min(1), F.Min(2), PromA(3), Ver Promedio(4), Max(5), F.Max(6), Promedio sobre %(7), % de Nivel(8), Ver promedio sobre %(9), Dist(10), Clasif(11)
                statsModel.addRow(new Object[]
                {
                    pc.name, "0.0", "-", "0.0", false, "0.0", "-", "0.0", Integer.valueOf(0), false, "-", "-"
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
        for (PhaseControlQ pc : phaseControls)
        {
            if (!pc.checkBox.isSelected()) continue;

            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;
            double absMax = 0; 
            double sumMed = 0, sumCalc = 0, sumS = 0;
            String dateMin = "-", dateMax = "-";
            int count = 0;

            for (int r = sIdx; r <= eIdx; r++)
            {
                String[] row = masterData.get(r);
                double valMed = glb.parseDoubleSafe(row[pc.colIdx]);
                
                if (!pc.isCalculated) {
                    double valCalc = (pc.idxCalc != -1) ? glb.parseDoubleSafe(row[pc.idxCalc]) : 0;
                    double valS = (pc.idxS != -1) ? glb.parseDoubleSafe(row[pc.idxS]) : 0;
                    sumCalc += valCalc;
                    sumS += valS;
                }

                if (valMed < min) { min = valMed; dateMin = row[0] + " " + row[1]; }
                if (valMed > max) { max = valMed; dateMax = row[0] + " " + row[1]; }
                
                if (Math.abs(valMed) > absMax) absMax = Math.abs(valMed);
                
                sumMed += valMed;
                count++;
            }

            double avgMed = count > 0 ? sumMed / count : 0;
            double avgCalc = count > 0 ? sumCalc / count : 0;
            double avgS = count > 0 ? sumS / count : 1.0; 

            // Lógica de Promedio sobre % de Nivel
            Object levelVal = statsModel.getValueAt(rowInModel, 8);
            int levelPercent = 0;
            if (levelVal instanceof Number) {
                levelPercent = ((Number) levelVal).intValue();
            } else if (levelVal != null) {
                try {
                    levelPercent = Integer.parseInt(levelVal.toString().replace("%", "").replace("▼", "").trim());
                } catch (Exception e) {}
            }
            
            double threshold = absMax * (levelPercent / 100.0);
            double sumAbove = 0;
            int countAbove = 0;

            for (int r = sIdx; r <= eIdx; r++) {
                double val = glb.parseDoubleSafe(masterData.get(r)[pc.colIdx]);
                if (Math.abs(val) >= threshold) {
                    sumAbove += val;
                    countAbove++;
                }
            }
            double avgAbove = countAbove > 0 ? sumAbove / countAbove : 0;

            // Actualizar Tabla (Comunes)
            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 1);
            statsModel.setValueAt(dateMin, rowInModel, 2);
            statsModel.setValueAt(String.format("%.2f", avgMed), rowInModel, 3);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 5);
            statsModel.setValueAt(dateMax, rowInModel, 6);
            statsModel.setValueAt(String.format("%.2f", avgAbove), rowInModel, 7);

            // Cálculo de Distorsión y Clasificación (Solo para filas MEDIDAS)
            if (!pc.isCalculated && pc.idxCalc != -1 && pc.idxS != -1) {
                double dVal = Math.sqrt(Math.max(0, Math.pow(avgCalc, 2) - Math.pow(avgMed, 2)));
                double distPercent = (avgS != 0) ? (dVal / avgS) * 100 : 0;

                String clasif = glb.clasificacion.get(0);
                if (distPercent >= 30) clasif = glb.clasificacion.get(3);
                else if (distPercent >= 15) clasif = glb.clasificacion.get(2);
                else if (distPercent >= 5) clasif = glb.clasificacion.get(1);

                statsModel.setValueAt(String.format("%.1f%%", distPercent), rowInModel, 10);
                statsModel.setValueAt(clasif, rowInModel, 11);
            } else {
                statsModel.setValueAt("-", rowInModel, 10);
                statsModel.setValueAt("-", rowInModel, 11);
            }

            // Gestionar Líneas de Tendencia
            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 4);
            boolean showS = (boolean) statsModel.getValueAt(rowInModel, 9);
            
            if (showP) {
                trendLines.add(new FastChartPanel.TrendLine(avgMed, pc.color, 
                    new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10, 5}, 0)));
            }
            if (showS) {
                trendLines.add(new FastChartPanel.TrendLine(avgAbove, pc.color, 
                    new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2, 4}, 0)));
            }

            rowInModel++;
        }
        chartPanel.setTrendLines(trendLines);
        tblStats.repaint(); 
    }

    private static class PhaseControlQ
    {
        JCheckBox checkBox;
        int colIdx, idxCalc, idxS;
        String name;
        Color color;
        boolean isCalculated;

        PhaseControlQ(JCheckBox checkBox, int colIdx, int idxCalc, int idxS, String name, Color color, boolean isCalculated)
        {
            this.checkBox = checkBox;
            this.colIdx = colIdx;
            this.idxCalc = idxCalc;
            this.idxS = idxS;
            this.name = name;
            this.color = color;
            this.isCalculated = isCalculated;
        }
    }

    void writeProperties(java.util.Properties p) { p.setProperty("version", "1.0"); }
    void readProperties(java.util.Properties p) {}
}

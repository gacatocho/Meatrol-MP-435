/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataCorrientes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
 * Top component que visualiza el análisis de corrientes. Incluye Diagrama
 * Fasorial interactivo sincronizado con el cursor.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataCorrientes//corrientes//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "corrientesTopComponent",
        iconBase = "org/gcto/dataCorrientes/corriente.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataCorrientes.corrientesTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_corrientesAction",
        preferredID = "corrientesTopComponent"
)
@Messages(
        {
            "CTL_corrientesAction=corrientes",
            "CTL_corrientesTopComponent=Análisis de Corrientes",
            "HINT_corrientesTopComponent=Visualización y análisis de corrientes por fase y neutro"
        })
public final class corrientesTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();
    private JCheckBox chkPhasor;
    private PhasorFrame phasorFrame;

    // Índices para cálculo fasorial
    private int idxUA = -1, idxUB = -1, idxUC = -1;
    private int idxIA = -1, idxIB = -1, idxIC = -1;
    private int idxFPA = -1, idxFPB = -1, idxFPC = -1;
    private int idxQA = -1, idxQB = -1, idxQC = -1;

    public corrientesTopComponent()
    {
        setName(Bundle.CTL_corrientesTopComponent());
        setToolTipText(Bundle.HINT_corrientesTopComponent());

        setupPhasorControl();

        // Sincronizar diagrama fasorial con el cursor
        chartPanel.setCursorDataListener((dataIdx, activeSeries) ->
        {
            if (phasorFrame != null && phasorFrame.isVisible())
            {
                if (dataIdx >= 0 && dataIdx < masterData.size())
                {
                    phasorFrame.updateData(masterData.get(dataIdx));
                } else
                {
                    phasorFrame.updateData(null);
                }
            }
        });
    }

    private void setupPhasorControl()
    {
        chkPhasor = new JCheckBox("Ver Diagrama Fasorial");
        chkPhasor.addActionListener(e -> togglePhasorFrame());

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 0);
        pnlSouth.add(chkPhasor, 0);
    }

    private void togglePhasorFrame()
    {
        if (chkPhasor.isSelected())
        {
            if (phasorFrame == null)
            {
                phasorFrame = new PhasorFrame();
                phasorFrame.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        chkPhasor.setSelected(false);
                        phasorFrame = null;
                    }
                });
            }
            phasorFrame.updateTheme();
            phasorFrame.setVisible(true);
        } else
        {
            if (phasorFrame != null)
            {
                phasorFrame.dispose();
                phasorFrame = null;
            }
        }
    }

    @Override
    public void updateTheme()
    {
        super.updateTheme();
        if (phasorFrame != null)
        {
            phasorFrame.updateTheme();
        }
    }

    @Override
    protected void mapColumns()
    {
        phaseControls.clear();
        statsModel.setRowCount(0);

        // Reset de índices de cálculo
        idxUA = -1;
        idxUB = -1;
        idxUC = -1;
        idxIA = -1;
        idxIB = -1;
        idxIC = -1;
        idxFPA = -1;
        idxFPB = -1;
        idxFPC = -1;
        idxQA = -1;
        idxQB = -1;
        idxQC = -1;

        List<Integer> chartIndices = new ArrayList<>();
        List<String> chartNames = new ArrayList<>();
        List<Color> chartColors = new ArrayList<>();

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();

            // Mapeo para Tendencia
            Color c = null;
            if (glb.isStrict(h, glb.CORRIENTE_FASE_A, glb.CORRIENTE))
            {
                c = glb.colorA;
                idxIA = i;
            } else if (glb.isStrict(h, glb.CORRIENTE_FASE_B, glb.CORRIENTE))
            {
                c = glb.colorB;
                idxIB = i;
            } else if (glb.isStrict(h, glb.CORRIENTE_FASE_C, glb.CORRIENTE))
            {
                c = glb.colorC;
                idxIC = i;
            } else if (glb.isStrict(h, glb.CORRIENTE_NEUTRO, glb.CORRIENTE))
            {
                c = glb.colorN;
            }

            if (c != null)
            {
                phaseControls.add(new PhaseControl(i, masterHeaders[i], c));
                chartIndices.add(i);
                chartNames.add(masterHeaders[i]);
                chartColors.add(c);
            }

            // Mapeo para Fasores (Tensiones, FP y Reactiva)
            if (glb.isStrict(h, glb.TENSION_FASE_A, glb.TENSION))
            {
                idxUA = i;
            } else if (glb.isStrict(h, glb.TENSION_FASE_B, glb.TENSION))
            {
                idxUB = i;
            } else if (glb.isStrict(h, glb.TENSION_FASE_C, glb.TENSION))
            {
                idxUC = i;
            }

            if (glb.isStrict(h, glb.POWER_FACTOR_FASE_A, glb.POWER_FACTOR))
            {
                idxFPA = i;
            } else if (glb.isStrict(h, glb.POWER_FACTOR_FASE_B, glb.POWER_FACTOR))
            {
                idxFPB = i;
            } else if (glb.isStrict(h, glb.POWER_FACTOR_FASE_C, glb.POWER_FACTOR))
            {
                idxFPC = i;
            }

            if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_A, glb.REACTIVE_POWER))
            {
                idxQA = i;
            } else if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_B, glb.REACTIVE_POWER))
            {
                idxQB = i;
            } else if (glb.isStrict(h, glb.REACTIVE_POWER_FASE_C, glb.REACTIVE_POWER))
            {
                idxQC = i;
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
            int levelPercent = (int) statsModel.getValueAt(rowInModel, 10);
            double threshold = max * (levelPercent / 100.0);
            double sumAbove = 0;
            int countAbove = 0;

            for (int r = sIdx; r <= eIdx; r++)
            {
                double val = glb.parseDoubleSafe(masterData.get(r)[colIdx]);
                if (val >= threshold)
                {
                    sumAbove += val;
                    countAbove++;
                }
            }
            double avgAbove = countAbove > 0 ? sumAbove / countAbove : 0;

            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 3);
            statsModel.setValueAt(dateMin, rowInModel, 4);
            statsModel.setValueAt(String.format("%.2f", avgArit), rowInModel, 5);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 7);
            statsModel.setValueAt(dateMax, rowInModel, 8);
            statsModel.setValueAt(String.format("%.2f", avgAbove), rowInModel, 9);

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 6);
            boolean showS = (boolean) statsModel.getValueAt(rowInModel, 11);

            if (showP)
            {
                trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color, new BasicStroke(glb.grosLinProm, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
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
    }

    @Override
    public void componentClosed()
    {
        super.componentClosed();
        if (phasorFrame != null)
        {
            phasorFrame.dispose();
            phasorFrame = null;
        }
    }

    /**
     * Ventana flotante no modal para el Diagrama Fasorial.
     */
    private class PhasorFrame extends JFrame
    {

        private final PhasorPanel panel;

        public PhasorFrame()
        {
            setTitle("Diagrama Fasorial - Tiempo Real");
            setSize(600, 650);
            setAlwaysOnTop(true);
            setResizable(false);
            panel = new PhasorPanel();
            add(panel);
            setLocationRelativeTo(corrientesTopComponent.this);
        }

        public void updateData(String[] row)
        {
            panel.setData(row);
        }

        public void updateTheme()
        {
            panel.setBackground(glb.darkMode ? new Color(33, 33, 33) : Color.WHITE);
            panel.repaint();
        }
    }

    /**
     * Panel de dibujo para los fasores.
     */
    private class PhasorPanel extends JPanel
    {

        private String[] rowData = null;

        public PhasorPanel()
        {
            setBackground(new Color(33, 33, 33));
        }

        public void setData(String[] row)
        {
            this.rowData = row;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int cx = w / 2, cy = h / 2 - 20;
            int radius = 200;

            Color textColor = glb.darkMode ? Color.WHITE : Color.BLACK;
            Color gridColor = glb.darkMode ? new Color(60, 60, 60) : new Color(200, 200, 200);

            // Dibujar Círculo de Referencia y Ejes
            g2.setColor(gridColor);
            g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]
            {
                5
            }, 0));
            g2.drawLine(cx - radius - 10, cy, cx + radius + 10, cy);
            g2.drawLine(cx, cy - radius - 10, cx, cy + radius + 10);
            g2.setStroke(new BasicStroke(1.0f));

            if (rowData == null)
            {
                g2.setColor(textColor);
                g2.setFont(new Font("Dialog", Font.BOLD, 14));
                g2.drawString("Mueva el cursor sobre la tendencia", cx - 110, cy);
                return;
            }

            // Título con tiempo
            g2.setColor(textColor);
            g2.setFont(new Font("Dialog", Font.BOLD, 14));
            g2.drawString("Fasores en: " + rowData[0] + " " + rowData[1], 20, 30);

            // --- DIBUJO DE FASORES ---
            // 1. Tensiones (Referencia: UA=0, UB=-120, UC=120)
            drawPhasor(g2, cx, cy, radius, 0, "UA", Color.GREEN, true, 0);
            drawPhasor(g2, cx, cy, radius, -120, "UB", Color.BLACK, true, 0);
            drawPhasor(g2, cx, cy, radius, 120, "UC", Color.ORANGE, true, 0);

            // 2. Corrientes y Acumulación para Neutro
            double inX = 0, inY = 0;

            // Fase A
            double angleA = calculateCurrentAngle(0, idxFPA, idxQA);
            double magA = (idxIA != -1) ? glb.parseDoubleSafe(rowData[idxIA]) : 0;
            drawPhasor(g2, cx, cy, (int) (radius * 0.85), angleA, "IA", glb.colorA, false, magA);
            inX += magA * Math.cos(Math.toRadians(angleA));
            inY += magA * Math.sin(Math.toRadians(angleA));

            // Fase B
            double angleB = calculateCurrentAngle(-120, idxFPB, idxQB);
            double magB = (idxIB != -1) ? glb.parseDoubleSafe(rowData[idxIB]) : 0;
            drawPhasor(g2, cx, cy, (int) (radius * 0.85), angleB, "IB", glb.colorB, false, magB);
            inX += magB * Math.cos(Math.toRadians(angleB));
            inY += magB * Math.sin(Math.toRadians(angleB));

            // Fase C
            double angleC = calculateCurrentAngle(120, idxFPC, idxQC);
            double magC = (idxIC != -1) ? glb.parseDoubleSafe(rowData[idxIC]) : 0;
            drawPhasor(g2, cx, cy, (int) (radius * 0.85), angleC, "IC", glb.colorC, false, magC);
            inX += magC * Math.cos(Math.toRadians(angleC));
            inY += magC * Math.sin(Math.toRadians(angleC));

            // 3. Neutro Resultante (Suma Vectorial)
            double magN = Math.sqrt(inX * inX + inY * inY);
            double angleN = Math.toDegrees(Math.atan2(inY, inX));
            if (magN > 0.1)
            {
                drawPhasor(g2, cx, cy, (int) (radius * 0.7), angleN, "In (Calc)", glb.colorN, false, magN);
            }

            drawPFTable(g2);
        }

        private double calculateCurrentAngle(double vAngle, int idxFP, int idxQ)
        {
            if (idxFP == -1)
            {
                return vAngle;
            }
            double fp = glb.parseDoubleSafe(rowData[idxFP]);
            double q = (idxQ != -1) ? glb.parseDoubleSafe(rowData[idxQ]) : 0;
            double phi = Math.toDegrees(Math.acos(Math.min(1.0, Math.abs(fp))));
            return (q >= 0) ? vAngle - phi : vAngle + phi; // +Q = Inductivo (atrasa), -Q = Capacitivo (adelanta)
        }

        private void drawPhasor(Graphics2D g2, int cx, int cy, int radius, double angleDeg, String label, Color color, boolean isVoltage, double magnitude)
        {
            double rad = Math.toRadians(angleDeg);
            int x = cx + (int) (radius * Math.cos(rad));
            int y = cy - (int) (radius * Math.sin(rad)); // Y invertida en Swing

            Color drawColor = chartPanel.getDisplayColor(color);
            g2.setColor(drawColor);
            g2.setStroke(new BasicStroke(isVoltage ? 1.5f : 3.5f));
            g2.drawLine(cx, cy, x, y);

            // Punta de flecha
            drawArrowHead(g2, x, y, rad, drawColor);

            // Etiqueta en la punta con fondo HUD para legibilidad
            g2.setFont(new Font("Dialog", Font.BOLD, 12));
            String info = label;
            if (!isVoltage)
            {
                info += String.format(": %.2fA (%.1f°)", magnitude, angleDeg);
            } else
            {
                double vVal = (label.equals("UA") && idxUA != -1) ? glb.parseDoubleSafe(rowData[idxUA])
                        : (label.equals("UB") && idxUB != -1) ? glb.parseDoubleSafe(rowData[idxUB])
                        : (label.equals("UC") && idxUC != -1) ? glb.parseDoubleSafe(rowData[idxUC]) : 0;
                info += String.format(": %.1fV", vVal);
            }

            int offX = (int) (15 * Math.cos(rad));
            int offY = -(int) (15 * Math.sin(rad));

            // Dibujar pequeño fondo para el texto si es necesario
            int strW = g2.getFontMetrics().stringWidth(info);
            g2.setColor(new Color(33, 33, 33, 180));
            g2.fillRect(x + offX - 2, y + offY - 12, strW + 4, 15);

            g2.setColor(drawColor);
            g2.drawString(info, x + offX, y + offY);
        }

        private void drawArrowHead(Graphics2D g2, int x, int y, double angle, Color color)
        {
            int size = 12;
            double angle1 = angle + Math.toRadians(155);
            double angle2 = angle - Math.toRadians(155);
            int x1 = x + (int) (size * Math.cos(angle1));
            int y1 = y - (int) (size * Math.sin(angle1));
            int x2 = x + (int) (size * Math.cos(angle2));
            int y2 = y - (int) (size * Math.sin(angle2));
            g2.drawLine(x, y, x1, y1);
            g2.drawLine(x, y, x2, y2);
        }

        private void drawPFTable(Graphics2D g2)
        {
            int tx = 20, ty = getHeight() - 120;
            Color bg = glb.darkMode ? new Color(45, 45, 45, 220) : new Color(240, 240, 240, 220);
            Color border = glb.darkMode ? Color.GRAY : Color.LIGHT_GRAY;

            g2.setColor(bg);
            g2.fillRoundRect(tx, ty, 260, 100, 10, 10);
            g2.setColor(border);
            g2.drawRoundRect(tx, ty, 260, 100, 10, 10);

            g2.setFont(new Font("Dialog", Font.BOLD, 13));
            g2.setColor(glb.darkMode ? Color.WHITE : Color.BLACK);
            g2.drawString("Factores de Potencia (HUD):", tx + 10, ty + 22);

            g2.setFont(new Font("Dialog", Font.PLAIN, 12));
            drawPFLine(g2, tx + 15, ty + 45, "Fase A", idxFPA, idxQA, glb.colorA);
            drawPFLine(g2, tx + 15, ty + 65, "Fase B", idxFPB, idxQB, glb.colorB);
            drawPFLine(g2, tx + 15, ty + 85, "Fase C", idxFPC, idxQC, glb.colorC);
        }

        private void drawPFLine(Graphics2D g2, int x, int y, String label, int idxFP, int idxQ, Color color)
        {
            if (idxFP == -1)
            {
                return;
            }
            double fp = glb.parseDoubleSafe(rowData[idxFP]);
            double q = (idxQ != -1) ? glb.parseDoubleSafe(rowData[idxQ]) : 0;
            String type = (q >= 0) ? "IND (+Q)" : "CAP (-Q)";

            g2.setColor(chartPanel.getDisplayColor(color));
            g2.drawString(String.format("%s: %.3f (%s)", label, Math.abs(fp), type), x, y);
        }
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

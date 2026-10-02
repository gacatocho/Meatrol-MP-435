/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataHarmonic;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.gcto.dataGlobal.FastChartPanel;
import org.gcto.dataGlobal.baseTopComponent;
import org.gcto.dataGlobal.glb;
import org.gcto.dataOpciones.Opciones;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component que visualiza el análisis de armónicos.
 * Incluye vista dual opcional: Espectro de Frecuencias (arriba) y Tendencia Temporal (abajo).
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataHarmonic//armonicos//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "armonicosTopComponent",
        iconBase = "org/gcto/dataHarmonic/TH.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.gcto.dataHarmonic.armonicosTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_armonicosAction",
        preferredID = "armonicosTopComponent"
)
@Messages(
        {
            "CTL_armonicosAction=armónicos",
            "CTL_armonicosTopComponent=Análisis de Armónicos",
            "HINT_armonicosTopComponent=Visualización de THD y espectro de armónicos"
        })
public final class armonicosTopComponent extends baseTopComponent
{

    private final List<PhaseControl> phaseControls = new ArrayList<>();
    private JSpinner spnMinY;
    private JSpinner spnMaxY;
    private JComboBox<String> cmbHarmonicOrder;
    private JCheckBox chkShowSpectrum;
    
    private JPanel pnlSplitView;
    private SpectrumPanel spectrumPanel;
    
    private String[] currentCursorRow = null;
    private boolean spectrumLocked = false;

    private final Color[] extendedColors = { Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY };

    public armonicosTopComponent()
    {
        setName(Bundle.CTL_armonicosTopComponent());
        setToolTipText(Bundle.HINT_armonicosTopComponent());
        
        setupSpectrumView();
        setupHarmonicControls();
        setupYRangeControls();
        hideStatsColumns(9, 10, 11);
        
        // Sincronizar el espectro con el cursor del gráfico de tendencia
        chartPanel.setCursorDataListener((dataIdx, activeSeries) -> {
            if (spectrumLocked) return; // No actualizar si el usuario bloqueó la vista para inspeccionar barras
            
            if (dataIdx >= 0 && dataIdx < masterData.size()) {
                currentCursorRow = masterData.get(dataIdx);
                spectrumPanel.updateSpectrum(currentCursorRow);
            } else {
                currentCursorRow = null;
                spectrumPanel.updateSpectrum(null);
            }
        });

        // Lógica de bloqueo/desbloqueo por eventos de ratón
        chartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    spectrumLocked = true;
                    spectrumPanel.repaint(); // Refrescar para mostrar indicador de bloqueo
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Al volver al panel de gráficas, se desbloquea automáticamente
                spectrumLocked = false;
                spectrumPanel.repaint();
            }
        });
    }

    private void setupSpectrumView() {
        spectrumPanel = new SpectrumPanel();
        pnlSplitView = new JPanel(new GridLayout(2, 1, 0, 5));
        pnlSplitView.setBackground(Color.BLACK);
        
        chkShowSpectrum = new JCheckBox("Ver Espectro");
        chkShowSpectrum.addActionListener(e -> toggleSpectrumView());
        
        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 0);
        pnlSouth.add(chkShowSpectrum, 0);
    }

    private void toggleSpectrumView() {
        pnlChartContainer.removeAll();
        if (chkShowSpectrum.isSelected()) {
            pnlSplitView.add(spectrumPanel);
            pnlSplitView.add(chartPanel);
            pnlChartContainer.add(pnlSplitView, BorderLayout.CENTER);
        } else {
            pnlChartContainer.add(chartPanel, BorderLayout.CENTER);
        }
        pnlChartContainer.revalidate();
        pnlChartContainer.repaint();
    }

    private void setupHarmonicControls() {
        cmbHarmonicOrder = new JComboBox<>();
        cmbHarmonicOrder.addItem("THD");
        cmbHarmonicOrder.addItem("Fundamental (H01)");
        for (int i = 2; i <= 50; i++) {
            cmbHarmonicOrder.addItem("Armónico " + String.format("%02d", i));
        }
        
        cmbHarmonicOrder.addActionListener(e -> mapColumns());

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlFilter.add(new JLabel("Ver en Tendencia:"));
        pnlFilter.add(cmbHarmonicOrder);
        
        pnlSouth.add(new JSeparator(JSeparator.VERTICAL), 2);
        pnlSouth.add(pnlFilter, 2);
    }

    private void setupYRangeControls()
    {
        spnMinY = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.5));
        spnMaxY = new JSpinner(new SpinnerNumberModel(20.0, 0.0, 1000.0, 0.5));
        spnMinY.setEditor(new JSpinner.NumberEditor(spnMinY, "0.0"));
        spnMaxY.setEditor(new JSpinner.NumberEditor(spnMaxY, "0.0"));
        spnMinY.addChangeListener(e -> updateManualRange());
        spnMaxY.addChangeListener(e -> updateManualRange());

        JPanel pnlY = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlY.add(new JLabel("Escala Y (%):")); pnlY.add(spnMinY);
        pnlY.add(new JLabel("Max:")); pnlY.add(spnMaxY);

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL));
        pnlSouth.add(pnlY);
        updateManualRange();
    }

    private void updateManualRange()
    {
        double min = (Double) spnMinY.getValue();
        double max = (Double) spnMaxY.getValue();
        if (min < max) {
            chartPanel.setManualYRange(min, max);
            spectrumPanel.setYRange(min, max);
        }
    }

    @Override
    protected void mapColumns()
    {
        if (masterHeaders == null || masterHeaders.length == 0) return;
        
        phaseControls.clear();
        statsModel.setRowCount(0);

        List<Integer> chartIndices = new ArrayList<>();
        List<String> chartNames = new ArrayList<>();
        List<Color> chartColors = new ArrayList<>();

        String selection = (String) cmbHarmonicOrder.getSelectedItem();
        if (selection == null) selection = "THD";

        for (int i = 0; i < masterHeaders.length; i++)
        {
            String h = masterHeaders[i].toUpperCase();
            boolean match = false;

            if ("THD".equals(selection)) {
                if ((h.contains("UTHD") || h.contains("ITHD")) && !h.matches(".*THD\\d+.*") && 
                    !h.contains("HA") && !h.contains("HB") && !h.contains("HC") &&
                    !h.contains("XA") && !h.contains("XB") && !h.contains("XC")) {
                    match = true;
                }
            } else if (selection.contains("Fundamental")) {
                if ((h.contains("UTHD") || h.contains("ITHD")) && !h.matches(".*THD\\d+.*") &&
                    (h.contains("HA") || h.contains("HB") || h.contains("HC") ||
                     h.contains("XA") || h.contains("XB") || h.contains("XC"))) {
                    match = true;
                }
            } else {
                String nStr = selection.replaceAll("[^0-9]", "");
                String target = "THD" + Integer.parseInt(nStr);
                if (h.contains(target) && h.contains("(%)")) {
                    match = true;
                }
            }

            if (match)
            {
                boolean isVoltage = h.contains("UTHD") || h.contains("UTH");
                Color c = extendedColors[i % extendedColors.length];
                
                if (h.contains("A") || h.contains("L1")) {
                    c = isVoltage ? Color.GREEN : glb.colorA;
                } else if (h.contains("B") || h.contains("L2")) {
                    c = isVoltage ? Color.BLACK : glb.colorB;
                } else if (h.contains("C") || h.contains("L3")) {
                    c = isVoltage ? Color.ORANGE : glb.colorC;
                } else if (h.contains("N")) {
                    c = isVoltage ? glb.colorN.darker() : glb.colorN;
                }

                phaseControls.add(new PhaseControl(i, masterHeaders[i], c));
                chartIndices.add(i);
                chartNames.add(masterHeaders[i]);
                chartColors.add(c);
            }
        }

        chartPanel.setSeries(chartIndices, chartNames, chartColors);
        updateStatsTableRows();
        
        // Actualizar el mapeo del espectro
        spectrumPanel.setupMapping(masterHeaders);
        
        if (!masterData.isEmpty() && !chartIndices.isEmpty()) {
            double maxVal = 0;
            int step = Math.max(1, masterData.size() / 500);
            for (int i = 0; i < masterData.size(); i += step) {
                String[] row = masterData.get(i);
                for (int colIdx : chartIndices) {
                    maxVal = Math.max(maxVal, glb.parseDoubleSafe(row[colIdx]));
                }
            }
            if (maxVal > 0) {
                double newMax = Math.ceil(maxVal * 1.1);
                spnMaxY.setValue(newMax);
                updateManualRange();
            }
        }
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

    @Override protected void onDataLoaded() { updateChartData(); updateStatistics(); }
    @Override protected void onTimeRangeUpdated() { updateChartData(); updateStatistics(); }

    private void updateChartData()
    {
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
            if (!isVisible) { rowInModel++; continue; }

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

            statsModel.setValueAt(String.format("%.2f", min), rowInModel, 3);
            statsModel.setValueAt(dateMin, rowInModel, 4);
            statsModel.setValueAt(String.format("%.2f", avgArit), rowInModel, 5);
            statsModel.setValueAt(String.format("%.2f", max), rowInModel, 7);
            statsModel.setValueAt(dateMax, rowInModel, 8);

            boolean showP = (boolean) statsModel.getValueAt(rowInModel, 6);
            if (showP) trendLines.add(new FastChartPanel.TrendLine(avgArit, pc.color, new BasicStroke(glb.grosLinProm, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10, 5}, 0)));

            rowInModel++;
        }
        chartPanel.setTrendLines(trendLines);
    }

    @Override
    public void updateTheme() {
        super.updateTheme();
        if (spectrumPanel != null) {
            spectrumPanel.setBackground(glb.darkMode ? Color.BLACK : Color.WHITE);
            spectrumPanel.repaint();
        }
    }

    @Override
    public void onExport() {
        if (!chkShowSpectrum.isSelected()) {
            super.onExport(); // Exportación normal de tendencia
            return;
        }
        
        // Exportación combinada (Espectro + Tendencia)
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Exportar Análisis de Armónicos (Fondo Blanco)");
        fc.setFileFilter(new FileNameExtensionFilter("Imagen PNG (*.png)", "png"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) file = new File(file.getAbsolutePath() + ".png");
            
            try {
                // 1. Guardar estado actual
                boolean wasDarkMode = glb.darkMode;
                Color oldColorN = glb.colorN;
                
                // 2. Forzar modo claro para exportación
                glb.darkMode = false;
                glb.colorN = Color.BLACK;
                updateTheme();

                BufferedImage img = new BufferedImage(pnlChartContainer.getWidth(), pnlChartContainer.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = img.createGraphics();
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, img.getWidth(), img.getHeight());
                
                pnlChartContainer.paint(g2);
                g2.dispose();
                
                ImageIO.write(img, "png", file);
                
                // 3. Restaurar estado
                glb.darkMode = wasDarkMode;
                glb.colorN = oldColorN;
                updateTheme();
                
                JOptionPane.showMessageDialog(this, "Análisis exportado correctamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Panel especializado para dibujar el espectro de armónicos (Barras H01-H50).
     */
    private class SpectrumPanel extends JPanel {
        private String[] rowData = null;
        private final List<HarmonicMapping> mappings = new ArrayList<>();
        private double minY = 0, maxY = 20;
        private Point mousePos = null;

        public SpectrumPanel() {
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(100, 200));
            
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    mousePos = e.getPoint();
                    repaint();
                }
            });
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    mousePos = null;
                    repaint();
                }
            });
        }

        public void setYRange(double min, double max) {
            this.minY = min; this.maxY = max;
            repaint();
        }

        public void setupMapping(String[] headers) {
            mappings.clear();
            for (int h = 1; h <= 50; h++) {
                String target = "THD" + (h == 1 ? "" : h);
                for (int i = 0; i < headers.length; i++) {
                    String head = headers[i].toUpperCase();
                    if (head.contains(target) && head.contains("(%)")) {
                        // Usar el color del vector glb.opc.colorArm para el armónico correspondiente
                        Color c = glb.colorDe_RGB_String(Opciones.colorArm[h]);
                        mappings.add(new HarmonicMapping(i, h, c, head));
                    }
                }
            }
            repaint();
        }

        public void updateSpectrum(String[] row) {
            this.rowData = row;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color textColor = glb.darkMode ? Color.WHITE : Color.BLACK;
            Color gridColor = glb.darkMode ? new Color(50, 50, 50) : new Color(220, 220, 220);

            // Indicador visual de bloqueo
            if (spectrumLocked) {
                setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
            } else {
                setBorder(null);
            }

            if (rowData == null || mappings.isEmpty()) {
                g2.setColor(textColor);
                g2.setFont(new Font("Dialog", Font.BOLD, 14));
                g2.drawString("Mueva el cursor sobre la tendencia para ver el espectro", 50, getHeight() / 2);
                return;
            }

            int w = getWidth(), h = getHeight();
            int marginL = 60, marginR = 30, marginT = 40, marginB = 40;
            int chartW = w - marginL - marginR, chartH = h - marginT - marginB;

            // Dibujar Ejes y Grilla
            g2.setColor(gridColor);
            g2.drawRect(marginL, marginT, chartW, chartH);
            
            g2.setColor(textColor);
            g2.setFont(new Font("Dialog", Font.BOLD, 12));
            String title = "Espectro en: " + rowData[0] + " " + rowData[1];
            if (spectrumLocked) title += " [BLOQUEADO - Click en tendencia para liberar]";
            g2.drawString(title, marginL, marginT - 15);

            // Escala Y
            for (int i = 0; i <= 5; i++) {
                double val = minY + (maxY - minY) * i / 5.0;
                int y = marginT + chartH - (int)((val - minY) / (maxY - minY) * chartH);
                g2.setColor(gridColor);
                g2.drawLine(marginL, y, marginL + chartW, y);
                g2.setColor(textColor);
                g2.drawString(String.format("%.1f", val), marginL - 45, y + 5);
            }

            // Dibujar Barras
            int barGroupW = Math.max(1, chartW / 50);
            HarmonicMapping hoveredMapping = null;
            double hoveredValue = 0;
            Point hoveredPoint = null;

            for (HarmonicMapping m : mappings) {
                double val = glb.parseDoubleSafe(rowData[m.colIdx]);
                if (val < minY) continue;
                
                int barW = Math.max(2, barGroupW / 4);
                int x = marginL + (m.order - 1) * barGroupW;
                
                // Desplazamiento por fase
                if (m.name.contains("B") || m.name.contains("L2")) x += barW;
                else if (m.name.contains("C") || m.name.contains("L3")) x += barW * 2;
                
                int barH = (int)((Math.min(val, maxY) - minY) / (maxY - minY) * chartH);
                int y = marginT + chartH - barH;

                // Detección de Hover
                boolean isHovered = mousePos != null && mousePos.x >= x && mousePos.x <= x + barW && mousePos.y >= y && mousePos.y <= marginT + chartH;
                
                Color baseColor = chartPanel.getDisplayColor(m.color);
                g2.setColor(isHovered ? baseColor.brighter() : baseColor);
                g2.fillRect(x, y, barW, barH);
                g2.setColor(gridColor);
                g2.drawRect(x, y, barW, barH);
                
                if (isHovered) {
                    hoveredMapping = m;
                    hoveredValue = val;
                    hoveredPoint = new Point(x, y);
                }
            }

            // Dibujar Tooltip del Espectro (Carbon HUD)
            if (hoveredMapping != null && hoveredPoint != null) {
                String tip = String.format("H%02d (%s): %.2f %%", hoveredMapping.order, 
                        hoveredMapping.name.contains("A") ? "Fase A" : (hoveredMapping.name.contains("B") ? "Fase B" : "Fase C"), 
                        hoveredValue);
                
                g2.setFont(new Font("Dialog", Font.BOLD, 12));
                int tipW = g2.getFontMetrics().stringWidth(tip) + 10;
                int tipX = Math.min(hoveredPoint.x + 5, w - tipW - 5);
                int tipY = hoveredPoint.y - 25;
                
                // Fondo Carbon HUD para el espectro también
                g2.setColor(new Color(33, 33, 33, 230));
                g2.fillRoundRect(tipX, tipY, tipW, 20, 5, 5);
                g2.setColor(new Color(80, 80, 80));
                g2.drawRoundRect(tipX, tipY, tipW, 20, 5, 5);
                
                // Usar el color original de la fase para el texto
                Color textColorTip = hoveredMapping.color;
                if (textColorTip.equals(Color.BLACK)) textColorTip = Color.WHITE;
                g2.setColor(textColorTip);
                g2.drawString(tip, tipX + 5, tipY + 15);
            }

            // Etiquetas X (Órdenes) - Se muestran todos los armónicos rotados para evitar solapamiento
            g2.setColor(textColor);
            g2.setFont(new Font("Dialog", Font.PLAIN, 9));
            for (int i = 1; i <= 50; i++) {
                int x = marginL + (i - 1) * barGroupW + (barGroupW / 2);
                Graphics2D gRot = (Graphics2D) g2.create();
                gRot.translate(x + 3, marginT + chartH + 5);
                gRot.rotate(Math.toRadians(90));
                gRot.drawString(String.format("%02d", i), 0, 0);
                gRot.dispose();
            }
        }
    }

    private static class HarmonicMapping {
        int colIdx, order;
        Color color;
        String name;
        HarmonicMapping(int c, int o, Color cl, String n) { colIdx=c; order=o; color=cl; name=n; }
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

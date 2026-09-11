package org.gcto.dataGlobal;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Motor de dibujo de alto rendimiento utilizando Java2D.
 * Optimizado para visualizar hasta 1M de puntos mediante Min-Max Downsampling.
 * Soporta valores negativos, escalado automático bidireccional y modo simétrico.
 * 
 * @author camilo
 */
public class FastChartPanel extends JPanel {
    
    private List<String[]> data = new ArrayList<>();
    private int startIndex = 0;
    private int endIndex = 0;
    private List<ChartSeries> series = new ArrayList<>();
    private List<TrendLine> trendLines = new ArrayList<>();
    
    private double currentMaxVal = 1.0;
    private double currentMinVal = 0.0;
    private Point mousePos = null;
    private Integer selectionStartIndex = null;
    
    // Propiedades de visualización
    private boolean symmetricY = false;
    
    private final int MARGIN_LEFT = 90; 
    private final int MARGIN_RIGHT = 30;
    private final int MARGIN_TOP = 30;
    private final int MARGIN_BOTTOM = 70; 

    public interface RangeSelectionListener {
        void onRangeSelected(int startIdx, int endIdx);
    }
    
    private RangeSelectionListener selectionListener;

    public FastChartPanel() {
        setBackground(Color.BLACK);
        setOpaque(true);
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (data == null || data.isEmpty()) return;
                
                if (e.getClickCount() == 2) {
                    if (selectionListener != null) {
                        selectionListener.onRangeSelected(0, data.size() - 1);
                    }
                    selectionStartIndex = null;
                    repaint();
                    return;
                }

                int chartW = getWidth() - MARGIN_LEFT - MARGIN_RIGHT;
                if (e.getX() < MARGIN_LEFT || e.getX() > MARGIN_LEFT + chartW) return;

                int numPoints = endIndex - startIndex + 1;
                double step = (double) numPoints / chartW;
                int dataIdx = startIndex + (int) ((e.getX() - MARGIN_LEFT) * step);
                if (dataIdx >= data.size()) dataIdx = data.size() - 1;

                if (SwingUtilities.isRightMouseButton(e)) {
                    selectionStartIndex = dataIdx;
                    repaint();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    if (selectionStartIndex != null) {
                        int start = Math.min(selectionStartIndex, dataIdx);
                        int end = Math.max(selectionStartIndex, dataIdx);
                        if (start != end && selectionListener != null) {
                            selectionListener.onRangeSelected(start, end);
                        }
                        selectionStartIndex = null;
                        repaint();
                    }
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                mousePos = null;
                repaint();
            }
        });
    }

    public void setSymmetricY(boolean symmetric) {
        this.symmetricY = symmetric;
        calculateRange();
        repaint();
    }

    public void setRangeSelectionListener(RangeSelectionListener listener) {
        this.selectionListener = listener;
    }

    public void setData(List<String[]> data, int start, int end) {
        this.data = data;
        this.startIndex = start;
        this.endIndex = end;
        calculateRange();
        repaint();
    }

    public void setSeries(List<Integer> columnIndices, List<String> names, List<Color> colors) {
        series.clear();
        for (int i = 0; i < columnIndices.size(); i++) {
            series.add(new ChartSeries(names.get(i), columnIndices.get(i), colors.get(i)));
        }
        calculateRange();
        repaint();
    }

    public void setSeriesVisible(int colIdx, boolean visible) {
        for (ChartSeries s : series) {
            if (s.colIdx == colIdx) {
                s.visible = visible;
                break;
            }
        }
        calculateRange();
        repaint();
    }
    
    public void setTrendLines(List<TrendLine> lines) {
        this.trendLines = lines != null ? lines : new ArrayList<>();
        repaint();
    }
    
    private void calculateRange() {
        if (data == null || data.isEmpty() || series.isEmpty()) {
            currentMaxVal = 1.0;
            currentMinVal = 0.0;
            return;
        }
        
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        boolean anyVisible = false;
        
        for (ChartSeries s : series) {
            if (!s.visible) continue;
            anyVisible = true;
            for (int i = startIndex; i <= endIndex; i++) {
                if (i >= data.size()) break;
                double val = glb.parseDoubleSafe(data.get(i)[s.colIdx]);
                if (val > max) max = val;
                if (val < min) min = val;
            }
        }
        
        if (!anyVisible) {
            max = 1.0;
            min = 0.0;
        } else {
            // Forzar inclusión del Cero
            if (max < 0) max = 0;
            if (min > 0) min = 0;
            
            if (symmetricY) {
                double absMax = Math.max(Math.abs(max), Math.abs(min));
                max = absMax;
                min = -absMax;
            }
            
            double range = max - min;
            if (range == 0) range = Math.abs(max) * 0.2;
            if (range == 0) range = 1.0;
            
            // Añadir un 10% de margen dinámico
            this.currentMaxVal = max + (range * 0.10);
            this.currentMinVal = min - (range * 0.10);
        }
    }

    private int valToY(double val, int chartH) {
        double range = currentMaxVal - currentMinVal;
        if (range == 0) return MARGIN_TOP + chartH / 2;
        return MARGIN_TOP + (int) ((currentMaxVal - val) / range * chartH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty() || series.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();
        int chartW = w - MARGIN_LEFT - MARGIN_RIGHT;
        int chartH = h - MARGIN_TOP - MARGIN_BOTTOM;

        if (chartW <= 0 || chartH <= 0) return;

        drawAxes(g2, chartW, chartH);

        // 1. Dibujar Líneas de Tendencia
        drawTrendLines(g2, chartW, chartH);

        // 2. Dibujar Series con Min-Max Downsampling
        int numPoints = endIndex - startIndex + 1;
        double pointsPerPixel = (double) numPoints / chartW;

        for (ChartSeries s : series) {
            if (!s.visible) continue;
            
            g2.setColor(s.color);
            g2.setStroke(new BasicStroke(1.0f));
            
            int prevX = -1;
            int prevYMax = -1;
            int prevYMin = -1;

            for (int x = 0; x <= chartW; x++) {
                int iStart = startIndex + (int) (x * pointsPerPixel);
                int iEnd = startIndex + (int) ((x + 1) * pointsPerPixel);
                if (iEnd > endIndex) iEnd = endIndex;
                if (iStart > endIndex) break;

                double minBucket = Double.MAX_VALUE;
                double maxBucket = -Double.MAX_VALUE;

                for (int i = iStart; i <= iEnd; i++) {
                    if (i >= data.size()) break;
                    double val = glb.parseDoubleSafe(data.get(i)[s.colIdx]);
                    if (val < minBucket) minBucket = val;
                    if (val > maxBucket) maxBucket = val;
                }

                int curX = MARGIN_LEFT + x;
                int curYMin = valToY(minBucket, chartH);
                int curYMax = valToY(maxBucket, chartH);

                if (curYMin == curYMax) {
                    if (prevX != -1) g2.drawLine(prevX, prevYMax, curX, curYMax);
                    else g2.drawLine(curX, curYMax, curX, curYMax);
                } else {
                    g2.drawLine(curX, curYMin, curX, curYMax);
                    if (prevX != -1) {
                        g2.drawLine(prevX, prevYMax, curX, curYMax);
                        g2.drawLine(prevX, prevYMin, curX, curYMin);
                    }
                }
                
                prevX = curX;
                prevYMax = curYMax;
                prevYMin = curYMin;
            }
        }
        
        drawSelection(g2, chartW, chartH);

        if (mousePos != null && mousePos.x >= MARGIN_LEFT && mousePos.x <= MARGIN_LEFT + chartW 
                && mousePos.y >= MARGIN_TOP && mousePos.y <= MARGIN_TOP + chartH) {
            
            drawCrosshair(g2, mousePos, chartW, chartH);
            drawTooltip(g2, mousePos, chartW, chartH);
        }

        drawLegend(g2);
    }

    private void drawTrendLines(Graphics2D g2, int chartW, int chartH) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        for (TrendLine line : trendLines) {
            int y = valToY(line.value, chartH);
            if (y < MARGIN_TOP || y > MARGIN_TOP + chartH) continue;
            
            g2.setColor(line.color);
            g2.setStroke(line.stroke);
            g2.drawLine(MARGIN_LEFT, y, MARGIN_LEFT + chartW, y);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawSelection(Graphics2D g2, int chartW, int chartH) {
        if (selectionStartIndex == null) return;
        int numPoints = endIndex - startIndex + 1;
        double pointsPerPixel = (double) numPoints / chartW;
        int xStart = MARGIN_LEFT + (int) ((selectionStartIndex - startIndex) / pointsPerPixel);
        
        if (xStart >= MARGIN_LEFT && xStart <= MARGIN_LEFT + chartW) {
            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g2.drawLine(xStart, MARGIN_TOP, xStart, MARGIN_TOP + chartH);
        }
        
        if (mousePos != null && mousePos.x >= MARGIN_LEFT && mousePos.x <= MARGIN_LEFT + chartW) {
            int x1 = Math.min(xStart, mousePos.x);
            int x2 = Math.max(xStart, mousePos.x);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2.setColor(Color.ORANGE);
            g2.fillRect(x1, MARGIN_TOP, x2 - x1, chartH);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    private void drawAxes(Graphics2D g2, int chartW, int chartH) {
        g2.setColor(new Color(80, 80, 80));
        g2.drawRect(MARGIN_LEFT, MARGIN_TOP, chartW, chartH);
        
        // Línea de Cero (Referencia Crítica para Reactiva)
        if (currentMinVal <= 0 && currentMaxVal >= 0) {
            int yZero = valToY(0, chartH);
            g2.setColor(new Color(180, 180, 180));
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawLine(MARGIN_LEFT, yZero, MARGIN_LEFT + chartW, yZero);
        }

        g2.setFont(new Font("Dialog", Font.BOLD, 12));
        double range = currentMaxVal - currentMinVal;
        for (int i = 0; i <= 10; i++) {
            double val = currentMinVal + (range * i / 10.0);
            int y = valToY(val, chartH);
            
            g2.setColor(new Color(50, 50, 50)); 
            g2.drawLine(MARGIN_LEFT, y, MARGIN_LEFT + chartW, y);
            
            g2.setColor(Color.WHITE);
            g2.drawLine(MARGIN_LEFT - 5, y, MARGIN_LEFT, y);
            String labelStr = String.format("%.1f", val);
            int strW = g2.getFontMetrics().stringWidth(labelStr);
            g2.drawString(labelStr, MARGIN_LEFT - 10 - strW, y + 5);
        }

        int numTimeLabels = 10; 
        int numPoints = endIndex - startIndex + 1;
        for (int i = 0; i < numTimeLabels; i++) {
            int xOffset = i * chartW / (numTimeLabels - 1);
            int x = MARGIN_LEFT + xOffset;
            g2.setColor(new Color(50, 50, 50));
            g2.drawLine(x, MARGIN_TOP, x, MARGIN_TOP + chartH);
            
            int dataIdx = startIndex + (int) ((double) i / (numTimeLabels - 1) * (numPoints - 1));
            if (dataIdx > endIndex) dataIdx = endIndex;
            if (dataIdx >= data.size()) dataIdx = data.size() - 1;
            
            String[] row = data.get(dataIdx);
            g2.setColor(Color.WHITE);
            g2.drawLine(x, MARGIN_TOP + chartH, x, MARGIN_TOP + chartH + 5);
            
            g2.setFont(new Font("Dialog", Font.BOLD, 13));
            int timeW = g2.getFontMetrics().stringWidth(row[1]);
            g2.drawString(row[1], x - timeW / 2, MARGIN_TOP + chartH + 20);
            
            g2.setFont(new Font("Dialog", Font.PLAIN, 11));
            int dateW = g2.getFontMetrics().stringWidth(row[0]);
            g2.drawString(row[0], x - dateW / 2, MARGIN_TOP + chartH + 35);
        }
    }
    
    private void drawLegend(Graphics2D g2) {
        int legendX = MARGIN_LEFT;
        g2.setFont(new Font("Dialog", Font.BOLD, 13));
        for (ChartSeries s : series) {
            if (!s.visible) continue;
            g2.setColor(s.color);
            g2.fillRect(legendX, 8, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(s.name, legendX + 15, 18);
            legendX += g2.getFontMetrics().stringWidth(s.name) + 35;
        }
    }
    
    private void drawCrosshair(Graphics2D g2, Point p, int chartW, int chartH) {
        g2.setColor(new Color(200, 200, 200, 150));
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawLine(p.x, MARGIN_TOP, p.x, MARGIN_TOP + chartH);
        g2.drawLine(MARGIN_LEFT, p.y, MARGIN_LEFT + chartW, p.y);
    }
    
    private void drawTooltip(Graphics2D g2, Point p, int chartW, int chartH) {
        int numPoints = endIndex - startIndex + 1;
        double step = (double) numPoints / chartW;
        int dataIdx = startIndex + (int) ((p.x - MARGIN_LEFT) * step);
        if (dataIdx > endIndex) dataIdx = endIndex;
        if (dataIdx >= data.size()) dataIdx = data.size() - 1;
        
        String[] row = data.get(dataIdx);
        List<String> lines = new ArrayList<>();
        lines.add(row[0] + " " + row[1]);
        for (ChartSeries s : series) {
            if (s.visible) {
                double val = glb.parseDoubleSafe(row[s.colIdx]);
                lines.add(s.name + ": " + String.format("%.2f", val));
            }
        }
        
        g2.setFont(new Font("Dialog", Font.PLAIN, 12));
        int tipW = 0;
        for (String line : lines) tipW = Math.max(tipW, g2.getFontMetrics().stringWidth(line));
        tipW += 20;
        int tipH = lines.size() * 18 + 10;
        
        int tipX = p.x + 15;
        int tipY = p.y + 15;
        if (tipX + tipW > getWidth()) tipX = p.x - tipW - 15;
        if (tipY + tipH > getHeight() - 20) tipY = p.y - tipH - 15;
        
        g2.setColor(new Color(20, 20, 20, 230));
        g2.fillRoundRect(tipX, tipY, tipW, tipH, 8, 8);
        g2.setColor(new Color(100, 100, 100));
        g2.drawRoundRect(tipX, tipY, tipW, tipH, 8, 8);
        
        int textY = tipY + 20;
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                g2.setFont(new Font("Dialog", Font.BOLD, 12));
                g2.setColor(Color.WHITE);
            } else {
                g2.setFont(new Font("Dialog", Font.PLAIN, 12));
                String line = lines.get(i);
                g2.setColor(Color.LIGHT_GRAY);
                for (ChartSeries s : series) {
                    if (line.startsWith(s.name)) { g2.setColor(s.color); break; }
                }
            }
            g2.drawString(lines.get(i), tipX + 10, textY);
            textY += 18;
        }
    }
    
    public void saveAsImage(File file) throws Exception {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        this.paint(g2);
        g2.dispose();
        String name = file.getName().toLowerCase();
        String format = name.endsWith(".png") ? "png" : "jpg";
        ImageIO.write(img, format, file);
    }

    private static class ChartSeries {
        String name;
        int colIdx;
        Color color;
        boolean visible = true;

        ChartSeries(String name, int colIdx, Color color) {
            this.name = name;
            this.colIdx = colIdx;
            this.color = color;
        }
    }
    
    public static class TrendLine {
        double value;
        Color color;
        Stroke stroke;

        public TrendLine(double value, Color color, Stroke stroke) {
            this.value = value;
            this.color = color;
            this.stroke = stroke;
        }
    }
}

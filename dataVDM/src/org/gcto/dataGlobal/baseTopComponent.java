/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;
import org.gcto.dataVDM.dataTopComponent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Clase base para todos los formularios de análisis. Estructura optimizada:
 * - NORTH: Tabla de Estadísticas (Altura fija para 4 filas)
 * - CENTER: Contenedor del Gráfico (Java2D) con Tooltip dinámico
 * - SOUTH: Rango Temporal y Controles de Escala Y
 *
 * @author camilo
 */
public abstract class baseTopComponent extends TopComponent
{

    protected JPanel pnlChartContainer;
    protected FastChartPanel chartPanel;
    protected JTable tblStats;
    protected DefaultTableModel statsModel;
    protected JScrollPane scrollStats;

    // Controles de Rango Temporal
    protected JPanel pnlSouth;
    protected JSlider sliderStart;
    protected JSlider sliderEnd;
    protected JLabel lblTimeStart;
    protected JLabel lblTimeEnd;
    protected JCheckBox chkSyncAll;

    // Controles de Escala Y
    protected JSpinner spnMinY;
    protected JSpinner spnMaxY;

    // Referencias a los datos maestros
    protected String[] masterHeaders = new String[0];
    protected List<String[]> masterData = new ArrayList<>();

    public baseTopComponent()
    {
        initComponentsBase();
    }

    private void initComponentsBase()
    {
        setLayout(new BorderLayout());

        // --- PANEL NORTE: Tabla de Estadísticas ---
        String[] statsHeaders = { "Color", "Nombre", "Ver", "Minimo", "Fecha Min", "Promedio Arit.", "Ver Promedio", "Maximo", "Fecha Max", "Promedio sobre %", "% de Nivel", "Ver promedio sobre %" };
        statsModel = new DefaultTableModel(statsHeaders, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column) { return isColumnEditable(column); }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Color.class;
                if (columnIndex == 2 || columnIndex == 6 || columnIndex == 11) return Boolean.class;
                if (columnIndex == 10) return Integer.class;
                return super.getColumnClass(columnIndex);
            }
        };

        statsModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int col = e.getColumn();
                if (col == 2) {
                    int row = e.getFirstRow();
                    String name = (String) statsModel.getValueAt(row, 1);
                    boolean visible = (boolean) statsModel.getValueAt(row, 2);
                    chartPanel.setSeriesVisibleByName(name, visible);
                }
                if (col == 2 || col == 6 || col == 10 || col == 11) onTimeRangeUpdated();
            }
        });

        tblStats = new JTable(statsModel);
        tblStats.setRowHeight(22);
        setupTableEditors();

        scrollStats = new JScrollPane(tblStats);
        add(scrollStats, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Gráfico ---
        pnlChartContainer = new JPanel(new BorderLayout());
        pnlChartContainer.setBackground(Color.BLACK);

        chartPanel = new FastChartPanel();
        chartPanel.setRangeSelectionListener((startIdx, endIdx) -> {
            if (masterData.isEmpty()) return;
            int sVal = (int) ((startIdx / (double) (masterData.size() - 1)) * 1000);
            int eVal = (int) ((endIdx / (double) (masterData.size() - 1)) * 1000);
            sliderStart.setValue(sVal);
            sliderEnd.setValue(eVal);
        });
        
        pnlChartContainer.add(chartPanel, BorderLayout.CENTER);
        add(pnlChartContainer, BorderLayout.CENTER);

        // --- PANEL SUR: Rango Temporal ---
        pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        lblTimeStart = new JLabel("Inicio: --/--/-- --:--");
        sliderStart = new JSlider(0, 1000, 0);
        sliderStart.setPreferredSize(new Dimension(250, 25));
        sliderEnd = new JSlider(0, 1000, 1000);
        sliderEnd.setPreferredSize(new Dimension(250, 25));
        lblTimeEnd = new JLabel("Fin: --/--/-- --:--");
        chkSyncAll = new JCheckBox("Aplicar a todos");
        chkSyncAll.setSelected(glb.syncTimeRange);
        chkSyncAll.addActionListener(e -> glb.syncTimeRange = chkSyncAll.isSelected());

        pnlSouth.add(lblTimeStart);
        pnlSouth.add(sliderStart);
        pnlSouth.add(sliderEnd);
        pnlSouth.add(lblTimeEnd);
        pnlSouth.add(chkSyncAll);
        add(pnlSouth, BorderLayout.SOUTH);

        setupTimeLabelInteractivity(lblTimeStart);
        setupTimeLabelInteractivity(lblTimeEnd);

        sliderStart.addChangeListener(e -> handleTimeRangeChange());
        sliderEnd.addChangeListener(e -> handleTimeRangeChange());
    }

    /**
     * Configura los controles de rango manual para el eje Y.
     * @param unit Unidad de medida (V, A, Hz, etc.)
     */
    protected void setupYRangeControls(String unit)
    {
        // Permitir valores negativos para potencias reactivas y otros casos
        spnMinY = new JSpinner(new SpinnerNumberModel(0.0, -1000000.0, 1000000.0, 1.0));
        spnMaxY = new JSpinner(new SpinnerNumberModel(100.0, -1000000.0, 1000000.0, 1.0));

        spnMinY.setEditor(new JSpinner.NumberEditor(spnMinY, "0.0"));
        spnMaxY.setEditor(new JSpinner.NumberEditor(spnMaxY, "0.0"));

        spnMinY.addChangeListener(e -> updateManualRange());
        spnMaxY.addChangeListener(e -> updateManualRange());

        JPanel pnlY = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlY.add(new JLabel("Min Y (" + unit + "):"));
        pnlY.add(spnMinY);
        pnlY.add(new JLabel("Max Y (" + unit + "):"));
        pnlY.add(spnMaxY);

        pnlSouth.add(new JSeparator(JSeparator.VERTICAL));
        pnlSouth.add(pnlY);

        pnlSouth.revalidate();
        pnlSouth.repaint();

        updateManualRange();
    }

    /**
     * Aplica los valores de los spinners al gráfico.
     */
    protected void updateManualRange()
    {
        if (spnMinY == null || spnMaxY == null) return;
        double min = (Double) spnMinY.getValue();
        double max = (Double) spnMaxY.getValue();
        if (min < max)
        {
            chartPanel.setManualYRange(min, max);
        }
    }

    /**
     * Ajusta automáticamente el spinner de Max Y basado en el valor máximo detectado.
     * Mantiene el mínimo en 0.
     * @param maxVal Valor máximo en la selección actual.
     */
    protected void autoAdjustYRange(double maxVal)
    {
        if (spnMaxY == null || maxVal <= 0) return;
        double newMaxY = maxVal * 1.1;
        // Solo actualizamos si hay un cambio significativo (1%) para evitar parpadeos
        if (Math.abs((Double) spnMaxY.getValue() - newMaxY) > (newMaxY * 0.01))
        {
            spnMaxY.setValue(newMaxY);
        }
    }

    /**
     * Ajusta automáticamente los spinners de Min Y y Max Y basado en el rango detectado.
     * Útil para valores que pueden ser negativos (ej: Potencia Reactiva).
     * @param minVal Valor mínimo en la selección actual.
     * @param maxVal Valor máximo en la selección actual.
     */
    protected void autoAdjustYRange(double minVal, double maxVal)
    {
        if (spnMinY == null || spnMaxY == null) return;
        if (minVal == Double.MAX_VALUE || maxVal == -Double.MAX_VALUE) return;

        double range = maxVal - minVal;
        if (range <= 0) range = Math.abs(maxVal) * 0.2;
        if (range <= 0) range = 1.0;
        
        double newMinY = minVal - (range * 0.1);
        double newMaxY = maxVal + (range * 0.1);
        
        // Solo actualizamos si hay un cambio significativo (1%) para evitar parpadeos
        if (Math.abs((Double) spnMinY.getValue() - newMinY) > (range * 0.01))
        {
            spnMinY.setValue(newMinY);
        }
        
        if (Math.abs((Double) spnMaxY.getValue() - newMaxY) > (range * 0.01))
        {
            spnMaxY.setValue(newMaxY);
        }
    }

    private void setupTimeLabelInteractivity(JLabel label)
    {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        RoundedBalloonStyle tipStyle = new RoundedBalloonStyle(5, 5, new Color(173, 216, 230), Color.BLUE);
        JLabel lblContent = new JLabel("click para ajuste preciso de tiempos");
        lblContent.setForeground(Color.BLACK);
        final BalloonTip tip = new BalloonTip(label, lblContent, tipStyle, false);
        tip.setVisible(false);

        label.addMouseListener(new MouseAdapter()
        {
            @Override public void mousePressed(MouseEvent e) { showTimeAdjustmentDialog(); }
            @Override public void mouseEntered(MouseEvent e) { tip.setVisible(true); }
            @Override public void mouseExited(MouseEvent e) { tip.setVisible(false); }
        });
    }

    private void showTimeAdjustmentDialog()
    {
        if (masterData.isEmpty()) return;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date minProjectDate, maxProjectDate;
        try {
            minProjectDate = sdf.parse(masterData.get(0)[0] + " " + masterData.get(0)[1]);
            maxProjectDate = sdf.parse(masterData.get(masterData.size() - 1)[0] + " " + masterData.get(masterData.size() - 1)[1]);
        } catch (ParseException ex) { return; }

        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));

        Date dateStart, dateEnd;
        try {
            dateStart = sdf.parse(masterData.get(sIdx)[0] + " " + masterData.get(sIdx)[1]);
            dateEnd = sdf.parse(masterData.get(eIdx)[0] + " " + masterData.get(eIdx)[1]);
        } catch (ParseException ex) {
            dateStart = minProjectDate; dateEnd = maxProjectDate;
        }

        final JSpinner spnStart = new JSpinner(new SpinnerDateModel(dateStart, null, null, java.util.Calendar.MINUTE));
        final JSpinner spnEnd = new JSpinner(new SpinnerDateModel(dateEnd, null, null, java.util.Calendar.MINUTE));
        spnStart.setEditor(new JSpinner.DateEditor(spnStart, "yyyy-MM-dd HH:mm"));
        spnEnd.setEditor(new JSpinner.DateEditor(spnEnd, "yyyy-MM-dd HH:mm"));

        spnStart.addChangeListener(ev -> {
            Date s = (Date) spnStart.getValue();
            if (s.before(minProjectDate)) { spnStart.setValue(minProjectDate); return; }
            if (s.after(maxProjectDate)) { spnStart.setValue(maxProjectDate); return; }
            Date f = (Date) spnEnd.getValue();
            if (f.getTime() - s.getTime() < 60000) {
                long next = s.getTime() + 60000;
                if (next <= maxProjectDate.getTime()) spnEnd.setValue(new Date(next));
                else spnStart.setValue(new Date(maxProjectDate.getTime() - 60000));
            }
        });

        spnEnd.addChangeListener(ev -> {
            Date f = (Date) spnEnd.getValue();
            if (f.before(minProjectDate)) { spnEnd.setValue(minProjectDate); return; }
            if (f.after(maxProjectDate)) { spnEnd.setValue(maxProjectDate); return; }
            Date s = (Date) spnStart.getValue();
            if (f.getTime() - s.getTime() < 60000) {
                long prev = f.getTime() - 60000;
                if (prev >= minProjectDate.getTime()) spnStart.setValue(new Date(prev));
                else spnEnd.setValue(new Date(minProjectDate.getTime() + 60000));
            }
        });

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.add(new JLabel("Inicio:")); pnl.add(spnStart);
        pnl.add(new JLabel("Fin:")); pnl.add(spnEnd);

        int result = JOptionPane.showConfirmDialog(this, pnl, "Ajuste Preciso de Tiempos", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Date newStart = (Date) spnStart.getValue();
            Date newEnd = (Date) spnEnd.getValue();
            int newSIdx = findClosestIndex(newStart, sdf);
            int newEIdx = findClosestIndex(newEnd, sdf);
            sliderStart.setValue((int) ((newSIdx / (double) (masterData.size() - 1)) * 1000));
            sliderEnd.setValue((int) ((newEIdx / (double) (masterData.size() - 1)) * 1000));
        }
    }

    private int findClosestIndex(Date target, SimpleDateFormat sdf)
    {
        if (masterData.isEmpty()) return 0;
        int low = 0, high = masterData.size() - 1, closestIdx = 0;
        long targetTime = target.getTime(), minDiff = Long.MAX_VALUE;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            try {
                Date midDate = sdf.parse(masterData.get(mid)[0] + " " + masterData.get(mid)[1]);
                long midTime = midDate.getTime();
                long diff = Math.abs(midTime - targetTime);
                if (diff < minDiff) { minDiff = diff; closestIdx = mid; }
                if (midTime < targetTime) low = mid + 1;
                else if (midTime > targetTime) high = mid - 1;
                else return mid;
            } catch (Exception e) { low = mid + 1; }
        }
        return closestIdx;
    }

    protected void setupTableEditors()
    {
        tblStats.createDefaultColumnsFromModel();
        
        // Renderizador de Color (Columna 0) con margen interno de 5px
        tblStats.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Color) {
                    Color color = (Color) value;
                    // Usar la misma lógica de contraste que el gráfico
                    c.setBackground(chartPanel.getDisplayColor(color));
                    setText("");
                    setBorder(BorderFactory.createLineBorder(table.getBackground(), 5));
                }
                return c;
            }
        });
        tblStats.getColumnModel().getColumn(0).setPreferredWidth(60);

        if (tblStats.getColumnCount() > 10) {
            TableColumn levelCol = tblStats.getColumnModel().getColumn(10);
            JComboBox<Integer> comboLevel = new JComboBox<>();
            for (int i = 0; i <= 100; i += 5) comboLevel.addItem(i);
            levelCol.setCellEditor(new DefaultCellEditor(comboLevel));
            levelCol.setCellRenderer(new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    if (!isSelected) setBackground(new Color(245, 245, 245));
                    setText(((value != null) ? value.toString() : "0") + " % ▼");
                    return this;
                }
            });
            tblStats.getColumnModel().getColumn(2).setPreferredWidth(50); // Ver
            tblStats.getColumnModel().getColumn(6).setPreferredWidth(100); // Ver Promedio
            tblStats.getColumnModel().getColumn(11).setPreferredWidth(160); // Ver promedio sobre %
        }
    }

    /**
     * Actualiza el tema visual de todo el componente (gráfico y tabla).
     */
    public void updateTheme() {
        // 1. Actualizar el gráfico
        chartPanel.updateTheme();
        
        // 2. Actualizar la tabla de estadísticas
        // Si hay una fila de Neutro, actualizar su color al valor global actual
        for (int i = 0; i < statsModel.getRowCount(); i++) {
            String name = (String) statsModel.getValueAt(i, 1);
            if (name.equalsIgnoreCase("Neutro") || name.equalsIgnoreCase("In")) {
                statsModel.setValueAt(glb.colorN, i, 0);
            }
        }
        
        // Forzar repintado de la tabla para que el renderizador de color actúe
        tblStats.repaint();
    }

//    /**
//     * Configura la visibilidad de las series y refresca el componente para una captura de informe.
//     * @param keywords Palabras clave para filtrar (ej: "A", "B", "C" o "SUM")
//     */
//    public void prepareForSnapshot(String... keywords) {
//        for (int i = 0; i < statsModel.getRowCount(); i++) {
//            String name = (String) statsModel.getValueAt(i, 1);
//            boolean visible = false;
//            for (String key : keywords) {
//                if (name.toUpperCase().contains(key.toUpperCase())) {
//                    visible = true;
//                    break;
//                }
//            }
//            statsModel.setValueAt(visible, i, 2);
//            chartPanel.setSeriesVisibleByName(name, visible);
//        }
//        onTimeRangeUpdated(); // Refresca estadísticas y gráfico
//    }

//    /**
//     * Captura una instantánea del componente para el informe PDF.
//     * @return ReportSnapshot con título, imagen y datos de tabla.
//     */
//    public ReportSnapshot getSnapshot() {
//        ReportSnapshot snapshot = new ReportSnapshot();
//        snapshot.title = getName();
//        snapshot.chartImage = chartPanel.getSnapshotImage(1000, 500);
//        
//        int colCount = tblStats.getColumnCount();
//        snapshot.tableHeaders = new String[colCount];
//        for (int i = 0; i < colCount; i++) {
//            snapshot.tableHeaders[i] = tblStats.getColumnName(i);
//        }
//        
//        snapshot.tableData = new ArrayList<>();
//        for (int i = 0; i < statsModel.getRowCount(); i++) {
//            // Solo incluir filas que están marcadas como visibles
//            if (!(boolean) statsModel.getValueAt(i, 2)) continue;
//            
//            String[] row = new String[colCount];
//            for (int j = 0; j < colCount; j++) {
//                Object val = statsModel.getValueAt(i, j);
//                row[j] = (val != null) ? val.toString() : "";
//            }
//            snapshot.tableData.add(row);
//        }
//        return snapshot;
//    }

//    /**
//     * DTO para almacenar la evidencia de un análisis para el informe.
//     */
//    public static class ReportSnapshot {
//        public String title;
//        public BufferedImage chartImage;
//        public String[] tableHeaders;
//        public List<String[]> tableData;
//    }

    protected boolean isColumnEditable(int column) { 
        return column == 2 || column == 6 || column == 10 || column == 11; 
    }

    private void handleTimeRangeChange()
    {
        if (masterData.isEmpty()) return;
        int start = sliderStart.getValue(), end = sliderEnd.getValue();
        if (start >= end) {
            if (sliderStart.hasFocus()) sliderEnd.setValue(start + 1);
            else sliderStart.setValue(end - 1);
        }
        updateTimeLabels();
        if (chkSyncAll.isSelected()) {
            glb.globalStartIndex = sliderStart.getValue();
            glb.globalEndIndex = sliderEnd.getValue();
            syncOtherForms();
        }
        onTimeRangeUpdated();
    }

    private void syncOtherForms()
    {
        Set<TopComponent> opened = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : opened) {
            if (tc instanceof baseTopComponent && tc != this) ((baseTopComponent) tc).updateFromGlobal();
        }
    }

    public void updateFromGlobal()
    {
        if (glb.syncTimeRange) {
            if (sliderStart.getValue() != glb.globalStartIndex) sliderStart.setValue(glb.globalStartIndex);
            if (sliderEnd.getValue() != glb.globalEndIndex) sliderEnd.setValue(glb.globalEndIndex);
            chkSyncAll.setSelected(true);
        }
    }

    protected void updateTimeLabels()
    {
        if (masterData.isEmpty()) return;
        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));
        String[] startRow = masterData.get(sIdx), endRow = masterData.get(eIdx);
        lblTimeStart.setText(startRow[0] + " " + startRow[1]);
        lblTimeEnd.setText(endRow[0] + " " + endRow[1]);
    }

    public void setData(String[] headers, List<String[]> data)
    {
        this.masterHeaders = headers;
        this.masterData = data;
        mapColumns();
        updateTimeLabels();
        onDataLoaded();
        adjustStatsTableHeight();
    }

    protected void adjustStatsTableHeight()
    {
        int rowHeight = tblStats.getRowHeight();
        if (rowHeight <= 0) rowHeight = 22;
        int headerHeight = tblStats.getTableHeader().getPreferredSize().height;
        // Forzamos visualización de exactamente 4 filas + cabecera + margen
        int totalHeight = (4 * rowHeight) + headerHeight + 12;
        scrollStats.setMinimumSize(new Dimension(100, totalHeight));
        scrollStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalHeight));
        scrollStats.setPreferredSize(new Dimension(scrollStats.getPreferredSize().width, totalHeight));
        revalidate();
    }

    protected void hideStatsColumns(int... indices)
    {
        for (int i : indices) {
            if (i < tblStats.getColumnCount()) {
                tblStats.getColumnModel().getColumn(i).setMinWidth(0);
                tblStats.getColumnModel().getColumn(i).setMaxWidth(0);
                tblStats.getColumnModel().getColumn(i).setPreferredWidth(0);
            }
        }
    }

    protected abstract void mapColumns();
    protected abstract void onDataLoaded();
    protected abstract void onTimeRangeUpdated();

    public void onRefresh()
    {
        TopComponent tc = WindowManager.getDefault().findTopComponent("dataTopComponent");
        if (tc instanceof dataTopComponent) {
            dataTopComponent dtc = (dataTopComponent) tc;
            if (dtc.hasData()) setData(dtc.getHeaders(), dtc.getDataList());
        }
    }

    /**
     * Ejecuta la exportación del análisis.
     * Siguiendo las instrucciones, ahora salta directamente a la exportación de imagen
     * para agilizar la generación de informes.
     */
    public void onExport()
    {
        exportImage();
    }

    private void exportImage()
    {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Exportar Gráfico para Informe (Fondo Blanco)");
        fc.setFileFilter(new FileNameExtensionFilter("Imagen PNG (*.png)", "png"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) file = new File(file.getAbsolutePath() + ".png");
            try {
                // El motor de gráficos ya se encarga de forzar el fondo blanco internamente
                chartPanel.saveAsImage(file);
                JOptionPane.showMessageDialog(this, "Imagen exportada correctamente para el informe.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar imagen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportCSV()
    {
        if (masterData.isEmpty()) return;
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Archivo CSV (*.csv)", "csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) file = new File(file.getAbsolutePath() + ".csv");
            try (PrintWriter pw = new PrintWriter(file)) {
                List<Integer> cols = getActiveColumnIndices();
                StringJoiner sj = new StringJoiner(",");
                for (int idx : cols) sj.add(masterHeaders[idx]);
                pw.println(sj.toString());
                int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
                int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));
                for (int i = sIdx; i <= eIdx; i++) {
                    String[] row = masterData.get(i);
                    sj = new StringJoiner(",");
                    for (int idx : cols) sj.add(row[idx]);
                    pw.println(sj.toString());
                }
                JOptionPane.showMessageDialog(this, "Datos exportados correctamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected List<Integer> getActiveColumnIndices()
    {
        List<Integer> indices = new ArrayList<>();
        indices.add(0); indices.add(1);
        for (int i = 0; i < statsModel.getRowCount(); i++) {
            String name = (String) statsModel.getValueAt(i, 1);
            for (int j = 0; j < masterHeaders.length; j++) {
                if (masterHeaders[j].equals(name)) { indices.add(j); break; }
            }
        }
        return indices;
    }

    @Override
    public void componentClosed() { masterHeaders = new String[0]; masterData = new ArrayList<>(); }
}

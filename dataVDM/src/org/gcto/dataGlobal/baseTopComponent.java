/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.gcto.dataVDM.dataTopComponent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Clase base para todos los formularios de análisis.
 * Estructura optimizada:
 * - NORTH: Selector de Fases + Acciones (Iconos) + Tabla de Estadísticas
 * - CENTER: Contenedor del Gráfico (Java2D)
 * - SOUTH: Fila única [Label, Slider, Slider, Label, CheckBox]
 * 
 * @author camilo
 */
public abstract class baseTopComponent extends TopComponent {
    
    protected JPanel pnlPhaseSelection;
    protected JPanel pnlChartContainer;
    protected FastChartPanel chartPanel;
    protected JTable tblStats;
    protected DefaultTableModel statsModel;
    protected JScrollPane scrollStats;
    
    // Controles de Rango Temporal
    protected JSlider sliderStart;
    protected JSlider sliderEnd;
    protected JLabel lblTimeStart;
    protected JLabel lblTimeEnd;
    protected JCheckBox chkSyncAll;
    
    // Referencias a los datos maestros
    protected String[] masterHeaders = new String[0];
    protected List<String[]> masterData = new ArrayList<>();
    protected int[] activeColumnIndices = new int[0];
    
    protected JButton btnRefresh;
    protected JButton btnExport;

    public baseTopComponent() {
        initComponentsBase();
    }

    private void initComponentsBase() {
        setLayout(new BorderLayout());
        
        // --- PANEL NORTE (Vertical) ---
        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.Y_AXIS));
        
        // 1. Fila Superior: Selector de Fases + Acciones
        JPanel pnlTopRow = new JPanel(new BorderLayout());
        
        pnlPhaseSelection = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlPhaseSelection.setBorder(BorderFactory.createTitledBorder("SELECTOR DE FASES"));
        pnlTopRow.add(pnlPhaseSelection, BorderLayout.CENTER);
        
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnRefresh = new JButton();
        try {
            btnRefresh.setIcon(new ImageIcon(getClass().getResource("/org/gcto/dataGlobal/refrescar.png")));
        } catch (Exception e) {}
        btnRefresh.setToolTipText("Refrescar");
        btnRefresh.setPreferredSize(new Dimension(32, 32));
        btnRefresh.addActionListener(e -> onRefresh());
        
        btnExport = new JButton();
        try {
            btnExport.setIcon(new ImageIcon(getClass().getResource("/org/gcto/dataGlobal/export.png")));
        } catch (Exception e) {}
        btnExport.setToolTipText("Exportar");
        btnExport.setPreferredSize(new Dimension(32, 32));
        btnExport.addActionListener(e -> onExport());
        
        pnlActions.add(btnRefresh);
        pnlActions.add(btnExport);
        pnlTopRow.add(pnlActions, BorderLayout.EAST);
        
        pnlNorth.add(pnlTopRow);
        
        // 2. Tabla de Estadísticas (crece según filas)
        // Columnas: Nombre(0), Min(1), F.Min(2), PromA(3), Ver Promedio(4), Max(5), F.Max(6), Promedio sobre %(7), % de Nivel(8), Ver promedio sobre %(9)
        String[] statsHeaders = {"Nombre", "Minimo", "Fecha Min", "Promedio Arit.", "Ver Promedio", "Maximo", "Fecha Max", "Promedio sobre %", "% de Nivel", "Ver promedio sobre %"};
        statsModel = new DefaultTableModel(statsHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return isColumnEditable(column);
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 9) return Boolean.class;
                if (columnIndex == 8) return Integer.class;
                return super.getColumnClass(columnIndex);
            }
        };
        
        statsModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int col = e.getColumn();
                if (col == 4 || col == 8 || col == 9) {
                    onTimeRangeUpdated();
                }
            }
        });
        
        tblStats = new JTable(statsModel);
        setupTableEditors();
        
        scrollStats = new JScrollPane(tblStats);
        scrollStats.setMinimumSize(new Dimension(100, 50));
        pnlNorth.add(scrollStats);
        
        add(pnlNorth, BorderLayout.NORTH);
        
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
        
        // --- PANEL SUR: Rango Temporal (Fila Única) ---
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
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
        
        // Listeners
        sliderStart.addChangeListener(e -> handleTimeRangeChange());
        sliderEnd.addChangeListener(e -> handleTimeRangeChange());
    }

    /**
     * Configura los editores de celda (ComboBox, Checkboxes) para la tabla de estadísticas.
     * Debe llamarse después de cualquier cambio en la estructura de columnas.
     */
    protected void setupTableEditors() {
        // Forzar a la tabla a sincronizar sus columnas con el modelo
        tblStats.createDefaultColumnsFromModel();
        
        if (tblStats.getColumnCount() > 8) {
            // 1. Configurar ComboBox para % de Nivel (índice 8)
            TableColumn levelCol = tblStats.getColumnModel().getColumn(8);
            JComboBox<Integer> comboLevel = new JComboBox<>();
            for (int i = 0; i <= 100; i += 5) comboLevel.addItem(i);
            levelCol.setCellEditor(new DefaultCellEditor(comboLevel));
            
            // 2. Añadir un renderizador visual para indicar que es un ComboBox
            levelCol.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    if (!isSelected) setBackground(new Color(245, 245, 245));
                    String valStr = (value != null) ? value.toString() : "0";
                    setText(valStr + " % ▼"); // Flecha indicadora
                    return this;
                }
            });
            
            // 3. Ajustar anchos de columnas de Ver (Checkboxes)
            tblStats.getColumnModel().getColumn(4).setPreferredWidth(100); // Ver Promedio
            tblStats.getColumnModel().getColumn(9).setPreferredWidth(160); // Ver promedio sobre %
        }
    }

    /**
     * Determina si una columna es editable. Las subclases pueden extender esto.
     */
    protected boolean isColumnEditable(int column) {
        return column == 4 || column == 8 || column == 9;
    }

    private void handleTimeRangeChange() {
        if (masterData.isEmpty()) return;
        int start = sliderStart.getValue();
        int end = sliderEnd.getValue();
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

    private void syncOtherForms() {
        Set<TopComponent> opened = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : opened) {
            if (tc instanceof baseTopComponent && tc != this) {
                ((baseTopComponent) tc).updateFromGlobal();
            }
        }
    }

    public void updateFromGlobal() {
        if (glb.syncTimeRange) {
            if (sliderStart.getValue() != glb.globalStartIndex) sliderStart.setValue(glb.globalStartIndex);
            if (sliderEnd.getValue() != glb.globalEndIndex) sliderEnd.setValue(glb.globalEndIndex);
            chkSyncAll.setSelected(true);
        }
    }

    protected void updateTimeLabels() {
        if (masterData.isEmpty()) return;
        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));
        String[] startRow = masterData.get(sIdx);
        String[] endRow = masterData.get(eIdx);
        lblTimeStart.setText(startRow[0] + " " + startRow[1]);
        lblTimeEnd.setText(endRow[0] + " " + endRow[1]);
    }

    public void setData(String[] headers, List<String[]> data) {
        this.masterHeaders = headers;
        this.masterData = data;
        mapColumns();
        updateTimeLabels();
        onDataLoaded();
        adjustStatsTableHeight();
    }
    
    protected void adjustStatsTableHeight() {
        int rowCount = statsModel.getRowCount();
        int rowHeight = tblStats.getRowHeight();
        int headerHeight = tblStats.getTableHeader().getPreferredSize().height;
        int totalHeight = (rowCount * rowHeight) + headerHeight + 10;
        scrollStats.setPreferredSize(new Dimension(scrollStats.getPreferredSize().width, Math.min(totalHeight, 300)));
        revalidate();
    }
    
    /**
     * Oculta columnas específicas de la tabla de estadísticas.
     * @param indices Los índices de las columnas a ocultar.
     */
    protected void hideStatsColumns(int... indices) {
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
    
    protected void onRefresh() {
        TopComponent tc = WindowManager.getDefault().findTopComponent("dataTopComponent");
        if (tc instanceof dataTopComponent) {
            dataTopComponent dtc = (dataTopComponent) tc;
            if (dtc.hasData()) {
                setData(dtc.getHeaders(), dtc.getDataList());
            }
        }
    }
    
    protected void onExport() {
        Object[] options = {"Imagen (PNG)", "Datos (CSV - Rango Seleccionado)"};
        int choice = JOptionPane.showOptionDialog(this, 
                "Seleccione el formato de exportación:", 
                "Exportar Análisis",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, options, options[0]);
        
        if (choice == 0) {
            exportImage();
        } else if (choice == 1) {
            exportCSV();
        }
    }

    private void exportImage() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Imagen PNG (*.png)", "png"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getAbsolutePath() + ".png");
            }
            try {
                chartPanel.saveAsImage(file);
                JOptionPane.showMessageDialog(this, "Imagen guardada correctamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar imagen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportCSV() {
        if (masterData.isEmpty()) return;
        
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Archivo CSV (*.csv)", "csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            
            try (PrintWriter pw = new PrintWriter(file)) {
                List<Integer> cols = getActiveColumnIndices();
                
                // Escribir encabezados
                StringJoiner sj = new StringJoiner(",");
                for (int idx : cols) sj.add(masterHeaders[idx]);
                pw.println(sj.toString());
                
                // Escribir datos en el rango
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

    protected List<Integer> getActiveColumnIndices() {
        List<Integer> indices = new ArrayList<>();
        indices.add(0); // Fecha
        indices.add(1); // Hora
        for (int i = 0; i < statsModel.getRowCount(); i++) {
            String name = (String) statsModel.getValueAt(i, 0);
            for (int j = 0; j < masterHeaders.length; j++) {
                if (masterHeaders[j].equals(name)) {
                    indices.add(j);
                    break;
                }
            }
        }
        return indices;
    }
    
    @Override
    public void componentClosed() {
        masterHeaders = new String[0];
        masterData = new ArrayList<>();
        activeColumnIndices = new int[0];
    }
}

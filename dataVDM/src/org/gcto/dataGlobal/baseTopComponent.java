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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
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
 * Clase base para todos los formularios de análisis. Estructura optimizada: -
 * NORTH: Selector de Fases + Acciones (Iconos) + Tabla de Estadísticas -
 * CENTER: Contenedor del Gráfico (Java2D) - SOUTH: Fila única [Label, Slider,
 * Slider, Label, CheckBox]
 *
 * @author camilo
 */
public abstract class baseTopComponent extends TopComponent
{

    protected JPanel pnlPhaseSelection;
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

    // Referencias a los datos maestros
    protected String[] masterHeaders = new String[0];
    protected List<String[]> masterData = new ArrayList<>();
    protected int[] activeColumnIndices = new int[0];

    protected JButton btnRefresh;
    protected JButton btnExport;

    public baseTopComponent()
    {
        initComponentsBase();
    }

    private void initComponentsBase()
    {
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
        try
        {
            btnRefresh.setIcon(new ImageIcon(getClass().getResource("/org/gcto/dataGlobal/refrescar.png")));
        } catch (Exception e)
        {
        }
        btnRefresh.setToolTipText("Refrescar");
        btnRefresh.setPreferredSize(new Dimension(32, 32));
        btnRefresh.addActionListener(e -> onRefresh());

        btnExport = new JButton();
        try
        {
            btnExport.setIcon(new ImageIcon(getClass().getResource("/org/gcto/dataGlobal/export.png")));
        } catch (Exception e)
        {
        }
        btnExport.setToolTipText("Exportar");
        btnExport.setPreferredSize(new Dimension(32, 32));
        btnExport.addActionListener(e -> onExport());

        pnlActions.add(btnRefresh);
        pnlActions.add(btnExport);
        pnlTopRow.add(pnlActions, BorderLayout.EAST);

        pnlNorth.add(pnlTopRow);

        // 2. Tabla de Estadísticas (crece según filas)
        String[] statsHeaders =
        {
            "Nombre", "Minimo", "Fecha Min", "Promedio Arit.", "Ver Promedio", "Maximo", "Fecha Max", "Promedio sobre %", "% de Nivel", "Ver promedio sobre %"
        };
        statsModel = new DefaultTableModel(statsHeaders, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return isColumnEditable(column);
            }

            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                if (columnIndex == 4 || columnIndex == 9)
                {
                    return Boolean.class;
                }
                if (columnIndex == 8)
                {
                    return Integer.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        statsModel.addTableModelListener(e ->
        {
            if (e.getType() == TableModelEvent.UPDATE)
            {
                int col = e.getColumn();
                if (col == 4 || col == 8 || col == 9)
                {
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
        chartPanel.setRangeSelectionListener((startIdx, endIdx) ->
        {
            if (masterData.isEmpty())
            {
                return;
            }
            int sVal = (int) ((startIdx / (double) (masterData.size() - 1)) * 1000);
            int eVal = (int) ((endIdx / (double) (masterData.size() - 1)) * 1000);
            sliderStart.setValue(sVal);
            sliderEnd.setValue(eVal);
        });
        pnlChartContainer.add(chartPanel, BorderLayout.CENTER);

        add(pnlChartContainer, BorderLayout.CENTER);

        // --- PANEL SUR: Rango Temporal (Fila Única) ---
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

        // Configurar interactividad de etiquetas de tiempo
        setupTimeLabelInteractivity(lblTimeStart);
        setupTimeLabelInteractivity(lblTimeEnd);

        // Listeners de Sliders
        sliderStart.addChangeListener(e -> handleTimeRangeChange());
        sliderEnd.addChangeListener(e -> handleTimeRangeChange());
    }

    private void setupTimeLabelInteractivity(JLabel label)
    {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Estilo del globo: Fondo azul claro, letras negras
        RoundedBalloonStyle tipStyle = new RoundedBalloonStyle(5, 5, new Color(173, 216, 230), Color.BLUE);
        JLabel lblContent = new JLabel("click para ajuste preciso de tiempos");
        lblContent.setForeground(Color.BLACK);

        final BalloonTip tip = new BalloonTip(label, lblContent, tipStyle, false);
        tip.setVisible(false);

        label.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                showTimeAdjustmentDialog();
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                tip.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                tip.setVisible(false);
            }
        });
    }

    private void showTimeAdjustmentDialog()
    {
        if (masterData.isEmpty())
        {
            return;
        }

        //se coloca el formato correcto para que no de error
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date minProjectDate, maxProjectDate;
        try
        {
            minProjectDate = sdf.parse(masterData.get(0)[0] + " " + masterData.get(0)[1]);
            maxProjectDate = sdf.parse(masterData.get(masterData.size() - 1)[0] + " " + masterData.get(masterData.size() - 1)[1]);
        } catch (ParseException ex)
        {
            System.out.println("Excepcion a leer el dato de tiempo : " + ex.getMessage());
            return;
        }

        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));

        Date dateStart, dateEnd;
        try
        {
            dateStart = sdf.parse(masterData.get(sIdx)[0] + " " + masterData.get(sIdx)[1]);
            dateEnd = sdf.parse(masterData.get(eIdx)[0] + " " + masterData.get(eIdx)[1]);
        } catch (ParseException ex)
        {
            dateStart = minProjectDate;
            dateEnd = maxProjectDate;
        }

        final JSpinner spnStart = new JSpinner(new SpinnerDateModel(dateStart, minProjectDate, maxProjectDate, java.util.Calendar.MINUTE));
        final JSpinner spnEnd = new JSpinner(new SpinnerDateModel(dateEnd, minProjectDate, maxProjectDate, java.util.Calendar.MINUTE));

        spnStart.setEditor(new JSpinner.DateEditor(spnStart, "yyyy-MM-dd HH:mm"));
        spnEnd.setEditor(new JSpinner.DateEditor(spnEnd, "yyyy-MM-dd HH:mm"));

        // Lógica de separación dinámica (mínimo 1 minuto)
        spnStart.addChangeListener(ev ->
        {
            Date s = (Date) spnStart.getValue();
            Date f = (Date) spnEnd.getValue();
            if (f.getTime() - s.getTime() < 60000)
            {
                long next = s.getTime() + 60000;
                if (next <= maxProjectDate.getTime())
                {
                    spnEnd.setValue(new Date(next));
                } else
                {
                    spnStart.setValue(new Date(maxProjectDate.getTime() - 60000));
                }
            }
        });

        spnEnd.addChangeListener(ev ->
        {
            Date s = (Date) spnStart.getValue();
            Date f = (Date) spnEnd.getValue();
            if (f.getTime() - s.getTime() < 60000)
            {
                long prev = f.getTime() - 60000;
                if (prev >= minProjectDate.getTime())
                {
                    spnStart.setValue(new Date(prev));
                } else
                {
                    spnEnd.setValue(new Date(minProjectDate.getTime() + 60000));
                }
            }
        });

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.add(new JLabel("Inicio:"));
        pnl.add(spnStart);
        pnl.add(new JLabel("Fin:"));
        pnl.add(spnEnd);

        int result = JOptionPane.showConfirmDialog(this, pnl, "Ajuste Preciso de Tiempos", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION)
        {
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
        if (masterData.isEmpty())
        {
            return 0;
        }
        int low = 0;
        int high = masterData.size() - 1;
        long targetTime = target.getTime();
        int closestIdx = 0;
        long minDiff = Long.MAX_VALUE;

        while (low <= high)
        {
            int mid = low + (high - low) / 2;
            try
            {
                Date midDate = sdf.parse(masterData.get(mid)[0] + " " + masterData.get(mid)[1]);
                long midTime = midDate.getTime();
                long diff = Math.abs(midTime - targetTime);

                if (diff < minDiff)
                {
                    minDiff = diff;
                    closestIdx = mid;
                }

                if (midTime < targetTime)
                {
                    low = mid + 1;
                } else if (midTime > targetTime)
                {
                    high = mid - 1;
                } else
                {
                    return mid;
                }
            } catch (Exception e)
            {
                low = mid + 1;
            }
        }
        return closestIdx;
    }

    /**
     * Configura los editores de celda (ComboBox, Checkboxes) para la tabla de
     * estadísticas.
     */
    protected void setupTableEditors()
    {
        tblStats.createDefaultColumnsFromModel();

        if (tblStats.getColumnCount() > 8)
        {
            TableColumn levelCol = tblStats.getColumnModel().getColumn(8);
            JComboBox<Integer> comboLevel = new JComboBox<>();
            for (int i = 0; i <= 100; i += 5)
            {
                comboLevel.addItem(i);
            }
            levelCol.setCellEditor(new DefaultCellEditor(comboLevel));

            levelCol.setCellRenderer(new DefaultTableCellRenderer()
            {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
                {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    if (!isSelected)
                    {
                        setBackground(new Color(245, 245, 245));
                    }
                    String valStr = (value != null) ? value.toString() : "0";
                    setText(valStr + " % ▼");
                    return this;
                }
            });

            tblStats.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblStats.getColumnModel().getColumn(9).setPreferredWidth(160);
        }
    }

    protected boolean isColumnEditable(int column)
    {
        return column == 4 || column == 8 || column == 9;
    }

    private void handleTimeRangeChange()
    {
        if (masterData.isEmpty())
        {
            return;
        }
        int start = sliderStart.getValue();
        int end = sliderEnd.getValue();
        if (start >= end)
        {
            if (sliderStart.hasFocus())
            {
                sliderEnd.setValue(start + 1);
            } else
            {
                sliderStart.setValue(end - 1);
            }
        }
        updateTimeLabels();
        if (chkSyncAll.isSelected())
        {
            glb.globalStartIndex = sliderStart.getValue();
            glb.globalEndIndex = sliderEnd.getValue();
            syncOtherForms();
        }
        onTimeRangeUpdated();
    }

    private void syncOtherForms()
    {
        Set<TopComponent> opened = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : opened)
        {
            if (tc instanceof baseTopComponent && tc != this)
            {
                ((baseTopComponent) tc).updateFromGlobal();
            }
        }
    }

    public void updateFromGlobal()
    {
        if (glb.syncTimeRange)
        {
            if (sliderStart.getValue() != glb.globalStartIndex)
            {
                sliderStart.setValue(glb.globalStartIndex);
            }
            if (sliderEnd.getValue() != glb.globalEndIndex)
            {
                sliderEnd.setValue(glb.globalEndIndex);
            }
            chkSyncAll.setSelected(true);
        }
    }

    protected void updateTimeLabels()
    {
        if (masterData.isEmpty())
        {
            return;
        }
        int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
        int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));
        String[] startRow = masterData.get(sIdx);
        String[] endRow = masterData.get(eIdx);
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
        int rowCount = statsModel.getRowCount();
        int rowHeight = tblStats.getRowHeight();
        int headerHeight = tblStats.getTableHeader().getPreferredSize().height;
        int totalHeight = (rowCount * rowHeight) + headerHeight + 10;
        scrollStats.setPreferredSize(new Dimension(scrollStats.getPreferredSize().width, Math.min(totalHeight, 300)));
        revalidate();
    }

    protected void hideStatsColumns(int... indices)
    {
        for (int i : indices)
        {
            if (i < tblStats.getColumnCount())
            {
                tblStats.getColumnModel().getColumn(i).setMinWidth(0);
                tblStats.getColumnModel().getColumn(i).setMaxWidth(0);
                tblStats.getColumnModel().getColumn(i).setPreferredWidth(0);
            }
        }
    }

    protected abstract void mapColumns();

    protected abstract void onDataLoaded();

    protected abstract void onTimeRangeUpdated();

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

    protected void onExport()
    {
        Object[] options =
        {
            "Imagen (PNG)", "Datos (CSV - Rango Seleccionado)"
        };
        int choice = JOptionPane.showOptionDialog(this,
                "Seleccione el formato de exportación:",
                "Exportar Análisis",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == 0)
        {
            exportImage();
        } else if (choice == 1)
        {
            exportCSV();
        }
    }

    private void exportImage()
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Imagen PNG (*.png)", "png"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png"))
            {
                file = new File(file.getAbsolutePath() + ".png");
            }
            try
            {
                chartPanel.saveAsImage(file);
                JOptionPane.showMessageDialog(this, "Imagen guardada correctamente.");
            } catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, "Error al guardar imagen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportCSV()
    {
        if (masterData.isEmpty())
        {
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Archivo CSV (*.csv)", "csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv"))
            {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try (PrintWriter pw = new PrintWriter(file))
            {
                List<Integer> cols = getActiveColumnIndices();

                StringJoiner sj = new StringJoiner(",");
                for (int idx : cols)
                {
                    sj.add(masterHeaders[idx]);
                }
                pw.println(sj.toString());

                int sIdx = (int) ((sliderStart.getValue() / 1000.0) * (masterData.size() - 1));
                int eIdx = (int) ((sliderEnd.getValue() / 1000.0) * (masterData.size() - 1));

                for (int i = sIdx; i <= eIdx; i++)
                {
                    String[] row = masterData.get(i);
                    sj = new StringJoiner(",");
                    for (int idx : cols)
                    {
                        sj.add(row[idx]);
                    }
                    pw.println(sj.toString());
                }
                JOptionPane.showMessageDialog(this, "Datos exportados correctamente.");
            } catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, "Error al exportar CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected List<Integer> getActiveColumnIndices()
    {
        List<Integer> indices = new ArrayList<>();
        indices.add(0); // Fecha
        indices.add(1); // Hora
        for (int i = 0; i < statsModel.getRowCount(); i++)
        {
            String name = (String) statsModel.getValueAt(i, 0);
            for (int j = 0; j < masterHeaders.length; j++)
            {
                if (masterHeaders[j].equals(name))
                {
                    indices.add(j);
                    break;
                }
            }
        }
        return indices;
    }

    @Override
    public void componentClosed()
    {
        masterHeaders = new String[0];
        masterData = new ArrayList<>();
        activeColumnIndices = new int[0];
    }
}

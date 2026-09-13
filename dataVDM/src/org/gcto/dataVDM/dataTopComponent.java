/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/templateTopComponent637.java to edit this template
 */
package org.gcto.dataVDM;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.gcto.dataCorrientes.dataCorrientes;
import org.gcto.dataTensiones.verTensiones;
import org.gcto.dataPotAparente.verPotAparente;
import org.gcto.dataPotActiva.verPotActiva;
import org.gcto.dataPotReactiva.verPotReactiva;
import org.gcto.dataFactoPotencia.verFP;
import org.gcto.dataGlobal.ETipoRED;
import org.gcto.dataGlobal.glb;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.SystemAction;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.gcto.dataVDM//data//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "dataTopComponent",
        iconBase = "org/gcto/dataVDM/tabla.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "org.gcto.dataVDM.dataTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_dataAction",
        preferredID = "dataTopComponent"
)
@Messages(
        {
            "CTL_dataAction=data",
            "CTL_dataTopComponent=data Window",
            "HINT_dataTopComponent=This is a data window"
        })
public final class dataTopComponent extends TopComponent
{
    
    private static final Logger LOG = Logger.getLogger(dataTopComponent.class.getName());
    private final DataTableModel tableModel;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));
    
    private final DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer()
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Object displayValue = value;
            if (value != null)
            {
                String s = value.toString().trim();
                if (!s.isEmpty())
                {
                    try
                    {
                        double d = glb.parseDoubleSafe(s);
                        displayValue = decimalFormat.format(d);
                    } catch (Exception e)
                    {
                        // No es un número, dejar como está
                    }
                }
            }
            return super.getTableCellRendererComponent(table, displayValue, isSelected, hasFocus, row, column);
        }
    };
    
    private final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    private String productSN = "";
    
    public dataTopComponent()
    {
        initComponents();
        setName(Bundle.CTL_dataTopComponent());
        setToolTipText(Bundle.HINT_dataTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        
        tableModel = new DataTableModel();
        dataTable.setModel(tableModel);

        // Deshabilitar ordenamiento de forma absoluta
        dataTable.setAutoCreateRowSorter(false);
        dataTable.setRowSorter(null);
        
        dataTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Configuración para scroll horizontal
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Menú contextual
        JPopupMenu popup = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Eliminar filas seleccionadas");
        deleteItem.addActionListener(e -> btnDeleteRowActionPerformed(null));
        popup.add(deleteItem);
        dataTable.setComponentPopupMenu(popup);
        
        updateStatus();
    }

    /**
     * Indica si hay datos cargados en la tabla.
     *
     * @return true si hay al menos una fila de datos.
     */
    public boolean hasData()
    {
        return tableModel != null && tableModel.getRowCount() > 0;
    }
    
    public String[] getHeaders()
    {
        return tableModel.getColumnNames();
    }
    
    public List<String[]> getDataList()
    {
        return tableModel.getDataList();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        toolBar = new javax.swing.JToolBar();
        btnLoad = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnDeleteRow = new javax.swing.JButton();
        btnClearZeros = new javax.swing.JButton();
        btnFitColumns = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        statusPanel = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setPreferredSize(new java.awt.Dimension(100, 34));

        btnLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gcto/dataVDM/abirCSV.png"))); // NOI18N
        btnLoad.setToolTipText(org.openide.util.NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.btnLoad.toolTip")); // NOI18N
        btnLoad.setFocusable(false);
        btnLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLoad.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnLoadActionPerformed(evt);
            }
        });
        toolBar.add(btnLoad);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gcto/dataVDM/guardar.png"))); // NOI18N
        btnSave.setToolTipText(org.openide.util.NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.btnSave.toolTip")); // NOI18N
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });
        toolBar.add(btnSave);
        toolBar.add(jSeparator1);

        btnDeleteRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gcto/dataVDM/eliminar.png"))); // NOI18N
        btnDeleteRow.setToolTipText(org.openide.util.NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.btnDeleteRow.toolTip")); // NOI18N
        btnDeleteRow.setFocusable(false);
        btnDeleteRow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeleteRow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDeleteRow.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteRowActionPerformed(evt);
            }
        });
        toolBar.add(btnDeleteRow);

        btnClearZeros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gcto/dataVDM/limpiarCeros.png"))); // NOI18N
        btnClearZeros.setToolTipText(org.openide.util.NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.btnClearZeros.toolTip")); // NOI18N
        btnClearZeros.setFocusable(false);
        btnClearZeros.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClearZeros.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClearZeros.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClearZerosActionPerformed(evt);
            }
        });
        toolBar.add(btnClearZeros);

        btnFitColumns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gcto/dataVDM/ajustar.png"))); // NOI18N
        btnFitColumns.setToolTipText(org.openide.util.NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.btnFitColumns.toolTip")); // NOI18N
        btnFitColumns.setFocusable(false);
        btnFitColumns.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFitColumns.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFitColumns.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFitColumnsActionPerformed(evt);
            }
        });
        toolBar.add(btnFitColumns);

        add(toolBar, java.awt.BorderLayout.NORTH);

        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        scrollPane.setViewportView(dataTable);

        add(scrollPane, java.awt.BorderLayout.CENTER);

        statusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusPanel.setPreferredSize(new java.awt.Dimension(100, 25));
        statusPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        org.openide.util.NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.lblStatus.text"); // NOI18N
        lblStatus.setText(org.openide.util.NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.lblStatus.text")); // NOI18N
        statusPanel.add(lblStatus);

        add(statusPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLoadActionPerformed
    {
        
        AbrirProyectoNuevo apn = new AbrirProyectoNuevo(null, true);
        apn.setLocationRelativeTo(null);
        apn.setVisible(true);
        
        if (glb.selectedFileCSV != null)
        {
            loadCsv(glb.selectedFileCSV);
            return;
        }
        if (glb.seletedFileVDM != null)
        {
            loadBinary(glb.seletedFileVDM);
            cargarProyectoVDM();
        }

//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("Datos (CSV, VDM)", "csv", "vdm"));
//        int result = fileChooser.showOpenDialog(this);
//        if (result == JFileChooser.APPROVE_OPTION)
//        {
//            File selectedFile = fileChooser.getSelectedFile();
//            if (selectedFile.getName().toLowerCase().endsWith(".vdm")) {
//                loadBinary(selectedFile);
//            } else {
//                loadCsv(selectedFile);
//            }
//        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {
        if (tableModel.getRowCount() == 0)
        {
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Sesión VistaDatos (.vdm)", "vdm"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".vdm"))
            {
                file = new File(file.getAbsolutePath() + ".vdm");
            }
            saveBinary(file);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteRowActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteRowActionPerformed
    {
        int[] selectedRows = dataTable.getSelectedRows();
        if (selectedRows.length > 0)
        {
            int[] modelRows = new int[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++)
            {
                modelRows[i] = dataTable.convertRowIndexToModel(selectedRows[i]);
            }
            Arrays.sort(modelRows);
            tableModel.removeRows(modelRows);
            updateStatus();
        } else
        {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una o más filas para eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteRowActionPerformed

    private void btnClearZerosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnClearZerosActionPerformed
    {
        if (tableModel.getRowCount() == 0)
        {
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar automáticamente todas las filas que contienen solo ceros?",
                "Limpiar Datos", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION)
        {
            tableModel.clearZeroRows();
            updateStatus();
        }
    }//GEN-LAST:event_btnClearZerosActionPerformed
    
    private void btnFitColumnsActionPerformed(java.awt.event.ActionEvent evt)
    {
        applyRenderers();
    }
    
    private void loadCsv(File file)
    {
        setUIEnabled(false);
        String msg = NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.loading");
        lblStatus.setText(msg);
        
        final ProgressHandle ph = ProgressHandle.createHandle(msg);
        ph.start(100); // 100 unidades de trabajo (porcentaje)

        new SwingWorker<Void, Integer>()
        {
            private String[] combinedColumns;
            private String sn = "";
            private final List<String[]> data = new ArrayList<>(1000000);
            
            @Override
            protected Void doInBackground() throws Exception
            {
                long totalBytes = file.length();
                long readBytes = 0;
                
                try (BufferedReader br = new BufferedReader(new FileReader(file)))
                {
                    String lineSN = br.readLine();
                    if (lineSN != null)
                    {
                        sn = lineSN.trim();
                        readBytes += lineSN.length() + 1;
                    }
                    
                    String lineMain = br.readLine();
                    if (lineMain != null)
                    {
                        readBytes += lineMain.length() + 1;
                    }
                    String lineSub = br.readLine();
                    if (lineSub != null)
                    {
                        readBytes += lineSub.length() + 1;
                    }
                    
                    if (lineMain != null && lineSub != null)
                    {
                        String[] mainHeaders = lineMain.split(",", -1);
                        String[] subHeaders = lineSub.split(",", -1);
                        combinedColumns = new String[subHeaders.length];
                        
                        String currentMain = "";
                        for (int i = 0; i < subHeaders.length; i++)
                        {
                            if (i < mainHeaders.length && !mainHeaders[i].trim().isEmpty())
                            {
                                currentMain = mainHeaders[i].trim();
                            }
                            String sub = subHeaders[i].trim();
                            combinedColumns[i] = currentMain.isEmpty() ? sub : currentMain + ": " + sub;
                        }
                    }
                    
                    String line;
                    int lastProgress = 0;
                    while ((line = br.readLine()) != null)
                    {
                        data.add(line.split(",", -1));
                        readBytes += line.length() + 1;
                        
                        int progress = (int) ((readBytes * 100) / totalBytes);
                        if (progress > lastProgress)
                        {
                            publish(progress);
                            lastProgress = progress;
                        }
                    }

                    // Aumentar datos con columnas calculadas
                    combinedColumns = augmentData(combinedColumns, data);
                }
                return null;
            }
            
            @Override
            protected void process(List<Integer> chunks)
            {
                for (int progress : chunks)
                {
                    ph.progress(progress);
                }
            }
            
            @Override
            protected void done()
            {
                try
                {
                    get();
                    productSN = sn;
                    tableModel.setColumns(combinedColumns);
                    tableModel.setData(data);
                    applyRenderers();
                    updateStatus();
                } catch (Exception e)
                {
                    showError(NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.error.load"), e);
                } finally
                {
                    ph.finish();
                    setUIEnabled(true);
                }
            }
        }.execute();
    }
    
    private void saveBinary(File file)
    {
        setUIEnabled(false);
        String msg = NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.saving");
        lblStatus.setText(msg);
        
        final ProgressHandle ph = ProgressHandle.createHandle(msg);
        final List<String[]> dataList = tableModel.getDataList();
        ph.start(100);
        
        new SwingWorker<Void, Integer>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file))))
                {
                    oos.writeUTF(productSN);
                    oos.writeObject(tableModel.getColumnNames());

                    // Escribimos el tamaño de la lista
                    oos.writeInt(dataList.size());
                    
                    int lastProgress = 0;
                    for (int i = 0; i < dataList.size(); i++)
                    {
                        oos.writeObject(dataList.get(i));
                        
                        int progress = (int) ((i * 100.0) / dataList.size());
                        if (progress > lastProgress)
                        {
                            publish(progress);
                            lastProgress = progress;
                        }
                    }
                }
                return null;
            }
            
            @Override
            protected void process(List<Integer> chunks)
            {
                for (int progress : chunks)
                {
                    ph.progress(progress);
                }
            }
            
            @Override
            protected void done()
            {
                try
                {
                    get();
                    updateStatus();
                } catch (Exception e)
                {
                    showError(NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.error.save"), e);
                } finally
                {
                    ph.finish();
                    setUIEnabled(true);
                }
            }
        }.execute();
    }
    
    private void loadBinary(File file)
    {
        setUIEnabled(false);
        String msg = NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.loading");
        lblStatus.setText(msg);
        
        final ProgressHandle ph = ProgressHandle.createHandle(msg);
        ph.start(100);
        
        new SwingWorker<Void, Integer>()
        {
            private String sn;
            private String[] cols;
            private List<String[]> data;
            
            @Override
            protected Void doInBackground() throws Exception
            {
                try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file))))
                {
                    sn = ois.readUTF();
                    cols = (String[]) ois.readObject();
                    
                    int size = ois.readInt();
                    data = new ArrayList<>(size);
                    
                    int lastProgress = 0;
                    for (int i = 0; i < size; i++)
                    {
                        data.add((String[]) ois.readObject());
                        
                        int progress = (int) ((i * 100.0) / size);
                        if (progress > lastProgress)
                        {
                            publish(progress);
                            lastProgress = progress;
                        }
                    }

                    // Aumentar datos con columnas calculadas si no están presentes
                    cols = augmentData(cols, data);
                }
                return null;
            }
            
            @Override
            protected void process(List<Integer> chunks)
            {
                for (int progress : chunks)
                {
                    ph.progress(progress);
                }
            }
            
            @Override
            protected void done()
            {
                try
                {
                    get();
                    productSN = sn;
                    tableModel.setColumns(cols);
                    tableModel.setData(data);
                    applyRenderers();
                    updateStatus();
                } catch (Exception e)
                {
                    showError(NbBundle.getMessage(dataTopComponent.class, "dataTopComponent.error.load"), e);
                } finally
                {
                    ph.finish();
                    setUIEnabled(true);
                }
            }
        }.execute();
    }
    
    private void setUIEnabled(boolean enabled)
    {
        btnLoad.setEnabled(enabled);
        btnSave.setEnabled(enabled);
        btnDeleteRow.setEnabled(enabled);
        btnClearZeros.setEnabled(enabled);
        btnFitColumns.setEnabled(enabled);
    }
    
    private void showError(String msg, Exception e)
    {
        JOptionPane.showMessageDialog(this, msg + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        lblStatus.setText("Error.");
    }
    
    private void applyRenderers()
    {
        if (tableModel.getColumnCount() == 0)
        {
            return;
        }
        
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = dataTable.getColumnModel();
        java.awt.FontMetrics fm = dataTable.getFontMetrics(dataTable.getFont());
        java.awt.FontMetrics headerFm = dataTable.getTableHeader().getFontMetrics(dataTable.getTableHeader().getFont());
        
        for (int i = 0; i < columnModel.getColumnCount(); i++)
        {
            String colName = tableModel.getColumnName(i);

            // Calcular ancho basado en el encabezado (con margen generoso)
            int width = headerFm.stringWidth(colName) + 50;

            // Muestrear filas para el ancho del contenido (aumentamos a 500 filas)
            int maxContentWidth = 0;
            int rowsToSample = Math.min(tableModel.getRowCount(), 500);
            for (int r = 0; r < rowsToSample; r++)
            {
                Object val = tableModel.getValueAt(r, i);
                if (val != null)
                {
                    int w = fm.stringWidth(val.toString()) + 30;
                    if (w > maxContentWidth)
                    {
                        maxContentWidth = w;
                    }
                }
            }
            
            width = Math.max(width, maxContentWidth);
            // Asegurar un ancho mínimo razonable
            width = Math.max(width, 100);
            
            columnModel.getColumn(i).setPreferredWidth(width);
            
            String colNameLower = colName.toLowerCase();
            if (colNameLower.contains("date") || colNameLower.contains("time") || colNameLower.contains("tiempo") || colNameLower.contains("fecha"))
            {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            } else
            {
                columnModel.getColumn(i).setCellRenderer(rightRenderer);
            }
        }
    }
    
    private void updateStatus()
    {
        int rows = tableModel.getRowCount();
        String status = String.format("SN: %s | Filas: %,d",
                productSN.isEmpty() ? "N/A" : productSN, rows);
        lblStatus.setText(status);
        updateActionState();
    }
    
    private void updateActionState()
    {
        boolean dataPresent = hasData();
        LOG.info("Actualizando estado de acciones de análisis. Datos presentes: " + dataPresent);
        SwingUtilities.invokeLater(() ->
        {
            SystemAction.get(dataCorrientes.class).setEnabled(dataPresent);
            SystemAction.get(verTensiones.class).setEnabled(dataPresent);
            SystemAction.get(verPotAparente.class).setEnabled(dataPresent);
            SystemAction.get(verPotActiva.class).setEnabled(dataPresent);
            SystemAction.get(verPotReactiva.class).setEnabled(dataPresent);
            SystemAction.get(verFP.class).setEnabled(dataPresent);
        });
    }

    /**
     * averigua si una columna ya existe para no duplicarla en la carga de un
     * archivo ya procesado
     *
     * @param headers
     * @param columnName
     * @return true: columna si existe - fasle: columna no existe
     */
    private boolean columnaEXiste(String[] headers, String columnName)
    {
        for (String name : headers)
        {
            if (name.equals(columnName))
            {
                return true;
            }
        }
        return false;
    }
    
    private String[] augmentData(String[] headers, List<String[]> data)
    {
        // Verificar si ya está aumentado (buscamos específicamente la última añadida: PF Average)
        for (String h : headers)
        {
            if (h.contains("PF Average"))
            {
                return headers;
            }
        }

        // --- DETECCIÓN DE COLUMNAS DE POTENCIA ---
        int idxSA = -1, idxSB = -1, idxSC = -1, idxSSum = -1;
        int idxPA = -1, idxPB = -1, idxPC = -1, idxPSum = -1;
        int idxQA = -1, idxQB = -1, idxQC = -1;
        int idxQSumOrig = -1;

        // --- DETECCIÓN DE COLUMNAS DE TENSIÓN ---
        int idxUA = -1, idxUB = -1, idxUC = -1;

        // --- DETECCIÓN DE COLUMNAS DE CORRIENTE ---
        int idxIA = -1, idxIB = -1, idxIC = -1;

        // --- DETECCIÓN DE COLUMNAS DE FACTOR DE POTENCIA ---
        int idxFPA = -1, idxFPB = -1, idxFPC = -1;
        
        for (int i = 0; i < headers.length; i++)
        {
            String h = headers[i].toUpperCase();

            // Potencia Aparente (S)
            if (matchesStrict(h, "SA", "APPARENT") || matchesStrict(h, "S L1", "APPARENT"))
            {
                idxSA = i;
            } else if (matchesStrict(h, "SB", "APPARENT") || matchesStrict(h, "S L2", "APPARENT"))
            {
                idxSB = i;
            } else if (matchesStrict(h, "SC", "APPARENT") || matchesStrict(h, "S L3", "APPARENT"))
            {
                idxSC = i;
            } else if (h.contains("APPARENT") && h.contains("(VA)") && h.contains("SSUM"))
            {
                idxSSum = i;
            }

            // Potencia Activa (P)
            if (matchesStrict(h, "PA", "ACTIVE") || matchesStrict(h, "P L1", "ACTIVE"))
            {
                idxPA = i;
            } else if (matchesStrict(h, "PB", "ACTIVE") || matchesStrict(h, "P L2", "ACTIVE"))
            {
                idxPB = i;
            } else if (matchesStrict(h, "PC", "ACTIVE") || matchesStrict(h, "P L3", "ACTIVE"))
            {
                idxPC = i;
            } else if (h.contains("ACTIVE") && h.contains("PSUM") && h.contains("(W)"))
            {
                idxPSum = i;
            }

            // Potencia Reactiva (Q)
            if (matchesStrict(h, "QA", "REACTIVE") || matchesStrict(h, "Q L1", "REACTIVE"))
            {
                idxQA = i;
            } else if (matchesStrict(h, "QB", "REACTIVE") || matchesStrict(h, "Q L2", "REACTIVE"))
            {
                idxQB = i;
            } else if (matchesStrict(h, "QC", "REACTIVE") || matchesStrict(h, "Q L3", "REACTIVE"))
            {
                idxQC = i;
            }

            // Buscar QSum original
            if (h.contains("REACTIVE") && (h.contains("QSUM") || h.contains("Q SUM") || h.contains("Q TOTAL")) && !h.contains("ENERGY") && !h.contains("VARH"))
            {
                idxQSumOrig = i;
            }

            // Tensiones de Fase (U)
            if (matchesStrict(h, "UA", "VOLTAGE") || matchesStrict(h, "U L1", "VOLTAGE"))
            {
                idxUA = i;
            } else if (matchesStrict(h, "UB", "VOLTAGE") || matchesStrict(h, "U L2", "VOLTAGE"))
            {
                idxUB = i;
            } else if (matchesStrict(h, "UC", "VOLTAGE") || matchesStrict(h, "U L3", "VOLTAGE"))
            {
                idxUC = i;
            }

            // Corrientes (I)
            if (matchesStrict(h, "IA", "CURRENT") || matchesStrict(h, "I L1", "CURRENT"))
            {
                idxIA = i;
            } else if (matchesStrict(h, "IB", "CURRENT") || matchesStrict(h, "I L2", "CURRENT"))
            {
                idxIB = i;
            } else if (matchesStrict(h, "IC", "CURRENT") || matchesStrict(h, "I L3", "CURRENT"))
            {
                idxIC = i;
            }

            // Factor de Potencia (FP) - Detección Flexible
            if (matchesStrict(h, "PFA", "POWER FACTOR"))
            {
                idxFPA = i;
            }
            if (matchesStrict(h, "PFB", "POWER FACTOR"))
            {
                idxFPB = i;
            }
            if (matchesStrict(h, "PFC", "POWER FACTOR"))
            {
                idxFPC = i;
            }
        }
        
        LOG.info(String.format("AugmentData: S(%d,%d,%d,%d) P(%d,%d,%d,%d) Q(%d,%d,%d) U(%d,%d,%d) I(%d,%d,%d) FP(%d,%d,%d)",
                idxSA, idxSB, idxSC, idxSSum, idxPA, idxPB, idxPC, idxPSum, idxQA, idxQB, idxQC, idxUA, idxUB, idxUC, idxIA, idxIB, idxIC, idxFPA, idxFPB, idxFPC));

        // Actualizar numFases basado en lo detectado (prioridad a reactiva o tensión)
        int detectedFases = 0;
        if (idxQA >= 0 || idxUA >= 0 || idxIA >= 0)
        {
            detectedFases = 1;
        }
        if (idxQB >= 0 || idxUB >= 0 || idxIB >= 0)
        {
            detectedFases = 2;
        }
        if (idxQC >= 0 || idxUC >= 0 || idxIC >= 0)
        {
            detectedFases = 3;
        }
        if (detectedFases > 0)
        {
            glb.numFases = detectedFases;
        }
        
        int numFases = glb.numFases;

        // --- CONSTRUCCIÓN DE NUEVOS ENCABEZADOS ---
        List<String> newHeadersList = new ArrayList<>();

        // Mapeo para Potencia Reactiva
        List<Integer> qCalcPositions = new ArrayList<>();
        int qSumPos = -1;

        // Mapeo para Tensiones FF
        List<Integer> uCalcPositions = new ArrayList<>(); // UAB, UBC, UAC
        int lastVoltageIdx = Math.max(idxUA, Math.max(idxUB, idxUC));

        // Mapeo para Neutro
        int inPos = -1;
        boolean allowIn = glb.tipoRed == ETipoRED.monoFase_FFN
                || glb.tipoRed == ETipoRED.tresFases_FFN
                || glb.tipoRed == ETipoRED.tresFases_FFFN;
        int lastCurrentIdx = Math.max(idxIA, Math.max(idxIB, idxIC));

        // Mapeo para PF Average
        int fpPromPos = -1;
        int lastFPIdx = Math.max(idxFPA, Math.max(idxFPB, idxFPC));

        //solo vuiendo la QA sabemos el resto
        boolean QCalcExist = columnaEXiste(headers, "ReactivePowerCalc(Var) QA");
        //con esta ansion FASE FASE sabemos lo demas
        boolean UABExist = columnaEXiste(headers, "Voltage(V) UAB");
        //averiguamos si la corriente de neutro existe
        boolean INExist = columnaEXiste(headers, "Current(A): In");
        //averigua si el power factor existe en promedio
        boolean PFAvrgExist = columnaEXiste(headers, "Power Factor: PF Average");
        
        for (int i = 0; i < headers.length; i++)
        {
            newHeadersList.add(headers[i]);

            // Inserción de Reactiva Calculada (al lado de cada original)
            //pero se debe preguntar primero si lo trae pues si no lo duplica
            //al salvar el VDM este ya lleva estas columnas // solo viendo la A se sabe si ya esta esto calculado
            if (!QCalcExist)
            {
                if (i == idxQA && numFases >= 1)
                {
                    qCalcPositions.add(newHeadersList.size());
                    newHeadersList.add("ReactivePowerCalc(Var) QA");
                }
                
                if (i == idxQB && numFases >= 2)
                {
                    qCalcPositions.add(newHeadersList.size());
                    newHeadersList.add("ReactivePowerCalc(Var) QB");
                }
                
                if (i == idxQC && numFases >= 3)
                {
                    qCalcPositions.add(newHeadersList.size());
                    newHeadersList.add("ReactivePowerCalc(Var) QC");
                }
                
                if (i == idxQSumOrig)
                {
                    qSumPos = newHeadersList.size();
                    newHeadersList.add("ReactivePowerCalc(Var) QSum");
                }
            }

            // Inserción de Tensiones FF (después del grupo UA, UB, UC)
            //se debe preguntar si las columnas no existen pues el VDM salvado
            //puede  que las traiga ya creadas
            if (!UABExist)
            {
                if (!headers[i].equals("Voltage(V) UAB"))
                {
                    if (i == lastVoltageIdx && numFases >= 2)
                    {
                        uCalcPositions.add(newHeadersList.size());
                        newHeadersList.add("Voltage(V) UAB");
                        if (numFases >= 3)
                        {
                            uCalcPositions.add(newHeadersList.size());
                            newHeadersList.add("Voltage(V) UBC");
                            uCalcPositions.add(newHeadersList.size());
                            newHeadersList.add("Voltage(V) UAC");
                        }
                    }
                }
            }
            
            if (!INExist)
            {
                // Inserción de Neutro (después del grupo IA, IB, IC)
                //se pregunta si no existe antes de proceder con esta compracion
                if (!headers[i].equals("Current(A): In"))
                {
                    if (i == lastCurrentIdx && allowIn && idxIA >= 0)
                    {
                        inPos = newHeadersList.size();
                        newHeadersList.add("Current(A): In");
                    }
                }
            }
            
            if (!PFAvrgExist)
            {
                // Inserción de PF Average (después del grupo FPA, FPB, FPC)
                //si el vdm se habia salvado con estas se va a duplicar
                if (!headers[i].equals("Power Factor: PF Average"))
                {
                    if (i == lastFPIdx && lastFPIdx != -1)
                    {
                        fpPromPos = newHeadersList.size();
                        newHeadersList.add("Power Factor: PF Average");
                    }
                }
            }
            
        }

//        // Fallback QSum
//        if (qSumPos == -1 && idxQA >= 0)
//        {
//            qSumPos = newHeadersList.size();
//            newHeadersList.add("ReactivePowerCalc(Var) QSum");
//        }
// Fallback PF Average: Si no se insertó y tenemos PSum/SSum, añadir al final
        if (fpPromPos == -1 && idxPSum != -1 && idxSSum != -1)
        {
            fpPromPos = newHeadersList.size();
            newHeadersList.add("Power Factor: PF Average");
        }

        // --- PROCESAMIENTO DE DATOS ---
        for (int r = 0; r < data.size(); r++)
        {
            String[] oldRow = data.get(r);
            String[] newRow = new String[newHeadersList.size()];
            int oldPtr = 0;
            double qSum = 0;

            // Pre-calcular valores de tensión para esta fila
            double ua = (idxUA >= 0 && idxUA < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxUA]) : 0;
            double ub = (idxUB >= 0 && idxUB < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxUB]) : 0;
            double uc = (idxUC >= 0 && idxUC < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxUC]) : 0;
            glb.calcularVFase(ua, ub, uc);

            // Pre-calcular Neutro si aplica
            if (inPos != -1)
            {
                double ia = (idxIA >= 0 && idxIA < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxIA]) : 0;
                double ib = (idxIB >= 0 && idxIB < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxIB]) : 0;
                double ic = (idxIC >= 0 && idxIC < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxIC]) : 0;
                double fpa = (idxFPA >= 0 && idxFPA < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxFPA]) : 0;
                double fpb = (idxFPB >= 0 && idxFPB < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxFPB]) : 0;
                double fpc = (idxFPC >= 0 && idxFPC < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxFPC]) : 0;
                double qa = (idxQA >= 0 && idxQA < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxQA]) : 0;
                double qb = (idxQB >= 0 && idxQB < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxQB]) : 0;
                double qc = (idxQC >= 0 && idxQC < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxQC]) : 0;
                
                glb.calculoNeutro(ia, ib, ic, fpa, fpb, fpc, qa, qb, qc);
            }
            
            for (int c = 0; c < newRow.length; c++)
            {
                // ¿Es una columna de Reactiva Calculada?
                int qIdx = qCalcPositions.indexOf(c);
                if (qIdx != -1)
                {
                    int[][] qSources =
                    {
                        {
                            idxSA, idxPA, idxQA
                        },
                        {
                            idxSB, idxPB, idxQB
                        },
                        {
                            idxSC, idxPC, idxQC
                        }
                    };
                    double val = calculateQValue(oldRow, qSources[qIdx][0], qSources[qIdx][1], qSources[qIdx][2]);
                    newRow[c] = String.valueOf(val);
                    qSum += val;
                    continue;
                }

                // ¿Es QSum?
                if (c == qSumPos)
                {
                    newRow[c] = String.valueOf(qSum);
                    continue;
                }

                // ¿Es una columna de Tensión FF?
                int uIdx = uCalcPositions.indexOf(c);
                if (uIdx != -1)
                {
                    if (uIdx == 0)
                    {
                        newRow[c] = String.valueOf(glb.UAB);
                    } else if (uIdx == 1)
                    {
                        newRow[c] = String.valueOf(glb.UBC);
                    } else if (uIdx == 2)
                    {
                        newRow[c] = String.valueOf(glb.UAC);
                    }
                    continue;
                }

                // ¿Es Neutro?
                if (c == inPos)
                {
                    newRow[c] = String.valueOf(glb.IN);
                    continue;
                }

                // ¿Es PF Average?
                if (c == fpPromPos)
                {
                    double pSum = (idxPSum >= 0 && idxPSum < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxPSum]) : 0;
                    double sSum = (idxSSum >= 0 && idxSSum < oldRow.length) ? glb.parseDoubleSafe(oldRow[idxSSum]) : 0;
                    double qSumVal = (idxQSumOrig >= 0) ? glb.parseDoubleSafe(oldRow[idxQSumOrig]) : qSum;
                    double pf = (sSum != 0) ? (pSum / sSum) : 0;
                    if (qSumVal < 0)
                    {
                        pf = -pf; // Aplicar signo basado en QSum (IND/CAP)
                    }
                    newRow[c] = String.valueOf(pf);
                    continue;
                }

                // Columna original
                if (oldPtr < oldRow.length)
                {
                    newRow[c] = oldRow[oldPtr++];
                }
            }
            data.set(r, newRow);
        }
        
        return newHeadersList.toArray(new String[0]);
    }
    
    private boolean matchesStrict(String header, String key, String type)
    {
        String h = header.toUpperCase().trim();
        String k = key.toUpperCase();

        // Debe contener el tipo (APPARENT, ACTIVE, REACTIVE, VOLTAGE, CURRENT, POWER FACTOR)
        if (!h.contains(type.toUpperCase()))
        {
            return false;
        }

        // NO debe ser energía ni otras unidades acumuladas
        if (h.contains("ENERGY") || h.contains("WH") || h.contains("VARH") || h.contains("AH") || h.contains("CALC"))
        {
            return false;
        }

        // Validación estricta de la clave (UA, QA, IA, etc)
        int idx = h.indexOf(k);
        while (idx != -1)
        {
            boolean startOk = (idx == 0) || !Character.isLetter(h.charAt(idx - 1));
            boolean endOk = (idx + k.length() == h.length()) || !Character.isLetter(h.charAt(idx + k.length()));
            
            if (startOk && endOk)
            {
                // Verificar prefijo 'E' específicamente para QA/QB/QC
                if (idx > 0 && h.charAt(idx - 1) == 'E' && (k.startsWith("Q") || k.startsWith("P") || k.startsWith("S")))
                {
                    // saltar
                } else
                {
                    return true;
                }
            }
            idx = h.indexOf(k, idx + 1);
        }
        
        return false;
    }
    
    private double calculateQValue(String[] row, int idxS, int idxP, int idxQ)
    {
        if (idxS < 0 || idxP < 0 || idxQ < 0 || idxS >= row.length || idxP >= row.length || idxQ >= row.length)
        {
            return 0;
        }
        try
        {
            double s = glb.parseDoubleSafe(row[idxS]);
            double p = glb.parseDoubleSafe(row[idxP]);
            double q = glb.parseDoubleSafe(row[idxQ]);
            return glb.CalcReactivaFase(s, p, q);
        } catch (Exception e)
        {
            return 0;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearZeros;
    private javax.swing.JButton btnDeleteRow;
    private javax.swing.JButton btnFitColumns;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnSave;
    private javax.swing.JTable dataTable;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened()
    {
        updateActionState();
    }
    
    @Override
    public void componentClosed()
    {
    }
    
    void writeProperties(java.util.Properties p)
    {
        p.setProperty("version", "1.0");
    }
    
    void readProperties(java.util.Properties p)
    {
        String version = p.getProperty("version");
    }

    /**
     * carga el proyecto y los datos a datos del proyecto para ver en el informe
     * estos datos
     */
    private void cargarProyectoVDM()
    {
        
    }
    
    private static class DataTableModel extends AbstractTableModel
    {
        
        private String[] columnNames = new String[0];
        private List<String[]> data = new ArrayList<>();
        
        public void setColumns(String[] columns)
        {
            this.columnNames = columns != null ? columns : new String[0];
            fireTableStructureChanged();
        }
        
        public void setData(List<String[]> data)
        {
            this.data = data != null ? data : new ArrayList<>();
            fireTableDataChanged();
        }
        
        public String[] getColumnNames()
        {
            return columnNames;
        }
        
        public List<String[]> getDataList()
        {
            return data;
        }
        
        public void removeRows(int[] modelIndices)
        {
            if (modelIndices.length == 0)
            {
                return;
            }
            
            List<String[]> newData = new ArrayList<>(data.size() - modelIndices.length);
            int indexPtr = 0;
            for (int i = 0; i < data.size(); i++)
            {
                if (indexPtr < modelIndices.length && modelIndices[indexPtr] == i)
                {
                    indexPtr++;
                } else
                {
                    newData.add(data.get(i));
                }
            }
            this.data = newData;
            fireTableDataChanged();
        }
        
        public void clearZeroRows()
        {
            List<String[]> newData = new ArrayList<>(data.size());
            for (String[] row : data)
            {
                boolean allZeros = true;
                for (int i = 0; i < row.length; i++)
                {
                    String colName = getColumnName(i).toLowerCase();
                    // Ignorar columnas de fecha/tiempo
                    if (colName.contains("date") || colName.contains("time")
                            || colName.contains("fecha") || colName.contains("tiempo")
                            || colName.contains("day") || colName.contains("hour")
                            || colName.contains("year") || colName.contains("month"))
                    {
                        continue;
                    }
                    
                    String val = row[i].trim();
                    // Verificar si es un valor numérico que representa cero
                    try
                    {
                        double d = glb.parseDoubleSafe(val);
                        if (d != 0.0)
                        {
                            allZeros = false;
                            break;
                        }
                    } catch (Exception e)
                    {
                        // Si no es número, no lo consideramos cero
                        if (!val.isEmpty())
                        {
                            allZeros = false;
                            break;
                        }
                    }
                }
                if (!allZeros)
                {
                    newData.add(row);
                }
            }
            this.data = newData;
            fireTableDataChanged();
        }
        
        @Override
        public int getRowCount()
        {
            return data.size();
        }
        
        @Override
        public int getColumnCount()
        {
            return columnNames.length;
        }
        
        @Override
        public String getColumnName(int column)
        {
            return columnNames[column];
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            String[] row = data.get(rowIndex);
            if (columnIndex < row.length)
            {
                return row[columnIndex];
            }
            return "";
        }
    }
}

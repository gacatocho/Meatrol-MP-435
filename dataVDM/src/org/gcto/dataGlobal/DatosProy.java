/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.persistence.Entity;

/**
 * clase que almacena los datos interesantes del proyecto para su correcta
 * información y para el informe final que se saca del los análisis
 *
 * @author camilo
 */
@Entity

public class DatosProy
{

    private String nombreProy = "nombre del proyecto";

    public static final String PROP_NOMBREPROY = "nombreProy";

    /**
     * Get the value of nombreProy
     *
     * @return the value of nombreProy
     */
    public String getNombreProy()
    {
        return nombreProy;
    }

    /**
     * Set the value of nombreProy
     *
     * @param nombreProy new value of nombreProy
     */
    public void setNombreProy(String nombreProy)
    {
        String oldNombreProy = this.nombreProy;
        this.nombreProy = nombreProy;
        propertyChangeSupport.firePropertyChange(PROP_NOMBREPROY, oldNombreProy, nombreProy);
    }

    private String ubicacionProy = "ubicación del proyecto";

    public static final String PROP_UBICACIONPROY = "ubicacionProy";

    /**
     * Get the value of ubicacionProy
     *
     * @return the value of ubicacionProy
     */
    public String getUbicacionProy()
    {
        return ubicacionProy;
    }

    /**
     * Set the value of ubicacionProy
     *
     * @param ubicacionProy new value of ubicacionProy
     */
    public void setUbicacionProy(String ubicacionProy)
    {
        String oldUbicacionProy = this.ubicacionProy;
        this.ubicacionProy = ubicacionProy;
        propertyChangeSupport.firePropertyChange(PROP_UBICACIONPROY, oldUbicacionProy, ubicacionProy);
    }

    private String operadorME = "operador  técnico";

    public static final String PROP_OPERADORME = "operadorME";

    /**
     * Get the value of operadorME / quien toma la lectura
     *
     * @return the value of operadorME
     */
    public String getOperadorME()
    {
        return operadorME;
    }

    /**
     * Set the value of operadorME / quien toma la lectura
     *
     * @param operadorME new value of operadorME
     */
    public void setOperadorME(String operadorME)
    {
        String oldOperadorME = this.operadorME;
        this.operadorME = operadorME;
        propertyChangeSupport.firePropertyChange(PROP_OPERADORME, oldOperadorME, operadorME);
    }

    private String ciudadProy = "ciudad donde se toma la muestra";

    public static final String PROP_CIUDADPROY = "ciudadProy";

    /**
     * Get the value of ciudadProy
     *
     * @return the value of ciudadProy
     */
    public String getCiudadProy()
    {
        return ciudadProy;
    }

    /**
     * Set the value of ciudadProy
     *
     * @param ciudadProy new value of ciudadProy
     */
    public void setCiudadProy(String ciudadProy)
    {
        String oldCiudadProy = this.ciudadProy;
        this.ciudadProy = ciudadProy;
        propertyChangeSupport.firePropertyChange(PROP_CIUDADPROY, oldCiudadProy, ciudadProy);
    }

    private String tablero = "tablero donde se conecta el equipo";

    public static final String PROP_TABLERO = "tablero";

    /**
     * Get the value of tablero
     *
     * @return the value of tablero
     */
    public String getTablero()
    {
        return tablero;
    }

    /**
     * Set the value of tablero
     *
     * @param tablero new value of tablero
     */
    public void setTablero(String tablero)
    {
        String oldTablero = this.tablero;
        this.tablero = tablero;
        propertyChangeSupport.firePropertyChange(PROP_TABLERO, oldTablero, tablero);
    }

    private int relacionBobina = 100;

    public static final String PROP_RELACIONBOBINA = "relacionBobina";

    /**
     * Get the value of relacionBobina
     *
     * @return the value of relacionBobina
     */
    public int getRelacionBobina()
    {
        return relacionBobina;
    }

    /**
     * Set the value of relacionBobina
     *
     * @param relacionBobina new value of relacionBobina
     */
    public void setRelacionBobina(int relacionBobina)
    {
        int oldRelacionBobina = this.relacionBobina;
        this.relacionBobina = relacionBobina;
        propertyChangeSupport.firePropertyChange(PROP_RELACIONBOBINA, oldRelacionBobina, relacionBobina);
    }

    private int relacionTensionPrim = 1;

    public static final String PROP_RELACIONTENSIONPRIM = "relacionTensionPrim";

    /**
     * Get the value of relacionTensionPrim
     *
     * @return the value of relacionTensionPrim
     */
    public int getRelacionTensionPrim()
    {
        return relacionTensionPrim;
    }

    /**
     * Set the value of relacionTensionPrim
     *
     * @param relacionTensionPrim new value of relacionTensionPrim
     */
    public void setRelacionTensionPrim(int relacionTensionPrim)
    {
        int oldRelacionTensionPrim = this.relacionTensionPrim;
        this.relacionTensionPrim = relacionTensionPrim;
        propertyChangeSupport.firePropertyChange(PROP_RELACIONTENSIONPRIM, oldRelacionTensionPrim, relacionTensionPrim);
    }

    private int relacionTensionSec = 1;

    public static final String PROP_RELACIONTENSIONSEC = "relacionTensionSec";

    /**
     * Get the value of relacionTensionSec
     *
     * @return the value of relacionTensionSec
     */
    public int getRelacionTensionSec()
    {
        return relacionTensionSec;
    }

    /**
     * Set the value of relacionTensionSec
     *
     * @param relacionTensionSec new value of relacionTensionSec
     */
    public void setRelacionTensionSec(int relacionTensionSec)
    {
        int oldRelacionTensionSec = this.relacionTensionSec;
        this.relacionTensionSec = relacionTensionSec;
        propertyChangeSupport.firePropertyChange(PROP_RELACIONTENSIONSEC, oldRelacionTensionSec, relacionTensionSec);
    }

    private int periodoSD = 1;

    public static final String PROP_PERIODOSD = "periodoSD";

    /**
     * Get the value of periodoSD / segundos
     *
     * @return the value of periodoSD
     */
    public int getPeriodoSD()
    {
        return periodoSD;
    }

    /**
     * Set the value of periodoSD / segundos
     *
     * @param periodoSD new value of periodoSD
     */
    public void setPeriodoSD(int periodoSD)
    {
        int oldPeriodoSD = this.periodoSD;
        this.periodoSD = periodoSD;
        propertyChangeSupport.firePropertyChange(PROP_PERIODOSD, oldPeriodoSD, periodoSD);
    }

    private ETipoRED tipoRed = ETipoRED.tresFases_FFFN;

    public static final String PROP_TIPORED = "tipoRed";

    /**
     * Get the value of tipoRed
     *
     * @return the value of tipoRed
     */
    public ETipoRED getTipoRed()
    {
        return tipoRed;
    }

    /**
     * Set the value of tipoRed
     *
     * @param tipoRed new value of tipoRed
     */
    public void setTipoRed(ETipoRED tipoRed)
    {
        ETipoRED oldTipoRed = this.tipoRed;
        this.tipoRed = tipoRed;
        propertyChangeSupport.firePropertyChange(PROP_TIPORED, oldTipoRed, tipoRed);
    }

    private String csvName = "csvName";

    public static final String PROP_CSVNAME = "csvName";

    /**
     * Get the value of csvName
     *
     * @return the value of csvName
     */
    public String getCsvName()
    {
        return csvName;
    }

    /**
     * Set the value of csvName
     *
     * @param csvName new value of csvName
     */
    public void setCsvName(String csvName)
    {
        String oldCsvName = this.csvName;
        this.csvName = csvName;
        propertyChangeSupport.firePropertyChange(PROP_CSVNAME, oldCsvName, csvName);
    }

    
    private String[] listaRecientes =
    {
        "", "", "", "", "", "", "", "", "", ""
    };

    public static final String PROP_LISTARECIENTES = "listaRecientes";

    /**
     * Get the value of listaRecientes / hasta 10 datos en el arreglo
     *
     * @return the value of listaRecientes
     */
    public String[] getListaRecientes()
    {
        return listaRecientes;
    }

    /**
     * Set the value of listaRecientes
     *
     * @param listaRecientes new value of listaRecientes
     */
    public void setListaRecientes(String[] listaRecientes)
    {
        String[] oldListaRecientes = this.listaRecientes;
        this.listaRecientes = listaRecientes;
        propertyChangeSupport.firePropertyChange(PROP_LISTARECIENTES, oldListaRecientes, listaRecientes);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}

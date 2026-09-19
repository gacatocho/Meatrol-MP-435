/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.gcto.interfases;

import org.gcto.dataGlobal.ETipoRED;

/**
 *
 * @author camilo
 */
public interface IDatosProy extends IBaseVDM
{

    String PROP_CIUDADPROY = "ciudadProy";
    String PROP_CSVNAME = "csvName";
    String PROP_LISTARECIENTES = "listaRecientes";
    String PROP_NOMBREPROY = "nombreProy";
    String PROP_OPERADORME = "operadorME";
    String PROP_PERIODOSD = "periodoSD";
    String PROP_RELACIONBOBINA = "relacionBobina";
    String PROP_RELACIONTENSIONPRIM = "relacionTensionPrim";
    String PROP_RELACIONTENSIONSEC = "relacionTensionSec";
    String PROP_TABLERO = "tablero";
    String PROP_TIPORED = "tipoRed";
    String PROP_UBICACIONPROY = "ubicacionProy";
    String PROP_RUTAPROYECTO = "rutaProyecto";

    /**
     * Get the value of ciudadProy
     *
     * @return the value of ciudadProy
     */
    String getCiudadProy();

    /**
     * Get the value of csvName
     *
     * @return the value of csvName
     */
    String getCsvName();

    /**
     * Get the value of listaRecientes / hasta 10 datos en el arreglo
     *
     * @return the value of listaRecientes
     */
    String[] getListaRecientes();

    /**
     * Get the value of nombreProy
     *
     * @return the value of nombreProy
     */
    String getNombreProy();

    /**
     * Get the value of operadorME / quien toma la lectura
     *
     * @return the value of operadorME
     */
    String getOperadorME();

    /**
     * Get the value of periodoSD / segundos
     *
     * @return the value of periodoSD
     */
    int getPeriodoSD();

    /**
     * Get the value of relacionBobina
     *
     * @return the value of relacionBobina
     */
    int getRelacionBobina();

    /**
     * Get the value of relacionTensionPrim
     *
     * @return the value of relacionTensionPrim
     */
    int getRelacionTensionPrim();

    /**
     * Get the value of relacionTensionSec
     *
     * @return the value of relacionTensionSec
     */
    int getRelacionTensionSec();

    /**
     * Get the value of tablero
     *
     * @return the value of tablero
     */
    String getTablero();

    /**
     * Get the value of tipoRed
     *
     * @return the value of tipoRed
     */
    ETipoRED getTipoRed();

    /**
     * Get the value of ubicacionProy
     *
     * @return the value of ubicacionProy
     */
    String getUbicacionProy();

    /**
     * Set the value of ciudadProy
     *
     * @param ciudadProy new value of ciudadProy
     */
    void setCiudadProy(String ciudadProy);

    /**
     * Set the value of csvName
     *
     * @param csvName new value of csvName
     */
    void setCsvName(String csvName);

    /**
     * Set the value of listaRecientes
     *
     * @param listaRecientes new value of listaRecientes
     */
    void setListaRecientes(String[] listaRecientes);

    /**
     * Set the value of nombreProy
     *
     * @param nombreProy new value of nombreProy
     */
    void setNombreProy(String nombreProy);

    /**
     * Set the value of operadorME / quien toma la lectura
     *
     * @param operadorME new value of operadorME
     */
    void setOperadorME(String operadorME);

    /**
     * Set the value of periodoSD / segundos
     *
     * @param periodoSD new value of periodoSD
     */
    void setPeriodoSD(int periodoSD);

    /**
     * Set the value of relacionBobina
     *
     * @param relacionBobina new value of relacionBobina
     */
    void setRelacionBobina(int relacionBobina);

    /**
     * Set the value of relacionTensionPrim
     *
     * @param relacionTensionPrim new value of relacionTensionPrim
     */
    void setRelacionTensionPrim(int relacionTensionPrim);

    /**
     * Set the value of relacionTensionSec
     *
     * @param relacionTensionSec new value of relacionTensionSec
     */
    void setRelacionTensionSec(int relacionTensionSec);

    /**
     * Set the value of tablero
     *
     * @param tablero new value of tablero
     */
    void setTablero(String tablero);

    /**
     * Set the value of tipoRed
     *
     * @param tipoRed new value of tipoRed
     */
    void setTipoRed(ETipoRED tipoRed);

    /**
     * Set the value of ubicacionProy
     *
     * @param ubicacionProy new value of ubicacionProy
     */
    void setUbicacionProy(String ubicacionProy);
    
     

    /**
     * Get the value of rutaProyecto
     *
     * @return the value of rutaProyecto
     */
    public String getRutaProyecto();
    
        /**
     * Set the value of rutaProyecto
     *
     * @param rutaProyecto new value of rutaProyecto
     */
    public void setRutaProyecto(String rutaProyecto);
    
}

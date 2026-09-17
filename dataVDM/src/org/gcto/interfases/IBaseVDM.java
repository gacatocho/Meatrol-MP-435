/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.gcto.interfases;

import org.gcto.dataGlobal.EEstado;

/**
 *
 * @author camilo
 */
public interface IBaseVDM
{

    //<editor-fold defaultstate="collapsed" desc="Copia de la clase">
    <T2> void copy(T2 destino);
    //</editor-fold>

    /**
     * Get the value of Estado
     *
     * @return the value of Estado
     */
    EEstado getEstado();

    /**
     * Set the value of Estado
     *
     * @param Estado new value of Estado
     */
    void setEstado(EEstado Estado);
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.gcto.interfases;

import java.awt.Color;
import org.gcto.interfases.IBaseVDM;

/**
 *
 * @author camilo
 */
public interface IOpciones extends IBaseVDM
{

    String PROP_COLORFASEA = "colorFaseA";
    String PROP_COLORFASEB = "colorFaseB";
    String PROP_COLORFASEC = "colorFaseC";
    String PROP_COLORNEUTRO = "colorNeutro";
    String PROP_LOGOPATH = "logoPath";

    /**
     * Get the value of colorFaseA
     *
     * @return the value of colorFaseA
     */
    String getColorFaseA();

    /**
     * Get the value of colorFaseB
     *
     * @return the value of colorFaseB
     */
    String getColorFaseB();

    /**
     * Get the value of colorFaseC
     *
     * @return the value of colorFaseC
     */
    String getColorFaseC();

    /**
     * Get the value of colorNeutro
     *
     * @return the value of colorNeutro
     */
    String getColorNeutro();

    /**
     * Set the value of colorFaseA
     *
     * @param colorFaseA new value of colorFaseA
     */
    void setColorFaseA(String colorFaseA);

    /**
     * Set the value of colorFaseB
     *
     * @param colorFaseB new value of colorFaseB
     */
    void setColorFaseB(String colorFaseB);

    /**
     * Set the value of colorFaseC
     *
     * @param colorFaseC new value of colorFaseC
     */
    void setColorFaseC(String colorFaseC);

    /**
     * Set the value of colorNeutro
     *
     * @param colorNeutro new value of colorNeutro
     */
    void setColorNeutro(String colorNeutro);
    
    String getLogoPath();
    
    void setLogoPath(String logoPath);
    
}

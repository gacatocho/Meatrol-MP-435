/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.gcto.interfases;

/**
 * interfase para la clase que manejas lsa diferentes opciones del programa
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
    String PROP_LISTARECIENTES = "listaRecientes";
    String PROP_COLORALTA = "colorAltA";
    String PROP_COLORALTB = "colorAltB";
    String PROP_COLORALTC = "colorAltC";
    String PROP_COLORALTN = "colorAltN";
    String PROP_COLORTOTP_ACT = "colorTotP_Act";
    String PROP_COLORTOTQ_REACT = "colorTotQ_react";
    String PROP_COLORTOTS_APAR = "colorTotS_apar";
    String PROP_COLOR_I_AVG = "color_I_AVG";
    String PROP_COLOR_V_AVG = "color_V_AVG";
    String PROP_COLOR_F_AVG = "color_F_AVG";
    String PROP_COLOR_IDM_AVG = "color_IDM_AVG";

    /**
     * Get the value of listaRecientes / hasta 10 datos en el arreglo
     *
     * @return the value of listaRecientes
     */
    String[] getListaRecientes();

    /**
     * Set the value of listaRecientes
     *
     * @param listaRecientes new value of listaRecientes
     */
    void setListaRecientes(String[] listaRecientes);

    
    /**
     * Get the value of colorFaseA
     *
     * @return the value of colorFaseA
     */
    String getColorFaseA();

    /**
     * Set the value of colorFaseA
     *
     * @param colorFaseA new value of colorFaseA
     */
    void setColorFaseA(String colorFaseA);

    /**
     * Get the value of colorFaseB
     *
     * @return the value of colorFaseB
     */
    String getColorFaseB();

    /**
     * Set the value of colorFaseB
     *
     * @param colorFaseB new value of colorFaseB
     */
    void setColorFaseB(String colorFaseB);

    /**
     * Get the value of colorFaseC
     *
     * @return the value of colorFaseC
     */
    String getColorFaseC();

    /**
     * Set the value of colorFaseC
     *
     * @param colorFaseC new value of colorFaseC
     */
    void setColorFaseC(String colorFaseC);

    /**
     * Get the value of colorNeutro
     *
     * @return the value of colorNeutro
     */
    String getColorNeutro();

    /**
     * Set the value of colorNeutro
     *
     * @param colorNeutro new value of colorNeutro
     */
    void setColorNeutro(String colorNeutro);

    /**
     * Get the value of colorAltA
     *
     * @return the value of colorAltA
     */
    public String getColorAltA();

    /**
     * Set the value of colorAltA
     *
     * @param colorAltA new value of colorAltA
     */
    public void setColorAltA(String colorAltA);

    /**
     * Get the value of colorAltB
     *
     * @return the value of colorAltB
     */
    public String getColorAltB();

    /**
     * Set the value of colorAltB
     *
     * @param colorAltB new value of colorAltB
     */
    public void setColorAltB(String colorAltB);

    /**
     * Get the value of colorAltC
     *
     * @return the value of colorAltC
     */
    public String getColorAltC();

    /**
     * Set the value of colorAltC
     *
     * @param colorAltC new value of colorAltC
     */
    public void setColorAltC(String colorAltC);

    /**
     * Get the value of colorAltN
     *
     * @return the value of colorAltN
     */
    public String getColorAltN();

    /**
     * Set the value of colorAltN
     *
     * @param colorAltN new value of colorAltN
     */
    public void setColorAltN(String colorAltN);

    /**
     * Get the value of colorTotP_Act / total potencia activa / suma
     *
     * @return the value of colorTotP_Act
     */
    public String getColorTotP_Act();

    /**
     * Set the value of colorTotP_Act / total potencia activa / suma
     *
     * @param colorTotP_Act new value of colorTotP_Act
     */
    public void setColorTotP_Act(String colorTotP_Act);

    /**
     * Get the value of colorTotQ_react / color total de potencia reactiva /
     * suma
     *
     * @return the value of colorTotQ_react
     */
    public String getColorTotQ_react();
       
    /**
     * Set the value of colorTotQ_react / color total de potencia reactiva / suma
     *
     * @param colorTotQ_react new value of colorTotQ_react
     */
    public void setColorTotQ_react(String colorTotQ_react);
    
        /**
     * Get the value of colorTotS_apar / color total potencia aparente / suma
     *
     * @return the value of colorTotS_apar
     */
    public String getColorTotS_apar();
        
    /**
     * Set the value of colorTotS_apar / color total potencia aparente / suma
     *
     * @param colorTotS_apar new value of colorTotS_apar
     */
    public void setColorTotS_apar(String colorTotS_apar);
    
    /**
     * Get the value of color_I_AVG / promedio
     *
     * @return the value of color_I_AVG
     */
    public String getColor_I_AVG();
    
   /*
     * Set the value of color_I_AVG / promedio
     *
     * @param color_I_AVG new value of color_I_AVG
     */
    public void setColor_I_AVG(String color_I_AVG);

    /**
     * Get the value of color_V_AVG / U promedio
     *
     * @return the value of color_V_AVG
     */
    public String getColor_V_AVG();
    
    /**
     * Set the value of color_V_AVG / U promedio
     *
     * @param color_V_AVG new value of color_V_AVG
     */
    public void setColor_V_AVG(String color_V_AVG);
    
    /**
     * Get the value of color_F_AVG /color de promedio de frecuencia
     *
     * @return the value of color_F_AVG
     */
    public String getColor_F_AVG();
    
        /**
     * Set the value of color_F_AVG / color de promedio de frecuencia
     *
     * @param color_F_AVG new value of color_F_AVG
     */
    
    public void setColor_F_AVG(String color_F_AVG);
    
        /**
     * Get the value of color_IDM_AVG / promedio demanda de corriente
     *
     * @return the value of color_IDM_AVG
     */
    public String getColor_IDM_AVG();
    
    /**
     * Set the value of color_IDM_AVG / promedio demanda de corriente
     *
     * @param color_IDM_AVG new value of color_IDM_AVG
     */
    public void setColor_IDM_AVG(String color_IDM_AVG);
    
    /**
     * get ruta donde esta el logo para el informe
     *
     * @return String - ruta
     */
    
    String getLogoPath();

    /**
     * set ruta donde esta el logo para el informe
     *
     * @param logoPath String - ruta
     */
    void setLogoPath(String logoPath);

}

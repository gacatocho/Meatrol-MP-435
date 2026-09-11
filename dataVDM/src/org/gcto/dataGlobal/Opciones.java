/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.persistence.Entity;

/**
 * Clase que gestiona y almacena los datos generales que estan seteados para
 * cualquier proyecto
 *
 * @author camilo
 */
@Entity
public class Opciones
{

    private Color colorFaseA = Color.YELLOW;

    public static final String PROP_COLORFASEA = "colorFaseA";

    /**
     * Get the value of colorFaseA
     *
     * @return the value of colorFaseA
     */
    public Color getColorFaseA()
    {
        return colorFaseA;
    }

    /**
     * Set the value of colorFaseA
     *
     * @param colorFaseA new value of colorFaseA
     */
    public void setColorFaseA(Color colorFaseA)
    {
        Color oldColorFaseA = this.colorFaseA;
        this.colorFaseA = colorFaseA;
        propertyChangeSupport.firePropertyChange(PROP_COLORFASEA, oldColorFaseA, colorFaseA);
    }

    private Color colorFaseB = Color.BLUE;

    public static final String PROP_COLORFASEB = "colorFaseB";

    /**
     * Get the value of colorFaseB
     *
     * @return the value of colorFaseB
     */
    public Color getColorFaseB()
    {
        return colorFaseB;
    }

    /**
     * Set the value of colorFaseB
     *
     * @param colorFaseB new value of colorFaseB
     */
    public void setColorFaseB(Color colorFaseB)
    {
        Color oldColorFaseB = this.colorFaseB;
        this.colorFaseB = colorFaseB;
        propertyChangeSupport.firePropertyChange(PROP_COLORFASEB, oldColorFaseB, colorFaseB);
    }

    private Color colorFaseC = Color.RED;

    public static final String PROP_COLORFASEC = "colorFaseC";

    /**
     * Get the value of colorFaseC
     *
     * @return the value of colorFaseC
     */
    public Color getColorFaseC()
    {
        return colorFaseC;
    }

    /**
     * Set the value of colorFaseC
     *
     * @param colorFaseC new value of colorFaseC
     */
    public void setColorFaseC(Color colorFaseC)
    {
        Color oldColorFaseC = this.colorFaseC;
        this.colorFaseC = colorFaseC;
        propertyChangeSupport.firePropertyChange(PROP_COLORFASEC, oldColorFaseC, colorFaseC);
    }

    private Color colorNeutro = Color.WHITE;

    public static final String PROP_COLORNEUTRO = "colorNeutro";

    /**
     * Get the value of colorNeutro
     *
     * @return the value of colorNeutro
     */
    public Color getColorNeutro()
    {
        return colorNeutro;
    }

    /**
     * Set the value of colorNeutro
     *
     * @param colorNeutro new value of colorNeutro
     */
    public void setColorNeutro(Color colorNeutro)
    {
        Color oldColorNeutro = this.colorNeutro;
        this.colorNeutro = colorNeutro;
        propertyChangeSupport.firePropertyChange(PROP_COLORNEUTRO, oldColorNeutro, colorNeutro);
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

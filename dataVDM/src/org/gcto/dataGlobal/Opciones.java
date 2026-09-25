/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import org.gcto.interfases.IOpciones;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import javax.persistence.Entity;
import org.openide.util.lookup.ServiceProvider;

/**
 * Clase que gestiona y almacena los datos generales que estan seteados para
 * cualquier proyecto
 *
 * @author camilo
 */
@Entity
@ServiceProvider(service = IOpciones.class)
public class Opciones implements IOpciones, PropertyChangeListener
{

    public Opciones()
    {
    }

    private String colorFaseA = "255,255,0";

    /**
     * Get the value of colorFaseA
     *
     * @return the value of colorFaseA
     */
    @Override
    public String getColorFaseA()
    {
        return colorFaseA;
    }

    /**
     * Set the value of colorFaseA
     *
     * @param colorFaseA new value of colorFaseA
     */
    @Override
    public void setColorFaseA(String colorFaseA)
    {
        String oldColorFaseA = this.colorFaseA;
        this.colorFaseA = colorFaseA;
        propertyChangeSupport.firePropertyChange(PROP_COLORFASEA, oldColorFaseA, colorFaseA);
    }

    private String colorFaseB = "0,0,255";

    /**
     * Get the value of colorFaseB
     *
     * @return the value of colorFaseB
     */
    @Override
    public String getColorFaseB()
    {
        return colorFaseB;
    }

    /**
     * Set the value of colorFaseB
     *
     * @param colorFaseB new value of colorFaseB
     */
    @Override
    public void setColorFaseB(String colorFaseB)
    {
        String oldColorFaseB = this.colorFaseB;
        this.colorFaseB = colorFaseB;
        propertyChangeSupport.firePropertyChange(PROP_COLORFASEB, oldColorFaseB, colorFaseB);
    }

    private String colorFaseC = "255,0,0";

    /**
     * Get the value of colorFaseC
     *
     * @return the value of colorFaseC
     */
    @Override
    public String getColorFaseC()
    {
        return colorFaseC;
    }

    /**
     * Set the value of colorFaseC
     *
     * @param colorFaseC new value of colorFaseC
     */
    @Override
    public void setColorFaseC(String colorFaseC)
    {
        String oldColorFaseC = this.colorFaseC;
        this.colorFaseC = colorFaseC;
        propertyChangeSupport.firePropertyChange(PROP_COLORFASEC, oldColorFaseC, colorFaseC);
    }

    private String colorNeutro = "0,0,0";

    /**
     * Get the value of colorNeutro
     *
     * @return the value of colorNeutro
     */
    @Override
    public String getColorNeutro()
    {
        return colorNeutro;
    }

    /**
     * Set the value of colorNeutro
     *
     * @param colorNeutro new value of colorNeutro
     */
    @Override
    public void setColorNeutro(String colorNeutro)
    {
        String oldColorNeutro = this.colorNeutro;
        this.colorNeutro = colorNeutro;
        propertyChangeSupport.firePropertyChange(PROP_COLORNEUTRO, oldColorNeutro, colorNeutro);
    }

    private String logoPath = "";

    @Override
    public String getLogoPath()
    {
        return logoPath;
    }

    @Override
    public void setLogoPath(String logoPath)
    {
        String oldLogoPath = this.logoPath;
        this.logoPath = logoPath;
        propertyChangeSupport.firePropertyChange(PROP_LOGOPATH, oldLogoPath, logoPath);
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

    @Override
    public <T2> void copy(T2 destino)
    {
        Class<? extends Object> copy1 = this.getClass();
        Class<? extends Object> copy2 = destino.getClass();

        Field[] fromFields = copy1.getDeclaredFields();
        //Field[] toFields = copy2.getDeclaredFields();

        Object value = null;

        for (Field field : fromFields)
        {

            try
            {

                Field field1 = copy2.getDeclaredField(field.getName());

                System.out.println(field.getName());
                value = field.get(this);
                field1.set(destino, value);

            } catch (NoSuchFieldException noSuchFieldException)
            {
                System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
                System.out.println("No such file en copiar  " + noSuchFieldException.getMessage());
                System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            } catch (SecurityException securityException)
            {
                System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
                System.out.println("SecurityException  " + securityException.getMessage());
                System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            } catch (IllegalArgumentException illegalArgumentException)
            {
                System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
                System.out.println("IllegalArgumentException  " + illegalArgumentException.getMessage());
                System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            } catch (IllegalAccessException illegalAccessException)
            {
                System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
                System.out.println("IllegalAccessException  " + illegalAccessException.getMessage());
                System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            }
        }
    }

    private EEstado estado = EEstado.NUEVO;

    @Override
    public EEstado getEstado()
    {
        return this.estado;
    }

    @Override
    public void setEstado(EEstado Estado)
    {
        this.estado=Estado;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        setEstado(EEstado.EDITADO);

    }

}

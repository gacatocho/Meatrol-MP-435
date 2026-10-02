/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import org.gcto.dataEnum.EEstado;
import org.gcto.interfases.IBaseVDM;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import javax.persistence.Entity;

/**
 * Base para generar las clases que se salvan en la base de datos o estan
 * presentes en el manejo general del programa
 *
 * @author camilo
 */
@Entity
public class BaseVDM implements PropertyChangeListener, IBaseVDM
{

    private EEstado Estado = EEstado.NUEVO;

    /**
     * Get the value of Estado
     *
     * @return the value of Estado
     */
    @Override
    public EEstado getEstado()
    {
        return Estado;
    }

    /**
     * Set the value of Estado
     *
     * @param Estado new value of Estado
     */
    @Override
    public void setEstado(EEstado Estado)
    {
        this.Estado = Estado;
    }

    //<editor-fold defaultstate="collapsed" desc="Copia de la clase">
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
//</editor-fold>

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        this.setEstado(EEstado.EDITADO);
    }

    public transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

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

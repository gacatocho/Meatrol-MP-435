/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import org.gcto.interfases.IDatosProy;
import javax.persistence.Entity;
import org.openide.util.lookup.ServiceProvider;

/**
 * clase que almacena los datos interesantes del proyecto para su correcta
 * información y para el informe final que se saca del los análisis
 *
 * @author camilo
 */
@Entity
@ServiceProvider(service = IDatosProy.class)
public class DatosProy implements IDatosProy, PropertyChangeListener
{

    public DatosProy()
    {
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private String nombreProy = "nombre del proyecto";

    /**
     * Get the value of nombreProy
     *
     * @return the value of nombreProy
     */
    @Override
    public String getNombreProy()
    {
        return nombreProy;
    }

    /**
     * Set the value of nombreProy
     *
     * @param nombreProy new value of nombreProy
     */
    @Override
    public void setNombreProy(String nombreProy)
    {
        String oldNombreProy = this.nombreProy;
        this.nombreProy = nombreProy;
        propertyChangeSupport.firePropertyChange(PROP_NOMBREPROY, oldNombreProy, nombreProy);
    }

    private String ubicacionProy = "ubicación del proyecto";

    /**
     * Get the value of ubicacionProy
     *
     * @return the value of ubicacionProy
     */
    @Override
    public String getUbicacionProy()
    {
        return ubicacionProy;
    }

    /**
     * Set the value of ubicacionProy
     *
     * @param ubicacionProy new value of ubicacionProy
     */
    @Override
    public void setUbicacionProy(String ubicacionProy)
    {
        String oldUbicacionProy = this.ubicacionProy;
        this.ubicacionProy = ubicacionProy;
        propertyChangeSupport.firePropertyChange(PROP_UBICACIONPROY, oldUbicacionProy, ubicacionProy);
    }

    private String operadorME = "operador  técnico";

    /**
     * Get the value of operadorME / quien toma la lectura
     *
     * @return the value of operadorME
     */
    @Override
    public String getOperadorME()
    {
        return operadorME;
    }

    /**
     * Set the value of operadorME / quien toma la lectura
     *
     * @param operadorME new value of operadorME
     */
    @Override
    public void setOperadorME(String operadorME)
    {
        String oldOperadorME = this.operadorME;
        this.operadorME = operadorME;
        propertyChangeSupport.firePropertyChange(PROP_OPERADORME, oldOperadorME, operadorME);
    }

    private String ciudadProy = "ciudad donde se toma la muestra";

    /**
     * Get the value of ciudadProy
     *
     * @return the value of ciudadProy
     */
    @Override
    public String getCiudadProy()
    {
        return ciudadProy;
    }

    /**
     * Set the value of ciudadProy
     *
     * @param ciudadProy new value of ciudadProy
     */
    @Override
    public void setCiudadProy(String ciudadProy)
    {
        String oldCiudadProy = this.ciudadProy;
        this.ciudadProy = ciudadProy;
        propertyChangeSupport.firePropertyChange(PROP_CIUDADPROY, oldCiudadProy, ciudadProy);
    }

    private String tablero = "tablero donde se conecta el equipo";

    /**
     * Get the value of tablero
     *
     * @return the value of tablero
     */
    @Override
    public String getTablero()
    {
        return tablero;
    }

    /**
     * Set the value of tablero
     *
     * @param tablero new value of tablero
     */
    @Override
    public void setTablero(String tablero)
    {
        String oldTablero = this.tablero;
        this.tablero = tablero;
        propertyChangeSupport.firePropertyChange(PROP_TABLERO, oldTablero, tablero);
    }

    private int relacionBobina = 100;

    /**
     * Get the value of relacionBobina
     *
     * @return the value of relacionBobina
     */
    @Override
    public int getRelacionBobina()
    {
        return relacionBobina;
    }

    /**
     * Set the value of relacionBobina
     *
     * @param relacionBobina new value of relacionBobina
     */
    @Override
    public void setRelacionBobina(int relacionBobina)
    {
        int oldRelacionBobina = this.relacionBobina;
        this.relacionBobina = relacionBobina;
        propertyChangeSupport.firePropertyChange(PROP_RELACIONBOBINA, oldRelacionBobina, relacionBobina);
    }

    private int relacionTensionPrim = 1;

    /**
     * Get the value of relacionTensionPrim
     *
     * @return the value of relacionTensionPrim
     */
    @Override
    public int getRelacionTensionPrim()
    {
        return relacionTensionPrim;
    }

    /**
     * Set the value of relacionTensionPrim
     *
     * @param relacionTensionPrim new value of relacionTensionPrim
     */
    @Override
    public void setRelacionTensionPrim(int relacionTensionPrim)
    {
        int oldRelacionTensionPrim = this.relacionTensionPrim;
        this.relacionTensionPrim = relacionTensionPrim;
        propertyChangeSupport.firePropertyChange(PROP_RELACIONTENSIONPRIM, oldRelacionTensionPrim, relacionTensionPrim);
    }

    private int relacionTensionSec = 1;

    /**
     * Get the value of relacionTensionSec
     *
     * @return the value of relacionTensionSec
     */
    @Override
    public int getRelacionTensionSec()
    {
        return relacionTensionSec;
    }

    /**
     * Set the value of relacionTensionSec
     *
     * @param relacionTensionSec new value of relacionTensionSec
     */
    @Override
    public void setRelacionTensionSec(int relacionTensionSec)
    {
        int oldRelacionTensionSec = this.relacionTensionSec;
        this.relacionTensionSec = relacionTensionSec;
        propertyChangeSupport.firePropertyChange(PROP_RELACIONTENSIONSEC, oldRelacionTensionSec, relacionTensionSec);
    }

    private int periodoSD = 1;

    /**
     * Get the value of periodoSD / segundos
     *
     * @return the value of periodoSD
     */
    @Override
    public int getPeriodoSD()
    {
        return periodoSD;
    }

    /**
     * Set the value of periodoSD / segundos
     *
     * @param periodoSD new value of periodoSD
     */
    @Override
    public void setPeriodoSD(int periodoSD)
    {
        int oldPeriodoSD = this.periodoSD;
        this.periodoSD = periodoSD;
        propertyChangeSupport.firePropertyChange(PROP_PERIODOSD, oldPeriodoSD, periodoSD);
    }

    private ETipoRED tipoRed = ETipoRED.tresFases_FFFN;

    /**
     * Get the value of tipoRed
     *
     * @return the value of tipoRed
     */
    @Override
    public ETipoRED getTipoRed()
    {
        return tipoRed;
    }

    /**
     * Set the value of tipoRed
     *
     * @param tipoRed new value of tipoRed
     */
    @Override
    public void setTipoRed(ETipoRED tipoRed)
    {
        ETipoRED oldTipoRed = this.tipoRed;
        this.tipoRed = tipoRed;
        propertyChangeSupport.firePropertyChange(PROP_TIPORED, oldTipoRed, tipoRed);
    }

    private String csvName = "csvName";

    /**
     * Get the value of csvName
     *
     * @return the value of csvName
     */
    @Override
    public String getCsvName()
    {
        return csvName;
    }

    /**
     * Set the value of csvName
     *
     * @param csvName new value of csvName
     */
    @Override
    public void setCsvName(String csvName)
    {
        String oldCsvName = this.csvName;
        this.csvName = csvName;
        propertyChangeSupport.firePropertyChange(PROP_CSVNAME, oldCsvName, csvName);
    }

    /**
     * la ruta en disco del proyecto
     */
    private String rutaProyecto = "";

    @Override
    public String getRutaProyecto()
    {
        return rutaProyecto;
    }

    @Override
    public void setRutaProyecto(String rutaProyecto)
    {
        String oldRutaProyecto = this.rutaProyecto;
        this.rutaProyecto = rutaProyecto;
        propertyChangeSupport.firePropertyChange(PROP_RUTAPROYECTO, oldRutaProyecto, rutaProyecto);
    }

    private String[] listaRecientes =
    {
        "", "", "", "", "", "", "", "", "", ""
    };

    /**
     * Get the value of listaRecientes / hasta 10 datos en el arreglo
     *
     * @return the value of listaRecientes
     */
    @Override
    public String[] getListaRecientes()
    {
        return listaRecientes;
    }

    /**
     * Set the value of listaRecientes
     *
     * @param listaRecientes new value of listaRecientes
     */
    @Override
    public void setListaRecientes(String[] listaRecientes)
    {
        String[] oldListaRecientes = this.listaRecientes;
        this.listaRecientes = listaRecientes;
        propertyChangeSupport.firePropertyChange(PROP_LISTARECIENTES, oldListaRecientes, listaRecientes);
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
        this.estado = Estado;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        setEstado(EEstado.EDITADO);
    }

}

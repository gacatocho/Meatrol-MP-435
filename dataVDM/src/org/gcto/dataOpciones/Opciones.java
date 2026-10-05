/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataOpciones;

import org.gcto.dataEnum.EEstado;
import org.gcto.interfases.IOpciones;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.persistence.Entity;
import org.gcto.dataBD.SalvarClaseGenerica;
import org.gcto.dataGlobal.glb;
import org.openide.util.Exceptions;
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
        addPropertyChangeListener(this);
    }

    /**
     * Color fase A
     */
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

    /**
     * Color fase B
     */
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

    /**
     * color fase C
     */
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

    /**
     * color del neutro (blanco para fondo negro)
     */
    private String colorNeutro = "255,255,255";

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

    /**
     * color alternativo de A
     */
    private String colorAltA = "204,204,0";

    @Override
    public String getColorAltA()
    {
        return colorAltA;
    }

    @Override
    public void setColorAltA(String colorAltA)
    {
        String oldColorAltA = this.colorAltA;
        this.colorAltA = colorAltA;
        propertyChangeSupport.firePropertyChange(PROP_COLORALTA, oldColorAltA, colorAltA);
    }

    /**
     * coloar alternativo a B
     */
    private String colorAltB = "51,153,255";

    @Override
    public String getColorAltB()
    {
        return colorAltB;
    }

    @Override
    public void setColorAltB(String colorAltB)
    {
        String oldColorAltB = this.colorAltB;
        this.colorAltB = colorAltB;
        propertyChangeSupport.firePropertyChange(PROP_COLORALTB, oldColorAltB, colorAltB);
    }

    /**
     * coloar alternativo C
     */
    private String colorAltC = "255,128,0";

    @Override
    public String getColorAltC()
    {
        return colorAltC;
    }

    @Override
    public void setColorAltC(String colorAltC)
    {
        String oldColorAltC = this.colorAltC;
        this.colorAltC = colorAltC;
        propertyChangeSupport.firePropertyChange(PROP_COLORALTC, oldColorAltC, colorAltC);
    }

    /**
     * color alternativo a neutro
     */
    private String colorAltN = "255,153,51";

    @Override
    public String getColorAltN()
    {
        return colorAltN;
    }

    @Override
    public void setColorAltN(String colorAltN)
    {
        String oldColorAltN = this.colorAltN;
        this.colorAltN = colorAltN;
        propertyChangeSupport.firePropertyChange(PROP_COLORALTN, oldColorAltN, colorAltN);
    }

    /**
     * valor de total potencia activa
     */
    private String colorTotP_Act = "0,102,204";

    @Override
    public String getColorTotP_Act()
    {
        return colorTotP_Act;
    }

    @Override
    public void setColorTotP_Act(String colorTotP_Act)
    {
        String oldColorTotP_Act = this.colorTotP_Act;
        this.colorTotP_Act = colorTotP_Act;
        propertyChangeSupport.firePropertyChange(PROP_COLORTOTP_ACT, oldColorTotP_Act, colorTotP_Act);
    }

    /**
     * color total de potencia reactiva
     */
    private String colorTotQ_react = "255,0,127";

    @Override
    public String getColorTotQ_react()
    {
        return colorTotQ_react;
    }

    @Override
    public void setColorTotQ_react(String colorTotQ_react)
    {
        String oldColorTotQ_react = this.colorTotQ_react;
        this.colorTotQ_react = colorTotQ_react;
        propertyChangeSupport.firePropertyChange(PROP_COLORTOTQ_REACT, oldColorTotQ_react, colorTotQ_react);
    }

    /**
     * color total potencia aparente
     */
    private String colorTotS_apar = "255,102,102";

    @Override
    public String getColorTotS_apar()
    {
        return colorTotS_apar;
    }

    @Override
    public void setColorTotS_apar(String colorTotS_apar)
    {
        String oldColorTotS_apar = this.colorTotS_apar;
        this.colorTotS_apar = colorTotS_apar;
        propertyChangeSupport.firePropertyChange(PROP_COLORTOTS_APAR, oldColorTotS_apar, colorTotS_apar);
    }

    /**
     * color promedio corriente
     */
    private String color_I_AVG = "255,102,178";

    @Override
    public String getColor_I_AVG()
    {
        return color_I_AVG;
    }

    @Override
    public void setColor_I_AVG(String color_I_AVG)
    {
        String oldColor_I_AVG = this.color_I_AVG;
        this.color_I_AVG = color_I_AVG;
        propertyChangeSupport.firePropertyChange(PROP_COLOR_I_AVG, oldColor_I_AVG, color_I_AVG);
    }

    /**
     * color U promedio
     */
    private String color_V_AVG = "55,251,55";

    @Override
    public String getColor_V_AVG()
    {
        return color_V_AVG;
    }

    @Override
    public void setColor_V_AVG(String color_V_AVG)
    {
        String oldColor_V_AVG = this.color_V_AVG;
        this.color_V_AVG = color_V_AVG;
        propertyChangeSupport.firePropertyChange(PROP_COLOR_V_AVG, oldColor_V_AVG, color_V_AVG);
    }

    /**
     * color de promedio de frecuencia
     */
    private String color_F_AVG = "0,255,128";

    @Override
    public String getColor_F_AVG()
    {
        return color_F_AVG;
    }

    @Override
    public void setColor_F_AVG(String color_F_AVG)
    {
        String oldColor_F_AVG = this.color_F_AVG;
        this.color_F_AVG = color_F_AVG;
        propertyChangeSupport.firePropertyChange(PROP_COLOR_F_AVG, oldColor_F_AVG, color_F_AVG);
    }

    /**
     * color de promedio corrientes demanda
     */
    private String color_IDM_AVG = "0,255,0";

    @Override
    public String getColor_IDM_AVG()
    {
        return color_IDM_AVG;
    }

    @Override
    public void setColor_IDM_AVG(String color_IDM_AVG)
    {
        String oldColor_IDM_AVG = this.color_IDM_AVG;
        this.color_IDM_AVG = color_IDM_AVG;
        propertyChangeSupport.firePropertyChange(PROP_COLOR_IDM_AVG, oldColor_IDM_AVG, color_IDM_AVG);
    }

    /**
     * ruta para el logo del informe
     */
    private String logoPath = "ENTRE LA RUTA DEL LOGO";

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
        //String[] oldListaRecientes = this.listaRecientes;
        this.listaRecientes = listaRecientes;
        //esto garantiza que no estemos apuntando al mismo array y dispare el evento
        propertyChangeSupport.firePropertyChange(PROP_LISTARECIENTES, null, listaRecientes);
    }

    /**
     * vector para color de armonicos / es fijo no se cambia
     */
    public static final String[] colorArm =
    {
        "230,25,75", // rojo
        "60,180,75", // verde
        "0,130,200", // azul
        "245,130,48", // naranja
        "145,30,180", // púrpura
        "70,240,240", // cian
        "240,50,230", // magenta
        "210,245,60", // lima
        "250,190,190", // rosa
        "0,128,128", // teal
        "230,190,255", // lavanda
        "170,110,40", // marrón
        "255,250,200", // crema
        "128,0,0", // granate
        "170,255,195", // menta
        "128,128,0", // oliva
        "255,215,180", // melocotón
        "0,0,128", // azul marino
        "128,128,128", // gris medio
        "255,255,25", // amarillo vivo
        "100,0,255",
        "255,100,0",
        "0,200,100",
        "255,0,100",
        "0,100,255",
        "200,255,0",
        "255,0,200",
        "0,255,200",
        "200,0,255",
        "255,200,0",
        "50,150,255",
        "255,50,150",
        "150,255,50",
        "50,255,150",
        "150,50,255",
        "255,150,50",
        "20,180,120",
        "180,20,120",
        "120,180,20",
        "20,120,180",
        "120,20,180",
        "180,120,20",
        "90,220,255",
        "255,90,220",
        "220,255,90",
        "90,255,220",
        "220,90,255",
        "255,220,90",
        "0,170,255",
        "255,0,170",
        "170,255,0",
        "255,170,0",
        "0,255,170"
    };

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
        this.estado = Estado;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (!estado.equals(EEstado.NUEVO))
        {
            setEstado(EEstado.EDITADO);
        }

        //carga los colores por si alguno ha cambiado
        glb.cargarColores();
        try
        {
            SalvarClaseGenerica.salvarObjeto(glb.opc);
        } catch (IOException ex)
        {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * inserta un path para el historico del vistos
     *
     * @param path
     */
    public void insertarPathHistorico(String path) throws IOException
    {
        if (path == null || path.trim().isEmpty())
        {
            return;
        }

        // Si ya existe, lo movemos al principio
        if (IsRecienteExiste(path))
        {
            int index = -1;
            for (int i = 0; i < 10; i++)
            {
                if (path.equals(listaRecientes[i]))
                {
                    index = i;
                    break;
                }
            }
            if (index != -1)
            {
                for (int i = index; i > 0; i--)
                {
                    listaRecientes[i] = listaRecientes[i - 1];
                }
                listaRecientes[0] = path;
                return;
            }
        }

        // Si no existe, desplazamos todos hacia la derecha y ponemos el nuevo en 0
        for (int i = 8; i >= 0; i--)
        {
            listaRecientes[i + 1] = listaRecientes[i];
        }
        listaRecientes[0] = path;

        setListaRecientes(listaRecientes);

    }

    /**
     * indica si este reciente ya existe en la lista o no
     *
     * @param path
     * @return
     */
    public boolean IsRecienteExiste(String path)
    {
        if (path == null)
        {
            return false;
        }
        for (int i = 0; i < 10; i++)
        {
            if (path.equals(listaRecientes[i]))
            {
                return true;
            }
        }
        return false;
    }

}

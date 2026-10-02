/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataProyecto;

import org.gcto.dataProyecto.DatosProy;
import java.lang.reflect.InvocationTargetException;
import org.gcto.dataEnum.EEstado;
import org.gcto.dataEnum.ETipoRED;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author camilo
 */
public class DatosProyNode extends AbstractNode
{

    public DatosProy datos;

    public DatosProyNode(DatosProy datos)
    {
        super(Children.LEAF);
        this.datos = datos;
        setDisplayName("Datos Pory");
    }

    @Override
    protected Sheet createSheet()
    {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set setCliente = Sheet.createPropertiesSet();
        setCliente.setName("cliente");
        setCliente.setDisplayName("Datos del Cliente");
        Sheet.Set setTecnico = Sheet.createPropertiesSet();
        setTecnico.setName("tecnico");
        setTecnico.setDisplayName("Datos del operador");
        Sheet.Set setRed = Sheet.createPropertiesSet();
        setRed.setName("red");
        setRed.setDisplayName("Datos de la red Medida");
        Sheet.Set setME = Sheet.createPropertiesSet();
        setME.setName("ME");
        setME.setDisplayName("Parámetros de trabajo del equipo");
        Sheet.Set setRutas = Sheet.createPropertiesSet();
        setRutas.setName("rutas");
        setRutas.setDisplayName("Rutas de almacenamiento de información");
        Sheet.Set setCom = Sheet.createPropertiesSet();
        setCom.setName("com");
        setCom.setDisplayName("Comentario y aclaraciones");
        Sheet.Set setEst = Sheet.createPropertiesSet();
        setEst.setName("est");
        setEst.setDisplayName("Estado de la información del proyecto");

//<editor-fold defaultstate="collapsed" desc="CLIENTE">
        try
        {
            Property<String> nombreProp = new PropertySupport.ReadWrite<String>("nombreProy", String.class, "Nombre del proyecto", "Es el nombre que identifica al proyecto")
            {
                @Override
                public String getValue() throws IllegalAccessException, InvocationTargetException
                {
                    return datos.getNombreProy();
                }

                @Override
                public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
                {
                    datos.setNombreProy(t);
                }
            };

            setCliente.put(nombreProp);
        } catch (Exception e)
        {
            System.out.println("********************************************************");
            System.out.println("En Sheet de nombreProp : " + e.getMessage());
            System.out.println("********************************************************");
        }

        Property<String> ubicacionProyProp = new PropertySupport.ReadWrite<String>("ubicacionProy", String.class, "Ubicación del proyecto", "La ubicación geográfica o de referencia del proyecto")
        {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getUbicacionProy();
            }

            @Override
            public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setUbicacionProy(t);
            }
        };

        setCliente.put(ubicacionProyProp);

        Property<String> cudadProyProp = new PropertySupport.ReadWrite<String>("ciudadProy", String.class, "Ciudad donde esta el proyecto", "La ciudad o pueblo o estado donde esta el proyecto")
        {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getCiudadProy();
            }

            @Override
            public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setCiudadProy(t);
            }
        };

        setCliente.put(cudadProyProp);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="TECNICO">
        Property<String> tecnicoProyProp = new PropertySupport.ReadWrite<String>("operadorME", String.class, "Operador del equipo", "Técnico u operador que toma  la medida con el analizador")
        {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getOperadorME();
            }

            @Override
            public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setOperadorME(t);
            }
        };

        setTecnico.put(tecnicoProyProp);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="RED Y TABLERO">
        Property<ETipoRED> tipoRedProyProp = new PropertySupport.ReadWrite<ETipoRED>("tipoRed", ETipoRED.class, "tipo de red a medir", "Explica cuantas fases y el tipo de RED a la cual se le toma la medida")
        {
            @Override
            public ETipoRED getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getTipoRed();
            }

            @Override
            public void setValue(ETipoRED t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setTipoRed(t);
            }
        };

        setTecnico.put(tipoRedProyProp);

        Property<String> tableroProp = new PropertySupport.ReadWrite<String>("tablero", String.class, "Tablero donde se toma la medida", "Indica cual es el tablero o cuadro de cargas donde se instaló el equipo para tomar la medida")
        {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getTablero();
            }

            @Override
            public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setTablero(t);
            }
        };

        setRed.put(tableroProp);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="PARAMETROS ME">
        Property<Integer> relacionBobinaProp = new PropertySupport.ReadWrite<Integer>("relacionbobina", Integer.class, "Relación de la bobina de medida", "Indica la corriente de ajusta de la medida  - se ajusta según el tipo de elementos con el que se mida  y según las indicaciones del manual del ME435")
        {
            @Override
            public Integer getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getRelacionBobina();
            }

            @Override
            public void setValue(Integer t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setRelacionBobina(t);
            }
        };

        setME.put(relacionBobinaProp);

        Property<Integer> relacionTensionPrimProp = new PropertySupport.ReadWrite<Integer>("relacionTensionPrim", Integer.class, "Relacion del primario de TP", "Indica la relación del primario en caso de haber usado un transformador de tensión para capturar el voltaje en la sonda, si no se usa o sea es conexión directa se coloca 1")
        {
            @Override
            public Integer getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getRelacionTensionPrim();
            }

            @Override
            public void setValue(Integer t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setRelacionTensionPrim(t);
            }
        };

        setME.put(relacionTensionPrimProp);

        Property<Integer> relacionTensionSecProp = new PropertySupport.ReadWrite<Integer>("relacionTensionSec", Integer.class, "Relacion del secundario de TP", "Indica la relación del secundario en caso de haber usado un transformador de tensión para capturar el voltaje en la sonda, si no se usa o sea es conexión directa se coloca 1")
        {
            @Override
            public Integer getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getRelacionTensionSec();
            }

            @Override
            public void setValue(Integer t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setRelacionTensionSec(t);
            }
        };

        setME.put(relacionTensionSecProp);

        Property<Integer> periodoSDProp = new PropertySupport.ReadWrite<Integer>("periodoSD", Integer.class, "Perido de Demanda", "indica el periodo que se evalúa al calcular la demanda de corriente o potencia - se da en segundos o minutos")
        {
            @Override
            public Integer getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getPeriodoSD();
            }

            @Override
            public void setValue(Integer t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setPeriodoSD(t);
            }
        };

        setME.put(periodoSDProp);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="RUTAS">
        Property<String> csvNameProp = new PropertySupport.ReadOnly<String>("csvName", String.class, "Ruta del archivo CSV", "Indica de donde se tomo la información para cargar el proyecto")
        {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getCsvName();
            }

        };

        setRutas.put(csvNameProp);

        Property<String> rutaProyectoProp = new PropertySupport.ReadOnly<String>("rutaProyecto", String.class, "Ruta en disco del proyecto", "Indica la ruta o directorio donde se encuentra la información del proyecto actual")
        {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getRutaProyecto();
            }
        };

        setRutas.put(rutaProyectoProp);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="COMENTARIOS">
        Property<String> comentarioProp = new PropertySupport.ReadWrite<String>("comentario", String.class, "Comentarios", "Comentarios y aclaraciones a la medida y el proyecto")
        {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getComentario();
            }

            @Override
            public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                datos.setComentario(t);
            }
        };
        setCom.put(comentarioProp);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ESTADO">
        Property<EEstado> estadoProp = new PropertySupport.ReadOnly<EEstado>("estado", EEstado.class, "Estado del proyecto", "indica si el proyecto fue cambiado o editado")
        {
            @Override
            public EEstado getValue() throws IllegalAccessException, InvocationTargetException
            {
                return datos.getEstado();
            }

        };
        setEst.put(estadoProp);
//</editor-fold>

        sheet.put(setCliente);
        sheet.put(setTecnico);
        sheet.put(setRed);
        sheet.put(setME);
        sheet.put(setRutas);
        sheet.put(setCom);
        sheet.put(setEst);

//            System.out.println("********************************************************");
//            System.out.println("En Sheet de datosProy : " + e.getMessage());
//            System.out.println("********************************************************");
        return sheet;

    }

}

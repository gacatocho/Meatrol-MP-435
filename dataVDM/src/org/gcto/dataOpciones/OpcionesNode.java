/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataOpciones;

import java.awt.Color;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import org.gcto.dataEnum.EEstado;
import org.gcto.dataGlobal.glb;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 * Node para encapsular a Opciones en un property grid
 *
 * @author camilo
 */
public class OpcionesNode extends AbstractNode
{

    public Opciones opc;

    public OpcionesNode(Opciones OPC)
    {
        super(Children.LEAF);
        this.opc = OPC;
        setDisplayName("Opciones");
    }

    @Override
    protected Sheet createSheet()
    {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set setColores = Sheet.createPropertiesSet();
        setColores.setName("colores");
        Sheet.Set setEst = Sheet.createPropertiesSet();
        setEst.setName("estado");
        Sheet.Set setPath = Sheet.createPropertiesSet();
        setPath.setName("rutaLogo");
        Sheet.Set setColorArm = Sheet.createPropertiesSet();
        setColorArm.setName("colores Armónicos");

//<editor-fold defaultstate="collapsed" desc="COLORES">
        Property<Color> colorAprop = new PropertySupport.ReadWrite<Color>("colorA", Color.class, "Color fase A", "Color usado para identificar la Fase A")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorFaseA());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorFaseA(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorAprop);

        Property<Color> colorBprop = new PropertySupport.ReadWrite<Color>("colorB", Color.class, "Color fase B", "Color usado para identificar la Fase B")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorFaseB());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorFaseB(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorBprop);

        Property<Color> colorCprop = new PropertySupport.ReadWrite<Color>("colorC", Color.class, "Color fase C", "Color usado para identificar la Fase C")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorFaseC());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorFaseC(glb.RGB_String_de_Color(t));
            }
        };

        Property<Color> colorNprop = new PropertySupport.ReadWrite<Color>("colorN", Color.class, "Color neutro N", "Color usado para identificar el neutro")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorNeutro());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorNeutro(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorNprop);

        Property<Color> colorAAprop = new PropertySupport.ReadWrite<Color>("colorAA", Color.class, "Color fase A Alt", "Color usado para identificar la Fase A - alternativo")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorAltA());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorAltA(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorAAprop);

        Property<Color> colorBBprop = new PropertySupport.ReadWrite<Color>("colorBB", Color.class, "Color fase B Alt", "Color usado para identificar la Fase B - alternativo")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorAltB());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorAltB(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorBBprop);

        Property<Color> colorCCprop = new PropertySupport.ReadWrite<Color>("colorCC", Color.class, "Color fase C Alt", "Color usado para identificar la Fase C - alternativo")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorAltC());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorAltC(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorCCprop);

        Property<Color> colorNNprop = new PropertySupport.ReadWrite<Color>("colorNN", Color.class, "Color neutro Alt", "Color usado para identificar al neutro - alternativo")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorAltN());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorAltN(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorNNprop);

        Property<Color> colorPsumprop = new PropertySupport.ReadWrite<Color>("colorPsum", Color.class, "Color suma de Pot Activa", "Color usado para identificar la sumatoria de potencia activa")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorTotP_Act());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorTotP_Act(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorPsumprop);

        Property<Color> colorQsumprop = new PropertySupport.ReadWrite<Color>("colorQsum", Color.class, "Color suma de Pot Reactiva", "Color usado para identificar la sumatoria de potencia reactiva")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorTotQ_react());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorTotQ_react(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorQsumprop);

        Property<Color> colorSsumprop = new PropertySupport.ReadWrite<Color>("colorSsum", Color.class, "Color suma de Pot Aparente", "Color usado para identificar la sumatoria de potencia aparente")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColorTotS_apar());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColorTotS_apar(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorSsumprop);

        Property<Color> colorIAVG = new PropertySupport.ReadWrite<Color>("colorIAVG", Color.class, "Color de promedio de corrientes", "Color usado para identificar el promedio de corrientes")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColor_I_AVG());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColor_I_AVG(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorIAVG);

        Property<Color> colorVAVG = new PropertySupport.ReadWrite<Color>("colorVAVG", Color.class, "Color para promedio tension", "Color usado para identificar el promedio de las tensiones de fase")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColor_V_AVG());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColor_V_AVG(glb.RGB_String_de_Color(t));
            }
        };

        setColores.put(colorVAVG);

        Property<Color> colorFAVG = new PropertySupport.ReadWrite<Color>("colorFAVG", Color.class, "Color para promedio Frecuencia", "Color usado para identificar el promedio de la Frecuencia")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColor_F_AVG());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColor_F_AVG(glb.RGB_String_de_Color(t));
            }
        };

        Property<Color> colorIDMAVG = new PropertySupport.ReadWrite<Color>("colorIDMVG", Color.class, "Color para promedio Demanda de corriente", "Color usado para identificar el promedio de la Demanda de corriente")
        {
            @Override
            public Color getValue() throws IllegalAccessException, InvocationTargetException
            {
                return glb.colorDe_RGB_String(opc.getColor_IDM_AVG());
            }

            @Override
            public void setValue(Color t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setColor_IDM_AVG(glb.RGB_String_de_Color(t));
            }
        };
        setColores.put(colorIDMAVG);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ESTADO">
        Property<EEstado> estProp = new PropertySupport.ReadOnly<EEstado>("estado", EEstado.class, "Estado de opciones", "indica el estado actual del objeto opciones")
        {
            @Override
            public EEstado getValue() throws IllegalAccessException, InvocationTargetException
            {
                return opc.getEstado();
            }

        };

        setEst.put(estProp);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="RUTA/PATH">
        Property<String> patlLogoprop = new PropertySupport<String>("logo", String.class, "Ruta para logo de informe", "Proporciona una ruta para ubicar el logo que se insertara en el informe del", true, true)
        {
            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException
            {
                return opc.getLogoPath();
            }

            @Override
            public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
            {
                opc.setLogoPath(t);
            }

            @Override
            public PropertyEditor getPropertyEditor()
            {
                return new FilePathEditor();
            }

        };

        setPath.put(patlLogoprop);
//</editor-fold>

        //**********************
        sheet.put(setColores);
        sheet.put(setEst);
        sheet.put(setPath);

        return sheet;
    }

}

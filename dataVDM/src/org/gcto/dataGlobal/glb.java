/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;

/**
 * Clase que hace diferentes cálculos y operaciones para completar y
 * complementar la lista que ha leido el equipo
 *
 * @author camilo
 */
public class glb
{

    /**
     * variable estatica que indica cuantas fases estamos midiendo, se carga al
     * leer el archivo en el diálogo de lectura
     */
    public static int numFases = 1;

    //para ejemplo unicamente, luego se involucra en el formualrio de abrir un  proyecto nuevo
    public static ETipoRED tipoRed = ETipoRED.tresFases_FFN; //por defecto para ejemplo

    /**
     * Indica si el sistema está en modo oscuro (true) o claro (false).
     */
    public static boolean darkMode = true;

    /**
     * color de fase A
     */
    public static Color colorA = Color.YELLOW;
    /**
     * color fase B
     */
    public static Color colorB = Color.BLUE;
    /**
     * color Fase C
     */
    public static Color colorC = Color.RED;

    /**
     * color alterno fase A
     */
    public static Color colorAA = Color.ORANGE;
    /**
     * color alterno fase B
     */
    public static Color colorBB = Color.CYAN;
    /**
     * color alterno fase C
     */
    public static Color colorCC = Color.PINK;

    /**
     * color del neutro. Se ajusta dinámicamente según el tema.
     */
    public static Color colorN = Color.WHITE;

    /**
     * tensión entre fases AB
     */
    public static double UAB = 0;

    /**
     * tensión entre fasses BC
     */
    public static double UBC = 0;

    /**
     * tensión entre fases AC
     */
    public static double UAC = 0;

    /**
     * corriente de Neutro calculada
     */
    public static double IN = 0;

    /**
     * el grosor de la linea de promedio
     */
    public static float grosLinProm = 1.8f;

    // --- SINCRONIZACIÓN GLOBAL DE RANGO TEMPORAL ---
    /**
     * Indica si el cambio de rango en un formulario afecta a todos los demás.
     */
    public static boolean syncTimeRange = false;

    /**
     * Índice de inicio global (fila de la tabla maestra).
     */
    public static int globalStartIndex = 0;

    /**
     * Índice de fin global (fila de la tabla maestra).
     */
    public static int globalEndIndex = 0;

    /**
     * Calcla la potencia reactiva a partir de datos leidos
     *
     * @param S - la potencia aparente leida en la tabla
     * @param P - la potencia activa leida en la tabla
     * @param Q - la potencia reactiva leida en la tabla
     * @return
     */
    public static double CalcReactivaFase(double S, double P, double Q)
    {
        //variable par ael cálculo
        double q = 0;
        //calcula a q
        q = Math.sqrt((Math.pow(S, 2)) - (Math.pow(P, 2)));

        if (Q >= 0)
        {
            //el sistema es inductivo - reactiva positiva
            return q;
        } else
        {
            //el sistema es capacitivo - reactiva negativa
            return -q;
        }
    }

    /**
     * calcula las tensiones de fase dependiendo de lo que llegue en los
     * parametros
     *
     * @param UA
     * @param UB
     * @param UC
     * @return
     */
    public static void calcularVFase(double UA, double UB, double UC)
    {
        double VAx;
        double VAy;
        double VBx;
        double VBy;
        double VCx;
        double VCy;

        switch (tipoRed)
        {
            case monFase_FN:
                //no se calcula
                break;
            case monoFase_FF:
            case monoFase_FFN:
                //el calculo se hace con desfase de 180 grados sobre VA y VB
                //VA por defincion es angulo 0 y VB es angulo de 180 grados
                UAB = UA + UB;
                break;
            case tresFases_FN:
                //no se calcula
                break;
            case tresFases_FFN:
                //Va desfase =0, VB desfae = -120 grados
                //Se suma vectorialmente 
                VAx = UA;
                VAy = 0;
                VBx = UB * Math.cos(Math.toRadians(-120));
                VBy = UB * Math.sin(Math.toRadians(-120));
                UAB = Math.sqrt(Math.pow((VAx - VBx), 2) + Math.pow((VAy + VBy), 2));
                break;
            case tresFases_FFF:
            case tresFases_FFFN:
                //Va desfase =0, VB desfae = -120 grados VC = 120 grados
                //Se suma vectorialmente 
                VAx = UA;
                VAy = 0;
                VBx = UB * Math.cos(Math.toRadians(-120));
                VBy = UB * Math.sin(Math.toRadians(-120));
                UAB = Math.sqrt(Math.pow((VAx - VBx), 2) + Math.pow((VAy + VBy), 2));
                VCx = UC * Math.cos(Math.toRadians(120));
                VCy = UC * Math.sin(Math.toRadians(120));
                UBC = Math.sqrt(Math.pow((VBx - VCx), 2) + Math.pow((-VBy + VCy), 2));
                UAC = Math.sqrt(Math.pow((VAx - VCx), 2) + Math.pow((VAy + VCy), 2));
                break;
            default:
                throw new AssertionError();
        }

    }

    /**
     * calcula la corrriente de neutro a partos de las corrientes de fase
     * basicamente en sistemas de tres fases o de dos fases que teng neutro la
     * corriente del mismo es la suma de las fases (en fasores)
     *
     * @param IA - magnitud de corriente fase A
     * @param IB - magnitud de corriente fase B
     * @param IC - magnitud decorriente vase C (Es cero no no esta)
     * @param FPA - factor de potencia Fase A / se coloca negativo si al QA es
     * negativa
     * @param FPB - factor de potencia fase B / se coloca negativo si al QB es
     * negativa
     * @param FPC - factor de potencia fase C / se coloca negativo si al QC es
     * negativa
     */
    public static void calculoNeutro(
            double IA, double IB, double IC,
            double FPA, double FPB, double FPC,
            double QA, double QB, double QC)
    {
        //con  los datos se hace la suma vectorial d las corrientes
        //veamos los angulos de las corrientes

        double angA;

        if (QA >= 0)
        {
            //inductivo
            angA = 0 - Math.acos(FPA);
        } else
        {
            //capacitivo
            angA = 0 + Math.acos(FPA);
        }

        double angB;

        if (QB >= 0)
        {
            angB = Math.toRadians(-120) - Math.acos(FPB);
        } else
        {
            angB = Math.toRadians(-120) + Math.acos(FPB);
        }

        double angC;

        if (QC >= 0)
        {
            angC = Math.toRadians(120) - Math.acos(FPC);
        } else
        {
            angC = Math.toRadians(120) + Math.acos(FPC);
        }

        double INx = IA * Math.cos(angA) + IB * Math.cos(angB) + IC * Math.cos(angC);
        double INy = IA * Math.sin(angA) + IB * Math.sin(angB) + IC * Math.sin(angC);

        //se calcula la magnitud del neutro
        IN = Math.sqrt(Math.pow(INx, 2) + Math.pow(INy, 2));

    }

    /**
     * Parsea un String a double de forma robusta, manejando comas y puntos
     * decimales.
     *
     * @param val El string a parsear.
     * @return El valor double, o 0 si hay error.
     */
    public static double parseDoubleSafe(String val)
    {
        if (val == null || val.trim().isEmpty())
        {
            return 0;
        }
        try
        {
            String clean = val.trim().replaceAll("[^0-9,.\\-]", "");
            if (clean.isEmpty())
            {
                return 0;
            }

            int lastComma = clean.lastIndexOf(',');
            int lastDot = clean.lastIndexOf('.');

            if (lastComma > lastDot)
            {
                clean = clean.replace(".", "").replace(",", ".");
            } else if (lastDot > lastComma)
            {
                clean = clean.replace(",", "");
            } else if (lastComma != -1)
            {
                clean = clean.replace(",", ".");
            }

            return Double.parseDouble(clean);
        } catch (Exception e)
        {
            return 0;
        }
    }

    public static java.util.List<String> clasificacion = Arrays.asList(
            "Buena / Excelente", "Aceptable / Moderada", "Deficiente / Mala", "Crítica / Muy Mala");

    public static java.util.List<String> tipsClasificacion = Arrays.asList(
            "Sistema muy limpio. Las cargas son predominantemente lineales.",
            "Presencia típica de cargas electrónicas industriales (drivers, VFDs, iluminación LED). Requiere monitoreo.",
            "Alta contaminación armónica. Calentamiento en transformadores, disparo fortuito de protecciones y vibraciones en motores.",
            "Riesgo alto de fallo de equipos, sobrecalentamiento de cables neutros y resonancia destructiva si hay bancos de condensadores.");

    /**
     * clase opciones generales del programa
     */
    public static Opciones opc = new Opciones();

    /**
     * clase que maniene los datos generales del proyecto.
     */
    public static DatosProy dp = new DatosProy();

    /**
     * el archivo que contiene un CSV con la información directo del equipo
     */
    public static File selectedFileCSV;

    /**
     * el archivo que contiene un VDM que contiene uno ya procesado e incluido
     * en un proyecto
     */
    public static File seletedFileVDM;

    //<editor-fold defaultstate="collapsed" desc="DEFINICION DE TEXTOS PARA MAPEO DE COLUMNAS EXACTO">
//defincion para mapeo de columnas
    //fecha y hora
    public static String FECHA_MED = "Date&Time : Date";
    public static String HORA_MED = "Date&Time : Time";

    //tensiones
    //termino general apra fultrar por columnas de tensión
    public static String TENSION = "VOLTAJE(V)";
    //terminos para ubicar las de fase y las de linea
    public static String TENSION_FASE_A = "UA";
    public static String TENSION_FASE_B = "UB";
    public static String TENSION_FASE_C = "UC";
    public static String TENSION_LINEA_AB = "UAB";
    public static String TENSION_LINEA_BC = "UBC";
    public static String TENSION_LINEA_AC = "UAC";

    //armonicos de tension
    //termino generico para ubicar columna de armonicos de tension
    public static String HARM_V = "UTHD(%)";
    //termino para refinar por fase
    public static String HARM_VOLT_FASE_A = "UTHA";
    public static String HARM_VOLT_FASE_B = "UTHB";
    public static String HARM_VOLT_FASE_C = " UTHC";

    //corrientes
    //termino general para ubicar columnas de corriente
    public static String CORRIENTE = "CURRENT(A)";
    //termino para refinar por las fases
    public static String CORRIENTE_FASE_A = "IA";
    public static String CORRIENTE_FASE_B = "IB";
    public static String CORRIENTE_FASE_C = "IC";

    //armonicos de corriente
    //termino generico para ubicar columna de armonicos de corriente - CUALQUIer numero o columnas
    public static String HARM_I = "ITHD";
    //termino generico para refinar y ubicar fases
    public static String HARM_I_FASE_A = "A";
    public static String HARM_I_FASE_B = "B";
    public static String HARM_I_FASE_C = "C";

    //frecuencias
    //termino generico para ubicar columnas de frecuencias
    public static String FREQ = "FREQUENCY";
    //termino para refinar por fase (buscado el gneral se filtra por este para las fases)
    public static String FREQ_FASE_A = "FA";
    public static String FREQ_FASE_B = "FB";
    public static String FREQ_FASE_C = "FC";

    //factor de potencia (Power Factor)
    //termino generico para ubicar columnas de power factor
    public static String POWER_FACTOR = "POWER FACTOR";
    //termino para refinar por fases y promedio
    public static String POWER_FACTOR_FASE_A = "PFA";
    public static String POWER_FACTOR_FASE_B = "PFB";
    public static String POWER_FACTOR_FASE_C = "PFC";
    public static String POWER_FACTOR_AVERAG = "PF AVERAGE";

    //potencia activa
    //termino generico para ubicar columnas de potencia activa
    public static String ACTIVE_POWER = "ACTIVE POWER(W)";
    //terminmo para refinar por fases
    public static String ACTIVE_POWER_FASE_A = "PA";
    public static String ACTIVE_POWER_FASE_B = "PB";
    public static String ACTIVE_POWER_FASE_C = "PC";
    public static String ACTIVE_POWER_SUM = "PSUM";

    //potencia REactiva
    //termino generico para ubicar columnas de potencia REactiva MEDIDA
    public static String REACTIVE_POWER = "REACTIVE POWER(VAR)";
    //termino generico para ubicar columnas de potencia REactiva CALCULADA
    public static String REACTIVE_POWER_CALC = "REACTIVEPOWERCALC";
    //para las fases y suma en cualquiera de los dos casos medida o calculada
    public static String REACTIVE_POWER_FASE_A = "QA";
    public static String REACTIVE_POWER_FASE_B = "QB";
    public static String REACTIVE_POWER_FASE_C = "QC";
    public static String REACTIVE_POWER_SUM = "QSUM";

    //potencia aparente
    //termino generico para ubicar columnas de potencia aparente
    public static String APPARENT_POWER = "APPARENT POWER(VA)";
//para REFINAR la seleccion de las fases y suma
    public static String APPARENT_POWER_FASE_A = "SA";
    public static String APPARENT_POWER_FASE_B = "SB";
    public static String APPARENT_POWER_FASE_C = "SC";
    public static String APPARENT_POWER_SUM = "SSUM";

    //ENERGIA activa
    //termino generico para ubicar columnas de energía activa
    public static String ACTIVE_ENERGY = "ACTIVE ENERGY";
    //terminmo para refinar por fases
    public static String ACTIVE_ENERGY_FASE_A = "EPA";
    public static String ACTIVE_ENERGY_FASE_B = "EPB";
    public static String ACTIVE_ENERGY_FASE_C = "EPC";
    public static String ACTIVE_ENERGY_SUM = "EPSUM";

    //ENERGIA REactiva
    //termino generico para ubicar columnas de energía REactiva
    public static String REACTIVE_ENERGY = "REACTIVE ENERGY";
    //terminmo para refinar por fases
    public static String REACTIVE_ENERGY_FASE_A = "EQA";
    public static String REACTIVE_ENERGY_FASE_B = "EQB";
    public static String REACTIVE_ENERGY_FASE_C = "EQC";
    public static String REACTIVE_ENERGY_SUM = "EQSUM";

    //ENERGIA APARENTE
    //termino generico para ubicar columnas de energía APARENTE
    public static String APPARENT_ENERGY = "APPARENT ENERGY";
    //terminmo para refinar por fases
    public static String APPARENT_ENERGY_FASE_A = "ESA";
    public static String APPARENT_ENERGY_FASE_B = "ESB";
    public static String APPARENT_ENERGY_FASE_C = "ESC";
    public static String APPARENT_ENERGY_SUM = "ESSUM";

    //DEMANDA DE CORRIENTE
    //termino generico para todas las columnas de demanda
    public static String DEMAND = "DEMAND";

    //termino para refinar por demanda de corriente especificamente PARA CADA FASE
    public static String CURRENT_DEMAND_FASE_A = "DMIA";
    public static String CURRENT_DEMAND_FASE_B = "DMIB";
    public static String CURRENT_DEMAND_FASE_C = "DMIC";
    //termino para refinar por PICO DE demanda de corriente especificamente PARA CADA FASE
    public static String CURRENT_PEAK_DEMAND_FASE_A = "PDMIA";
    public static String CURRENT_PEAK_DEMAND_FASE_B = "PDMIB";
    public static String CURRENT_PEAK_DEMAND_FASE_C = "PDMIC";
    //COLUMNAS QUE REPRESENTANA LAS FECHAS POR FASE DEL PICO DE DEMANDA
    public static String CURRENT_DATE_PEAK_DEMAND_FASE_A = "PDMIA_D/T";
    public static String CURRENT_DATE_PEAK_DEMAND_FASE_B = "PDMIB_D/T";
    public static String CURRENT_DATE_PEAK_DEMAND_FASE_C = "PDMIC_D/T";

    //termino para refinar por demanda de POTENCIA ACTIVA TOTAL
    public static String TOTAL_ACTIVE_POWER_DEMAND = "DMP";
    //termino para refinar por PICO DE  demanda de POTENCIA ACTIVA TOTAL
    public static String TOTAL_PEAK_ACTIVE_POWER_DEMAND = "PDMP";
    //COLUMNA QUE REPRESENTANA LAS FECHAS POR FASE DEL PICO DE DEMANDA  DE POTENCIA ACTIVA TOTAL
    public static String TOTAL_ACTIVE_POWER_DATE_PEAK_DEMAND = "PDMP_D/T";

    //termino para refinar por demanda de POTENCIA REACTIVA TOTAL
    public static String TOTAL_REACTIVE_POWER_DEMAND = "DMQ";
    //termino para refinar por PICO DE  demanda de POTENCIA REACTIVA TOTAL
    public static String TOTAL_PEAK_REACTIVE_POWER_DEMAND = "PDMQ";
    //COLUMNA QUE REPRESENTANA LAS FECHAS POR FASE DEL PICO DE DEMANDA  DE POTENCIA REACTIVA TOTAL
    public static String TOTAL_REACTIVE_POWER_DATE_PEAK_DEMAND = "PDMQ_D/T";

    //termino para refinar por demanda de POTENCIA APARENTE TOTAL
    public static String TOTAL_APPARENT_POWER_DEMAND = "DMS";
    //termino para refinar por PICO DE  demanda de POTENCIA APARENTE TOTAL
    public static String TOTAL_PEAK_APPARENT_POWER_DEMAND = "PDMS";
    //COLUMNA QUE REPRESENTANA LAS FECHAS POR FASE DEL PICO DE DEMANDA  DE POTENCIA APARENTE TOTAL
    public static String TOTAL_APPARENT_POWER_DATE_PEAK_DEMAND = "PDMS_D/T";
//</editor-fold>
}

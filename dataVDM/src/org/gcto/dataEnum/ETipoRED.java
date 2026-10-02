/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.gcto.dataEnum;

/**
 * define lo stios de red a los que esta conectado el analizador
 *
 * @author camilo
 */
public enum ETipoRED
{
    /**
     * es conecta auna red monofasica de una fase - angulo de desfase de V es 0
     * es fase y neutro
     */
    monFase_FN,
    /**
     * sistema monofasico donde se miden las dos fases, no se usa el neutro _
     * desfase de V es de 180º
     */
    monoFase_FF,
    /**
     * sistema monofasico donde se miden las dos fases, y se usa el neutro _
     * desfase de V es de 180º
     */
    monoFase_FFN,
    /**
     * sistema trifasico donde se miden solo una Fase_ Neutro _ desfase de V es
     * de 0º
     */
    tresFases_FN,
    /**
     * sistema trifasico donde se miden dos Fases y neutro se usa - desfase
     * de V es de 120º
     */
    tresFases_FFN,
    /**
     * sistema trifasico donde se miden Tres Fases y neutro no se usa - desfase
     * de V es de -120º y 120º 
     */
    tresFases_FFF,
/**
     * sistema trifasico donde se miden Tres Fases y neutro si se usa - desfase
     * de V es de -120º y 120º 
     */
    tresFases_FFFN    

}

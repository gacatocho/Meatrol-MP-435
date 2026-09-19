/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataGlobal;

import java.io.File;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.gcto.interfases.IBaseVDM;

/**
 * recuperqa de la base de datos una clsae generica dada
 *
 * @author camilo
 */
public class RecuperarClaseGenerica
{

    /**
     * recupera la clase de la base de datos y carga los datos en las clases
     * correspondientes para el uso dentro de la sesion
     *
     * @param nombreClase - el nombre de la clase a recuperar
     */
    public static boolean cargarObjeto(String nombreClase, String RutaBD)
    {
        /**
         * nombre de la sola base de datos
         */
        String nomBD = null;
        /**
         * nombre completo de la base de datos, ruta y nombre
         */
        String BD = null;

        //se recuper ala ruta para guardar la base de datos
        String rutaBD = RutaBD;

        if (nombreClase.equals("DatosProy"))
        {
            nomBD = "datosProyecto";
        }

        if (nombreClase.equals("Opciones"))
        {  
            nomBD = "opciones";
   
        }
        //aqui ya sabemos la ruta, el nombre y el objeto a guardar

        if (nomBD != null)
        {
            BD = rutaBD + File.separator + nomBD + ".odb";
        } else
        {
            System.out.println("Error en lo snombres de la base de datos");
            
            return false;
        }

        IBaseVDM baseVDM = recuperarElementosDeLaClase(BD, nombreClase);

        if (baseVDM != null)
        {
            if (baseVDM.getClass().equals(DatosProy.class))
            {
                //es el datos del proyecto
                glb.dp = (DatosProy) baseVDM;
            }
            if (baseVDM.getClass().equals(Opciones.class))
            {
                //es el datos del proyecto
                glb.opc = (Opciones) baseVDM;
            }
        } else
        {
            System.out.println("*****************************************************************************************************************************************");
            System.out.println("Ha fallado el cargue de la clase " + nombreClase);
            System.out.println("*****************************************************************************************************************************************");
            
            return false;
        }

        return true;
        
    }

    /**
     * recupera de la base de datos todas las instancias que vengan de la clase
     * BaseVDM que en este conexto son todas las clases del programa
     */
    private static synchronized IBaseVDM recuperarElementosDeLaClase(String BD, String nombreClase)
    {

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory(BD);
        final EntityManager em = emf.createEntityManager();

        List<IBaseVDM> listaObjetos;

        try
        {
            System.out.println("*****************************************************************************************************************************************");
            System.out.println("cargando de la base de datos " + nombreClase);
            System.out.println("*****************************************************************************************************************************************");

            em.getTransaction().begin();

            String queryString = "SELECT p FROM " + nombreClase + " p";

            @SuppressWarnings(
                    {
                        "unchecked", "unchecked"
                    })
            TypedQuery<IBaseVDM> query = (TypedQuery<IBaseVDM>) em.createQuery(queryString);

            listaObjetos = query.getResultList();

            em.getTransaction().commit();

            //se recupera el item 0  que en teoria es el unico que hay
            return listaObjetos.get(0);

        } catch (Exception e)
        {

            System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            System.out.println("error ------ : --------" + e.getMessage());
            System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");

            return null;

        } finally
        {

            em.close();
            emf.close();

        }

    }

}

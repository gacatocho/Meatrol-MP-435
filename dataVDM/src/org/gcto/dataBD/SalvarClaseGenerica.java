/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gcto.dataBD;

import org.gcto.dataProyecto.DatosProy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.gcto.dataEnum.EEstado;
import org.gcto.dataOpciones.Opciones;
import org.gcto.dataGlobal.glb;
import org.gcto.interfases.IBaseVDM;

/**
 * salva una clase generica en la base de datos
 *
 * @author camilo
 */
public final class SalvarClaseGenerica
{

    /**
     * salva un objeto dado en la ruta del proyecto en glb.dp
     *
     * @param obj
     */
    public static void salvarObjeto(IBaseVDM obj) throws IOException
    {

        /**
         * el nombre de la clase que se va a salvar en el archivo
         * correspondiente
         */
        String NOMBRE_CLASE = null;

        /**
         * nombre de la sola base de datos
         */
        String nomBD = null;
        /**
         * nombre completo de la base de datos, ruta y nombre
         */
        String BD = null;

        //se recuper ala ruta para guardar la base de datos
        String rutaBD = null; 

        if (obj instanceof DatosProy)
        {
            nomBD = "datosProyecto";
            NOMBRE_CLASE = "DatosProy";
            rutaBD  = glb.dp.getRutaProyecto();

        }

        if (obj instanceof Opciones)
        {
            nomBD = "opciones";
            NOMBRE_CLASE = "Opciones";
            // Obtiene el directorio de datos de la aplicación de forma portable
            String userHome = System.getProperty("user.home");
            
            // Carpeta común "dataApplications" dentro del home del usuario
            Path carpeta = Paths.get(userHome, "dataApplications");
            
            // Crear la carpeta si no existe
            if (!Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }
            //se captura el path
            rutaBD = carpeta.toString();
            
        }
        //aqui ya sabemos la ruta, el nombre y el objeto a guardar

        if (nomBD != null)
        {
            BD = rutaBD + File.separator + nomBD+".odb";
        } else
        {
            System.out.println("Error en lo snombres de la base de datos");
            return;
        }

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory(BD);
        final EntityManager em = emf.createEntityManager();

        try
        {

            System.out.println("*****************************************************************************************************************************************");
            System.out.println(" ********************* Se guardara algo en la base de datos : " + BD);
            System.out.println("*****************************************************************************************************************************************");

            em.getTransaction().begin();

            if (obj.getEstado() != EEstado.SIN_CAMBIO)
            {
                EEstado estado = obj.getEstado();

                switch (estado)
                {
                    case NUEVO:
                        obj.setEstado(EEstado.SIN_CAMBIO);
                        System.out.println("*****************************************************************************************************************************************");
                        System.out.println("Persisitineod uno  nuevo   "); // + obj.getNombre());
                        System.out.println("*****************************************************************************************************************************************");
                        em.persist(obj);
                        break;

                    case EDITADO:
                        obj.setEstado(EEstado.SIN_CAMBIO);
                        IBaseVDM objeto = getObjetoPorClase(em, obj.getClass(), NOMBRE_CLASE);
                        if (objeto != null)
                        {
                            //copiaar todas las propiedades donde la fuente es el de lista actual y el
                            //destino es el de la base de datos

                            System.out.println("*****************************************************************************************************************************************");
                            System.out.println("El objeto encontrad para actualziar es "); //+ objeto.getNombre());
                            System.out.println("*****************************************************************************************************************************************");
                            obj.copy(objeto);
                        }
                        break;
                    case SIN_CAMBIO:
                        //no hace nada
                        break;
                    default:
                        throw new AssertionError(estado.name());
                }
                
                em.getTransaction().commit();

            }
            
            

        } catch (Exception e)
        {
            System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            System.out.println("Salvando en el trhread de " + nomBD + " : " + e.getMessage());
            System.out.println("////////////////////////////////////////*****************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");

        } finally
        {
            
            em.close();
            emf.close();
           
        }

    }

    /**
     * retorna una clase pues las bases deben ser de una sola clase
     *
     * @param em
     * @param aClass
     * @param nombreClase
     * @return
     */
    private static IBaseVDM getObjetoPorClase(EntityManager em, Class<? extends Object> aClass, String nombreClase)
    {
        IBaseVDM basevdm = null;

        String queryString = "SELECT c FROM " + nombreClase + " c";

        System.out.println("*****************************************************************************************************************************************");
        System.out.println("el quertyString para el ejemplo es  " + queryString);
        System.out.println("*****************************************************************************************************************************************");

        TypedQuery<IBaseVDM> query = em.createQuery(queryString, IBaseVDM.class);

        basevdm = query.getSingleResult();

        return basevdm;

    }

}

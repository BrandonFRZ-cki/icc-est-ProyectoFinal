package dao;

import models.AlgorithmResult;
import java.util.List;

/**
 * Interfaz DAO que define las operaciones de persistencia
 * para los resultados de algoritmos utilizados en la resolución de laberintos.
 */
public interface AlgorithmResultDAO {

    /**
     * Guarda un nuevo resultado de algoritmo.
     *
     * @param resultado Objeto AlgorithmResult que contiene el nombre del algoritmo,
     *                  el número de pasos y el tiempo de ejecución.
     */
    void guardar(AlgorithmResult resultado);

    /**
     * Lista todos los resultados almacenados.
     *
     * @return Lista de objetos AlgorithmResult guardados en el almacenamiento.
     */
    List<AlgorithmResult> listar();

    /**
     * Elimina todos los resultados almacenados.
     * Puede usarse para limpiar los datos antes de una nueva ejecución.
     */
    void limpiar();
}

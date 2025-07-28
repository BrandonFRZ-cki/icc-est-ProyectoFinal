package models;

import java.util.List;
import java.util.Set;

/**
 * Clase que encapsula los resultados obtenidos al resolver un laberinto.
 * Incluye el camino encontrado, las celdas visitadas, el tiempo de ejecución
 * y el nombre del algoritmo utilizado.
 */
public class SolveResults {

    private final List<Cell> camino;
    private final Set<Cell> visitadas;
    private long tiempo;        // Tiempo de ejecución en nanosegundos
    private String algoritmo;   // Nombre del algoritmo utilizado

    /**
     * Crea un objeto de resultados de resolución.
     *
     * @param camino Lista con el camino resuelto (de inicio a fin).
     * @param visitadas Conjunto de celdas visitadas durante la búsqueda.
     */
    public SolveResults(List<Cell> camino, Set<Cell> visitadas) {
        this.camino = camino;
        this.visitadas = visitadas;
    }

    /**
     * @return el camino encontrado por el algoritmo.
     */
    public List<Cell> getCamino() {
        return camino;
    }

    /**
     * @return conjunto de celdas que fueron visitadas (alias de getVisited()).
     */
    public Set<Cell> getVisitadas() {
        return visitadas;
    }

    /**
     * Alias requerido por el controlador.
     * @return el camino resuelto por el algoritmo.
     */
    public List<Cell> getPath() {
        return camino;
    }

    /**
     * Alias requerido por el controlador.
     * @return conjunto de celdas visitadas por el algoritmo.
     */
    public Set<Cell> getVisited() {
        return visitadas;
    }

    /**
     * @return tiempo de ejecución del algoritmo en nanosegundos.
     */
    public long getTiempo() {
        return tiempo;
    }

    /**
     * Define el tiempo de ejecución del algoritmo.
     * @param tiempo tiempo en nanosegundos.
     */
    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * @return nombre del algoritmo utilizado.
     */
    public String getAlgoritmo() {
        return algoritmo;
    }

    /**
     * Define el nombre del algoritmo utilizado.
     * @param algoritmo nombre del algoritmo.
     */
    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }
}

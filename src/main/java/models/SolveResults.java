package models;


import java.util.List;
import java.util.Set;

public class SolveResults {
    private List<Cell> camino;
    private Set<Cell> visitadas;
    private long tiempo;
    private String algoritmo;

    /**
     * Constructor con camino y celdas visitadas.
     *
     * @param camino Lista de celdas que representan el camino solucionado.
     * @param visitadas Conjunto de celdas visitadas durante la búsqueda.
     */
    public SolveResults(List<Cell> camino, Set<Cell> visitadas) {
        this.camino = camino;
        this.visitadas = visitadas;
    }

    /**
     * Devuelve el camino completo.
     */
    public List<Cell> getCamino() {
        return camino;
    }

    /**
     * Devuelve las celdas visitadas (alias).
     */
    public Set<Cell> getVisitadas() {
        return visitadas;
    }

    /**
     * Alias requerido por el controlador: camino solucionado.
     */
    public List<Cell> getPath() {
        return camino;
    }

    /**
     * Alias requerido por el controlador: celdas visitadas.
     */
    public Set<Cell> getVisited() {
        return visitadas;
    }

    /**
     * Tiempo que tomó el algoritmo.
     */
    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Nombre del algoritmo usado.
     */
    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }
}

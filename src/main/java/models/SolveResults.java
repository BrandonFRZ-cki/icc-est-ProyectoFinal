package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SolveResults {
    private List<List<Cell>> caminos;
    private List<Set<Cell>> visitados;

    public SolveResults() {
        this.caminos = new ArrayList<>();
        this.visitados = new ArrayList<>();
    }


    public void agregarResultado(List<Cell> camino, Set<Cell> celdasVisitadas) {
        this.caminos.add(camino);
        this.visitados.add(celdasVisitadas);
    }

    public List<List<Cell>> getCaminos() {
        return caminos;
    }

    public List<Set<Cell>> getVisitados() {
        return visitados;
    }
}
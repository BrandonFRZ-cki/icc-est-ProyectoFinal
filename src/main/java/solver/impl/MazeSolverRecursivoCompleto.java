package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MazeSolverRecursivoCompleto implements MazeSolver {
    private boolean[][] laberinto;
    private Cell destino;
    private List<Cell> camino;
    private Set<Cell> visitadas;

    public SolveResults resolver(boolean[][] laberinto, Cell inicio, Cell fin) {
        this.laberinto = laberinto;
        this.destino = fin;
        this.camino = new ArrayList<>();
        this.visitadas = new LinkedHashSet<>();

        buscar(inicio);

        SolveResults resultado = new SolveResults();
        resultado.agregarResultado(camino, visitadas);
        return resultado;
    }

    private boolean buscar(Cell actual) {
        if (!esValido(actual) || visitadas.contains(actual)) return false;

        visitadas.add(actual);
        camino.add(actual);

        if (actual.equals(destino)) return true;

        for (Cell vecino : obtenerVecinos(actual)) {
            if (buscar(vecino)) return true;
        }

        camino.remove(camino.size() - 1);
        return false;
    }

    private boolean esValido(Cell c) {
        int r = c.getRow();
        int col = c.getCol();
        return r >= 0 && col >= 0 && r < laberinto.length && col < laberinto[0].length && laberinto[r][col];
    }

    private List<Cell> obtenerVecinos(Cell c) {
        int[][] dirs = {{1,0},{0,1},{-1,0},{0,-1}};
        List<Cell> vecinos = new ArrayList<>();
        for (int[] d : dirs) {
            int r = c.getRow() + d[0];
            int col = c.getCol() + d[1];
            Cell vecino = new Cell(r, col);
            if (esValido(vecino)) {
                vecinos.add(vecino);
            }
        }
        return vecinos;
    }
}
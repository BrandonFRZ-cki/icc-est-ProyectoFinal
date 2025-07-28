package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MazeSolverRecursivo implements MazeSolver {
    private Set<Cell> visitadas;
    private List<Cell> camino;
    private boolean[][] laberinto;
    private Cell destino;

    public SolveResults resolver(boolean[][] laberinto, Cell inicio, Cell fin) {
        this.laberinto = laberinto;
        this.destino = fin;
        this.visitadas = new HashSet<>();
        this.camino = new ArrayList<>();

        backtrack(inicio);

        SolveResults resultado = new SolveResults();
        resultado.agregarResultado(camino, visitadas);
        return resultado;
    }

    private boolean backtrack(Cell actual) {
        if (!esValido(actual) || visitadas.contains(actual)) {
            return false;
        }
        visitadas.add(actual);
        camino.add(actual);

        if (actual.equals(destino)) {
            return true;
        }

        for (Cell vecino : obtenerVecinos(actual)) {
            if (backtrack(vecino)) {
                return true;
            }
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
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        List<Cell> vecinos = new ArrayList<>();
        for (int[] d : dirs) {
            int r = c.getRow() + d[0];
            int col = c.getCol() + d[1];
            Cell vecino = new Cell(r, col);
            if (esValido(vecino) && !visitadas.contains(vecino)) {
                vecinos.add(vecino);
            }
        }
        return vecinos;
    }
}
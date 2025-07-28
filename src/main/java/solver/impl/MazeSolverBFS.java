package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

public class MazeSolverBFS implements MazeSolver {
    public SolveResults resolver(boolean[][] laberinto, Cell inicio, Cell fin) {
        Queue<Cell> cola = new LinkedList<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Set<Cell> visitadas = new HashSet<>();
        cola.add(inicio);
        visitadas.add(inicio);

        while (!cola.isEmpty()) {
            Cell actual = cola.poll();
            if (actual.equals(fin)) break;
            for (Cell vecino : obtenerVecinos(actual, laberinto)) {
                if (!visitadas.contains(vecino)) {
                    cola.add(vecino);
                    visitadas.add(vecino);
                    cameFrom.put(vecino, actual);
                }
            }
        }

        List<Cell> camino = reconstruirCamino(cameFrom, inicio, fin);
        SolveResults resultado = new SolveResults();
        resultado.agregarResultado(camino, visitadas);
        return resultado;
    }

    private List<Cell> obtenerVecinos(Cell c, boolean[][] laberinto) {
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        List<Cell> vecinos = new ArrayList<>();
        for (int[] d : dirs) {
            int r = c.getRow() + d[0];
            int col = c.getCol() + d[1];
            if (r >= 0 && col >= 0 && r < laberinto.length && col < laberinto[0].length && laberinto[r][col]) {
                vecinos.add(new Cell(r, col));
            }
        }
        return vecinos;
    }

    private List<Cell> reconstruirCamino(Map<Cell, Cell> cameFrom, Cell inicio, Cell fin) {
        List<Cell> camino = new ArrayList<>();
        Cell actual = fin;
        while (actual != null && !actual.equals(inicio)) {
            camino.add(actual);
            actual = cameFrom.get(actual);
        }
        if (actual != null) camino.add(inicio);
        Collections.reverse(camino);
        return camino;
    }
}
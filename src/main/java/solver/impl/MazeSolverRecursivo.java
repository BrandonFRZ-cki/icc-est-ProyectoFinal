package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación recursiva básica para resolver un laberinto desde una celda de inicio hasta una de fin.
 * El algoritmo intenta ir en las 4 direcciones cardinales mientras marca celdas visitadas y construye el camino.
 */
public class MazeSolverRecursivo implements MazeSolver {

    private final List<Cell> path = new ArrayList<>();
    private final Set<Cell> visited = new HashSet<>();
    private boolean[][] grid;
    private Cell end;

    /**
     * Método principal que inicia la búsqueda del camino.
     *
     * @param maze matriz de booleanos (true: celda libre, false: muro)
     * @param start celda de inicio
     * @param end celda objetivo
     * @return objeto SolveResults con el camino y las celdas visitadas
     */
    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.grid = maze;
        this.end = end;
        path.clear();
        visited.clear();

        findPath(start);

        return new SolveResults(new ArrayList<>(path), new HashSet<>(visited));
    }

    /**
     * Busca recursivamente el camino desde la celda actual hasta el destino.
     *
     * @param current celda actual
     * @return true si se llegó al destino, false si no hay camino
     */
    private boolean findPath(Cell current) {
        int row = current.getRow();
        int col = current.getCol();

        // Validaciones de límites, paredes y visitados
        if (!isInBounds(row, col) || !grid[row][col] || visited.contains(current)) {
            return false;
        }

        visited.add(current);
        path.add(current);

        // Condición de éxito: se llega a la celda final
        if (current.equals(end)) {
            return true;
        }

        // Buscar en 4 direcciones (abajo, arriba, derecha, izquierda)
        if (findPath(new Cell(row + 1, col)) ||
                findPath(new Cell(row - 1, col)) ||
                findPath(new Cell(row, col + 1)) ||
                findPath(new Cell(row, col - 1))) {
            return true;
        }

        // Retroceso: se remueve del camino si no lleva al objetivo
        path.remove(path.size() - 1);
        return false;
    }

    /**
     * Verifica que una celda esté dentro del rango del laberinto.
     *
     * @param row fila
     * @param col columna
     * @return true si está dentro de los límites, false si no
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < grid.length &&
                col >= 0 && col < grid[0].length;
    }
}

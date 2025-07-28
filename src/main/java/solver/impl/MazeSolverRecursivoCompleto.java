package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

public class MazeSolverRecursivoCompleto implements MazeSolver {
    private final List<Cell> path = new ArrayList<>();
    private final Set<Cell> visited = new HashSet<>();
    private boolean[][] grid;
    private Cell end;

    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.grid = maze;
        this.end = end;
        path.clear();
        visited.clear();

        findPath(start);
        return new SolveResults(path, visited);
    }

    private boolean findPath(Cell current) {
        int row = current.getRow();
        int col = current.getCol();

        if (!isInBounds(row, col) || !grid[row][col] || visited.contains(current)) {
            return false;
        }

        visited.add(current);
        path.add(current);

        if (current.equals(end)) {
            return true;
        }

        // Recursión en 4 direcciones
        if (findPath(new Cell(row + 1, col)) || // abajo
                findPath(new Cell(row - 1, col)) || // arriba
                findPath(new Cell(row, col + 1)) || // derecha
                findPath(new Cell(row, col - 1))) { // izquierda
            return true;
        }

        // Retroceso si no hay camino
        path.remove(path.size() - 1);
        return false;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}
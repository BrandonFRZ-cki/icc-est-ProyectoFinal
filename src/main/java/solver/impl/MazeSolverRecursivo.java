package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

public class MazeSolverRecursivo implements MazeSolver {

    private final List<Cell> path = new ArrayList<>();
    private final List<Cell> ordenDeVisita = new ArrayList<>(); // para el paso a paso
    private boolean[][] grid;
    private Cell end;

    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.grid = maze;
        this.end = end;
        path.clear();
        ordenDeVisita.clear();

        findPath(start);
        return new SolveResults(new ArrayList<>(path), new LinkedHashSet<>(ordenDeVisita));
    }

    private boolean findPath(Cell current) {
        int row = current.getRow();
        int col = current.getCol();

        if (!isInBounds(row, col) || !grid[row][col]) return false;
        if (path.contains(current)) return false;

        ordenDeVisita.add(current);
        path.add(current);

        if (current.equals(end)) {
            return true;
        }

        // Intentar en las 4 direcciones
        if (findPath(new Cell(row + 1, col)) ||
                findPath(new Cell(row - 1, col)) ||
                findPath(new Cell(row, col + 1)) ||
                findPath(new Cell(row, col - 1))) {
            return true;
        }

        // Retroceder
        path.remove(path.size() - 1); // quita del camino si no sirve
        return false;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}


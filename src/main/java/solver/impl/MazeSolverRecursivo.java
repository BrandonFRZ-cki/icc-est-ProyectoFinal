package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MazeSolverRecursivo implements MazeSolver {
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

        boolean success = findPath(start);
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

        // Recursión solo hacia abajo y derecha
        if (findPath(new Cell(row + 1, col)) || findPath(new Cell(row, col + 1))) {
            return true;
        }

        // Retroceder si no se encuentra camino
        path.remove(path.size() - 1);
        return false;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}
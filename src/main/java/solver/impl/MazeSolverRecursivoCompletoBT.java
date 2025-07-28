package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MazeSolverRecursivoCompletoBT implements MazeSolver {
    private List<Cell> path;
    private Set<Cell> visited;
    private boolean[][] maze;
    private Cell end;

    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.maze = maze;
        this.end = end;
        this.path = new ArrayList<>();
        this.visited = new LinkedHashSet<>();
        findPath(start);
        return new SolveResults(path, visited);
    }

    private boolean findPath(Cell current) {
        int row = current.getRow();
        int col = current.getCol();

        if (!isValid(row, col) || visited.contains(current)) {
            return false;
        }

        visited.add(current);
        path.add(current);

        if (current.equals(end)) {
            return true;
        }

        // Movimiento en 4 direcciones
        if (findPath(new Cell(row + 1, col)) ||
                findPath(new Cell(row - 1, col)) ||
                findPath(new Cell(row, col + 1)) ||
                findPath(new Cell(row, col - 1))) {
            return true;
        }

        // Backtrack si no hay salida
        path.remove(path.size() - 1);
        return false;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < maze.length &&
                col >= 0 && col < maze[0].length &&
                maze[row][col];
    }
}
package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

public class MazeSolverDFS implements MazeSolver {

    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        int rows = maze.length;
        int cols = maze[0].length;

        List<Cell> path = new ArrayList<>();
        Set<Cell> visited = new LinkedHashSet<>();
        Stack<Cell> stack = new Stack<>();
        Map<Cell, Cell> parentMap = new HashMap<>();

        stack.push(start);
        visited.add(start);

        boolean found = false;

        while (!stack.isEmpty()) {
            Cell current = stack.pop();

            if (current.equals(end)) {
                found = true;
                break;
            }

            for (Cell neighbor : getNeighbors(current, rows, cols)) {
                if (!maze[neighbor.getRow()][neighbor.getCol()]) continue;
                if (visited.contains(neighbor)) continue;

                visited.add(neighbor);
                parentMap.put(neighbor, current);
                stack.push(neighbor);
            }
        }

        if (found) {
            Cell step = end;
            while (step != null) {
                path.add(0, step);
                step = parentMap.get(step);
            }
        }

        return new SolveResults(path, visited);
    }

    private List<Cell> getNeighbors(Cell cell, int rows, int cols) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();
        int c = cell.getCol();

        if (r > 0) neighbors.add(new Cell(r - 1, c));      // arriba
        if (r < rows - 1) neighbors.add(new Cell(r + 1, c)); // abajo
        if (c > 0) neighbors.add(new Cell(r, c - 1));      // izquierda
        if (c < cols - 1) neighbors.add(new Cell(r, c + 1)); // derecha

        return neighbors;
    }
}

package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

public class MazeSolverBFS implements MazeSolver {

    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> parentMap = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            if (current.equals(end)) break;

            for (Cell neighbor : getNeighbors(current, maze)) {
                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }

        List<Cell> path = buildPath(end, parentMap);
        return new SolveResults(path, visited);
    }

    private List<Cell> buildPath(Cell end, Map<Cell, Cell> parentMap) {
        List<Cell> path = new ArrayList<>();
        for (Cell at = end; at != null; at = parentMap.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    private List<Cell> getNeighbors(Cell cell, boolean[][] maze) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();
        int c = cell.getCol();
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < maze.length && nc >= 0 && nc < maze[0].length && maze[nr][nc]) {
                neighbors.add(new Cell(nr, nc));
            }
        }
        return neighbors;
    }
}

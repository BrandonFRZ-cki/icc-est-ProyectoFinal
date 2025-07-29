package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

public class MazeSolverRecursivoCompleto implements MazeSolver {

    private final List<Cell> path = new ArrayList<>();
    private final List<Cell> ordenDeVisita = new ArrayList<>(); // orden para paso a paso
    private boolean[][] grid;
    private Cell end;

    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.grid = maze;
        this.end = end;
        path.clear();
        ordenDeVisita.clear();

        boolean exito = findPath(start);

        if (exito) {
            return new SolveResults(new ArrayList<>(path), new LinkedHashSet<>(ordenDeVisita));
        } else {
            return null;
        }
    }

    private boolean findPath(Cell current) {
        int row = current.getRow();
        int col = current.getCol();

        // Validación de límites y obstáculos
        if (!isInBounds(row, col) || !grid[row][col]) return false;
        if (path.contains(current)) return false; // ya está en camino actual

        ordenDeVisita.add(current);  // registrar visita para animación
        path.add(current);           // intentar agregar al camino

        if (current.equals(end)) {
            return true;
        }

        // Explorar en las 4 direcciones
        if (findPath(new Cell(row + 1, col)) || // abajo
                findPath(new Cell(row - 1, col)) || // arriba
                findPath(new Cell(row, col + 1)) || // derecha
                findPath(new Cell(row, col - 1))) { // izquierda
            return true;
        }

        // Retroceso (backtracking)
        path.remove(path.size() - 1);
        return false;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}

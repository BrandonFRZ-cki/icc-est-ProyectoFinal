package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación del algoritmo recursivo completo con backtracking.
 * Intenta encontrar una única solución al laberinto desde la celda de inicio hasta la de fin,
 * registrando todas las celdas visitadas.
 */
public class MazeSolverRecursivoCompletoBT implements MazeSolver {

    private List<Cell> path;
    private Set<Cell> visited;
    private boolean[][] maze;
    private Cell end;

    /**
     * Resuelve el laberinto desde una celda inicial a una final.
     *
     * @param maze matriz de booleanos (true: camino libre, false: muro)
     * @param start celda de inicio
     * @param end celda objetivo
     * @return objeto SolveResults con el camino y celdas visitadas o null si no hay solución
     */
    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.maze = maze;
        this.end = end;
        this.path = new ArrayList<>();
        this.visited = new LinkedHashSet<>(); // Mantiene el orden de visita

        boolean found = findPath(start);

        if (found) {
            return new SolveResults(new ArrayList<>(path), new LinkedHashSet<>(visited));
        } else {
            return null;
        }
    }

    /**
     * Busca el camino de forma recursiva con retroceso.
     *
     * @param current celda actual a explorar
     * @return true si se encontró una ruta hasta el final
     */
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

        // Explorar en 4 direcciones
        if (findPath(new Cell(row + 1, col)) ||  // abajo
                findPath(new Cell(row - 1, col)) ||  // arriba
                findPath(new Cell(row, col + 1)) ||  // derecha
                findPath(new Cell(row, col - 1))) {  // izquierda
            return true;
        }

        // Retroceso si no se pudo continuar
        path.remove(path.size() - 1);
        return false;
    }

    /**
     * Verifica si una celda está dentro del laberinto y es transitable.
     *
     * @param row fila
     * @param col columna
     * @return true si es una celda válida y no es muro
     */
    private boolean isValid(int row, int col) {
        return row >= 0 && row < maze.length &&
                col >= 0 && col < maze[0].length &&
                maze[row][col];
    }
}

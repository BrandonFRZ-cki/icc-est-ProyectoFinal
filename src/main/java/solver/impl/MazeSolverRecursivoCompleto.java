package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación del algoritmo recursivo completo para encontrar un camino en el laberinto.
 * Si hay un camino del inicio al fin, lo encuentra usando backtracking.
 */
public class MazeSolverRecursivoCompleto implements MazeSolver {

    private final List<Cell> path = new ArrayList<>();
    private final Set<Cell> visited = new HashSet<>();
    private boolean[][] grid;
    private Cell end;

    /**
     * Resuelve el laberinto desde la celda de inicio hasta la de fin utilizando recursividad y backtracking.
     *
     * @param maze matriz de booleanos (true: celda libre, false: muro)
     * @param start celda de inicio
     * @param end celda objetivo
     * @return objeto SolveResults con el camino y las celdas visitadas, o null si no hay camino
     */
    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.grid = maze;
        this.end = end;
        path.clear();
        visited.clear();

        boolean exito = findPath(start);

        if (exito) {
            // Se retorna una copia del camino y visitados
            return new SolveResults(new ArrayList<>(path), new HashSet<>(visited));
        } else {
            // No se pudo encontrar un camino
            return null;
        }
    }

    /**
     * Busca recursivamente el camino desde la celda actual al destino.
     *
     * @param current celda actual a explorar
     * @return true si se encuentra un camino hasta el final, false si no
     */
    private boolean findPath(Cell current) {
        int row = current.getRow();
        int col = current.getCol();

        // Verificación de límites, paredes y ya visitados
        if (!isInBounds(row, col) || !grid[row][col] || visited.contains(current)) {
            return false;
        }

        visited.add(current);
        path.add(current);

        // Éxito: se llegó a la celda final
        if (current.equals(end)) {
            return true;
        }

        // Explorar en las 4 direcciones posibles
        if (findPath(new Cell(row + 1, col)) || // Abajo
                findPath(new Cell(row - 1, col)) || // Arriba
                findPath(new Cell(row, col + 1)) || // Derecha
                findPath(new Cell(row, col - 1))) { // Izquierda
            return true;
        }

        // Retroceso si no hay salida en esta dirección
        path.remove(path.size() - 1);
        return false;
    }

    /**
     * Verifica que una celda esté dentro de los límites del laberinto.
     *
     * @param row fila
     * @param col columna
     * @return true si está dentro de los límites y no es un muro
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < grid.length &&
                col >= 0 && col < grid[0].length;
    }
}

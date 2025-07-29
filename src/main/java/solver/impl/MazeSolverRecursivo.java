package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación de un algoritmo recursivo simple para encontrar un camino en un laberinto.
 * Utiliza DFS recursivo y no garantiza el camino más corto.
 */
public class MazeSolverRecursivo implements MazeSolver {

    private final List<Cell> path = new ArrayList<>();              // Lista para almacenar el camino encontrado
    private final List<Cell> ordenDeVisita = new ArrayList<>();    // Lista para registrar el orden en que se visitan las celdas
    private boolean[][] grid;  // Referencia al laberinto como matriz booleana
    private Cell end;          // Celda destino (fin del laberinto)

    /**
     * Resuelve el laberinto desde una celda de inicio hasta una celda de fin.
     *
     * @param maze matriz de booleanos (true: camino, false: muro)
     * @param start celda inicial
     * @param end celda destino
     * @return objeto SolveResults con el camino y las celdas visitadas
     */
    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.grid = maze;       // Asigna el laberinto a variable local
        this.end = end;         // Define la celda objetivo
        path.clear();           // Limpia la lista del camino
        ordenDeVisita.clear();  // Limpia la lista de visitados

        findPath(start);        // Inicia la búsqueda recursiva desde la celda inicial
        return new SolveResults(new ArrayList<>(path), new LinkedHashSet<>(ordenDeVisita));
    }

    /**
     * Función recursiva para buscar el camino hacia la celda final.
     *
     * @param current celda actual en la recursión
     * @return true si se encontró el camino desde esta celda, false si hay que retroceder
     */
    private boolean findPath(Cell current) {
        int row = current.getRow();  // fila actual
        int col = current.getCol();  // columna actual

        // Caso base: si está fuera del laberinto o es un muro → no seguir
        if (!isInBounds(row, col) || !grid[row][col]) return false;

        // Si ya fue visitada (está en el camino actual) → no repetir
        if (path.contains(current)) return false;

        ordenDeVisita.add(current);  // Se registra como visitada para visualización
        path.add(current);           // Se añade al camino actual

        // Si llegamos a la meta → éxito
        if (current.equals(end)) {
            return true;
        }

        // Llamadas recursivas en las 4 direcciones (abajo, arriba, derecha, izquierda)
        if (findPath(new Cell(row + 1, col)) ||  // abajo
                findPath(new Cell(row - 1, col)) ||  // arriba
                findPath(new Cell(row, col + 1)) ||  // derecha
                findPath(new Cell(row, col - 1))) {  // izquierda
            return true; // Si alguna devuelve true, se mantiene en el camino
        }

        // Si no hay camino por aquí → retroceder (backtracking)
        path.remove(path.size() - 1); // se quita del camino
        return false;
    }

    /**
     * Verifica si una posición está dentro de los límites del laberinto.
     *
     * @param row fila a verificar
     * @param col columna a verificar
     * @return true si la posición está dentro del laberinto, false si está fuera
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}

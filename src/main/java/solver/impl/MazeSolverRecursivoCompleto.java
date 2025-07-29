package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación de un algoritmo de búsqueda recursivo completo en 4 direcciones.
 * Utiliza backtracking para encontrar un camino desde una celda de inicio hasta una celda final.
 */
public class MazeSolverRecursivoCompleto implements MazeSolver {

    // Lista que almacena el camino actual desde el inicio hasta la meta
    private final List<Cell> path = new ArrayList<>();

    // Lista que registra el orden en que se visitan las celdas (para animación paso a paso)
    private final List<Cell> ordenDeVisita = new ArrayList<>();

    // Matriz booleana que representa el laberinto: true = camino, false = muro
    private boolean[][] grid;

    // Celda final a la que se quiere llegar
    private Cell end;

    /**
     * Método principal que resuelve el laberinto con recursión completa.
     *
     * @param maze  matriz de booleanos representando el laberinto
     * @param start celda inicial
     * @param end   celda final
     * @return objeto SolveResults con el camino y las celdas visitadas, o null si no hay solución
     */
    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.grid = maze;            // Asignamos el laberinto a la variable local
        this.end = end;             // Definimos la meta
        path.clear();               // Limpiamos el camino anterior
        ordenDeVisita.clear();      // Limpiamos la lista de visitados

        boolean exito = findPath(start); // Llamada al método recursivo

        if (exito) {
            // Si se encontró camino, lo retornamos junto con el orden de visita
            return new SolveResults(new ArrayList<>(path), new LinkedHashSet<>(ordenDeVisita));
        } else {
            return null; // No se encontró solución
        }
    }

    /**
     * Método recursivo para buscar un camino desde la celda actual hasta la celda final.
     *
     * @param current celda actual en la exploración
     * @return true si se encontró camino hacia la meta desde esta celda
     */
    private boolean findPath(Cell current) {
        int row = current.getRow(); // Fila actual
        int col = current.getCol(); // Columna actual

        // Si está fuera de los límites o es un muro → no se puede continuar
        if (!isInBounds(row, col) || !grid[row][col]) return false;

        // Si ya se está en el camino actual → evitar ciclos
        if (path.contains(current)) return false;

        ordenDeVisita.add(current); // Registrar visita para visualización
        path.add(current);          // Agregar al camino actual

        // Si llegamos al destino → éxito
        if (current.equals(end)) {
            return true;
        }

        // Intentar avanzar en 4 direcciones (abajo, arriba, derecha, izquierda)
        if (findPath(new Cell(row + 1, col)) ||     // abajo
                findPath(new Cell(row - 1, col)) ||     // arriba
                findPath(new Cell(row, col + 1)) ||     // derecha
                findPath(new Cell(row, col - 1))) {     // izquierda
            return true; // Si alguna de las direcciones retorna true, se mantiene el camino
        }

        // Si no hay camino → retroceder (backtracking)
        path.remove(path.size() - 1);
        return false;
    }

    /**
     * Verifica si una celda está dentro de los límites del laberinto.
     *
     * @param row fila
     * @param col columna
     * @return true si está dentro del rango, false si está fuera
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}

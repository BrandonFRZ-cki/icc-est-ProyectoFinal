package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementación de un algoritmo recursivo completo con backtracking.
 * Explora en 4 direcciones para encontrar un camino desde una celda inicial hasta una celda final.
 * Registra el camino encontrado y las celdas visitadas.
 */
public class MazeSolverRecursivoCompletoBT implements MazeSolver {

    // Lista para guardar el camino actual desde el inicio hasta el fin
    private List<Cell> path;

    // Conjunto para registrar las celdas que ya han sido visitadas
    private Set<Cell> visited;

    // Matriz booleana que representa el laberinto (true = camino, false = muro)
    private boolean[][] maze;

    // Celda destino
    private Cell end;

    /**
     * Método principal que resuelve el laberinto con recursión + retroceso (backtracking).
     *
     * @param maze  matriz del laberinto
     * @param start celda de inicio
     * @param end   celda de destino
     * @return objeto SolveResults con el camino y las celdas visitadas, o null si no hay camino
     */
    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        this.maze = maze;               // Guardamos la matriz de entrada
        this.end = end;                 // Guardamos la celda objetivo
        this.path = new ArrayList<>();  // Inicializamos la lista de camino
        this.visited = new LinkedHashSet<>(); // Inicializamos el conjunto de visitados

        boolean found = findPath(start); // Iniciamos la búsqueda recursiva

        if (found) {
            // Si se encontró el camino, retornamos el resultado con camino y visitas
            return new SolveResults(new ArrayList<>(path), new LinkedHashSet<>(visited));
        } else {
            return null; // Si no se encontró camino, retornamos null
        }
    }

    /**
     * Método recursivo para explorar el laberinto usando backtracking.
     *
     * @param current celda actual desde donde se explora
     * @return true si se encuentra un camino válido al destino
     */
    private boolean findPath(Cell current) {
        int row = current.getRow(); // Obtener fila de la celda actual
        int col = current.getCol(); // Obtener columna de la celda actual

        // Validaciones: fuera de límites, celda ya visitada o bloqueada
        if (!isValid(row, col) || visited.contains(current)) {
            return false;
        }

        visited.add(current); // Registrar la celda como visitada
        path.add(current);    // Agregar la celda al camino actual

        if (current.equals(end)) {
            return true; // Si llegamos al destino, éxito
        }

        // Exploramos en las 4 direcciones posibles
        if (findPath(new Cell(row + 1, col)) ||  // Abajo
                findPath(new Cell(row - 1, col)) ||  // Arriba
                findPath(new Cell(row, col + 1)) ||  // Derecha
                findPath(new Cell(row, col - 1))) {  // Izquierda
            return true;
        }

        // Retroceso: eliminamos la celda del camino si no lleva a la solución
        path.remove(path.size() - 1);
        return false;
    }

    /**
     * Verifica si una celda es válida: dentro del rango y no es un muro.
     *
     * @param row fila
     * @param col columna
     * @return true si es válida, false si es inválida
     */
    private boolean isValid(int row, int col) {
        return row >= 0 && row < maze.length &&
                col >= 0 && col < maze[0].length &&
                maze[row][col]; // Debe ser una celda transitable (true)
    }
}

package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación del algoritmo Depth-First Search (DFS) para resolución de laberintos.
 * Este algoritmo explora lo más profundo posible antes de retroceder.
 */
public class MazeSolverDFS implements MazeSolver {

    /**
     * Método que resuelve el laberinto usando búsqueda en profundidad (DFS).
     *
     * @param maze matriz booleana del laberinto (true = camino, false = muro).
     * @param start celda de inicio.
     * @param end celda de fin.
     * @return SolveResults con el camino y las celdas visitadas.
     */
    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        int rows = maze.length;            // número de filas
        int cols = maze[0].length;         // número de columnas

        List<Cell> path = new ArrayList<>();           // lista del camino final
        Set<Cell> visited = new LinkedHashSet<>();     // celdas visitadas (en orden)
        Stack<Cell> stack = new Stack<>();             // pila para DFS
        Map<Cell, Cell> parentMap = new HashMap<>();   // mapa de padres para reconstruir el camino

        stack.push(start);      // agrega la celda de inicio a la pila
        visited.add(start);     // marca la celda como visitada

        boolean found = false;  // bandera para saber si se encontró el camino

        // Bucle principal DFS
        while (!stack.isEmpty()) {
            Cell current = stack.pop();  // obtiene la celda del tope de la pila

            if (current.equals(end)) {   // si llegamos al final
                found = true;
                break;
            }

            // Explora los vecinos de la celda actual
            for (Cell neighbor : getNeighbors(current, rows, cols)) {
                if (!maze[neighbor.getRow()][neighbor.getCol()]) continue;  // si es muro, lo ignora
                if (visited.contains(neighbor)) continue;                  // si ya fue visitado, lo ignora

                visited.add(neighbor);               // marca como visitado
                parentMap.put(neighbor, current);    // guarda el "padre" de esa celda
                stack.push(neighbor);                // añade a la pila para seguir explorando
            }
        }

        // Si se encontró el final, reconstruye el camino
        if (found) {
            Cell step = end;
            while (step != null) {
                path.add(0, step);              // añade cada celda desde el final hacia el inicio
                step = parentMap.get(step);     // retrocede hacia el padre
            }
        }

        // Devuelve el resultado (camino y celdas exploradas)
        return new SolveResults(path, visited);
    }

    /**
     * Obtiene las celdas vecinas (arriba, abajo, izquierda, derecha) dentro de los límites.
     *
     * @param cell celda actual.
     * @param rows total de filas del laberinto.
     * @param cols total de columnas del laberinto.
     * @return lista de celdas vecinas válidas.
     */
    private List<Cell> getNeighbors(Cell cell, int rows, int cols) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();  // fila actual
        int c = cell.getCol();  // columna actual

        if (r > 0) neighbors.add(new Cell(r - 1, c));      // vecino de arriba
        if (r < rows - 1) neighbors.add(new Cell(r + 1, c)); // vecino de abajo
        if (c > 0) neighbors.add(new Cell(r, c - 1));      // vecino izquierdo
        if (c < cols - 1) neighbors.add(new Cell(r, c + 1)); // vecino derecho

        return neighbors;
    }
}

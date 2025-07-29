package solver.impl;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

import java.util.*;

/**
 * Implementación del algoritmo Breadth-First Search (BFS) para resolver laberintos.
 * Explora el laberinto por niveles, garantizando el camino más corto en número de pasos.
 */
public class MazeSolverBFS implements MazeSolver {

    /**
     * Método principal que resuelve el laberinto desde una celda de inicio hasta una celda de fin.
     *
     * @param maze matriz booleana que representa el laberinto (true = camino libre, false = muro).
     * @param start celda de inicio.
     * @param end celda de fin.
     * @return objeto SolveResults con el camino encontrado y las celdas visitadas.
     */
    @Override
    public SolveResults solve(boolean[][] maze, Cell start, Cell end) {
        Queue<Cell> queue = new LinkedList<>();          // Cola para BFS
        Map<Cell, Cell> parentMap = new HashMap<>();     // Para reconstruir el camino
        Set<Cell> visited = new LinkedHashSet<>();       // Celdas visitadas en orden

        queue.offer(start);     // Añade la celda inicial a la cola
        visited.add(start);     // Marca como visitada

        boolean found = false;  // Bandera para indicar si se encontró el final

        // Bucle principal BFS
        while (!queue.isEmpty()) {
            Cell current = queue.poll();  // Extrae la siguiente celda de la cola

            if (current.equals(end)) {    // Si se llegó al destino
                found = true;
                break;
            }

            // Se exploran los vecinos de la celda actual
            for (Cell neighbor : getNeighbors(current, maze)) {
                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);                 // Se añade a la cola
                    visited.add(neighbor);                 // Se marca como visitado
                    parentMap.put(neighbor, current);      // Se guarda de dónde vino
                }
            }
        }

        // Si se encontró el destino, se reconstruye el camino. Si no, devuelve lista vacía.
        List<Cell> path = found ? buildPath(end, parentMap) : Collections.emptyList();

        return new SolveResults(path, visited); // Devuelve el resultado con camino y visitados
    }

    /**
     * Reconstruye el camino desde el nodo final hasta el inicial usando el mapa de padres.
     *
     * @param end nodo final (celda de llegada).
     * @param parentMap mapa que contiene de dónde vino cada celda.
     * @return lista ordenada del camino desde el inicio hasta el fin.
     */
    private List<Cell> buildPath(Cell end, Map<Cell, Cell> parentMap) {
        List<Cell> path = new ArrayList<>();
        for (Cell at = end; at != null; at = parentMap.get(at)) {
            path.add(at);  // Se agrega la celda actual
        }
        Collections.reverse(path); // Se invierte para que vaya de inicio a fin
        return path;
    }

    /**
     * Devuelve la lista de vecinos válidos (caminables) desde la celda actual.
     *
     * @param cell celda actual.
     * @param maze laberinto representado como matriz booleana.
     * @return lista de celdas vecinas caminables.
     */
    private List<Cell> getNeighbors(Cell cell, boolean[][] maze) {
        List<Cell> neighbors = new ArrayList<>();
        int r = cell.getRow();  // Fila actual
        int c = cell.getCol();  // Columna actual

        // Direcciones: arriba, abajo, izquierda, derecha (4 direcciones cardinales)
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1]; // Nueva fila y columna
            // Verifica que esté dentro del laberinto y que no sea muro
            if (nr >= 0 && nr < maze.length && nc >= 0 && nc < maze[0].length && maze[nr][nc]) {
                neighbors.add(new Cell(nr, nc));  // Se agrega como vecino válido
            }
        }

        return neighbors;
    }
}

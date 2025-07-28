package solver;

import models.Cell;
import models.SolveResults;

/**
 * Interfaz base para los algoritmos de resolución de laberintos.
 */
public interface MazeSolver {

    /**
     * Resuelve el laberinto desde la celda de inicio hasta la celda de fin.
     *
     * @param maze Matriz booleana del laberinto (true = camino, false = muro).
     * @param start Celda de inicio.
     * @param end Celda de fin.
     * @return Objeto SolveResults con el camino encontrado y las celdas visitadas, o null si no hay solución.
     */
    SolveResults solve(boolean[][] maze, Cell start, Cell end);
}

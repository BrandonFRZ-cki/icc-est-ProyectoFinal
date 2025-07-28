package solver;

import models.Cell;
import models.SolveResults;

public interface MazeSolver {
    SolveResults resolver(boolean[][] laberinto, Cell inicio, Cell fin);
}
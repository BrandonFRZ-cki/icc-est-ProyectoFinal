package solver;

import models.Cell;
import models.SolveResults;

public interface MazeSolver {
    SolveResults solve(boolean[][] maze, Cell start, Cell end);
}

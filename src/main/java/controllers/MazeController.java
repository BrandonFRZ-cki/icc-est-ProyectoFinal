package controllers;

import models.Cell;
import models.SolveResults;
import solver.MazeSolver;

public class MazeController {
    private MazeSolver solucionador;

    public MazeController(MazeSolver solucionador) {
        this.solucionador = solucionador;
    }

    public SolveResults resolverLaberinto(boolean[][] laberinto, Cell inicio, Cell fin) {
        return solucionador.resolver(laberinto, inicio, fin);
    }

    public void setSolucionador(MazeSolver nuevoSolucionador) {
        this.solucionador = nuevoSolucionador;
    }
}
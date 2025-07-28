package controllers;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;
import models.Cell;
import models.CellState;
import models.SolveResults;
import solver.MazeSolver;
import solver.impl.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MazeController {

    private final AlgorithmResultDAO dao;
    private final Map<String, MazeSolver> algoritmos;

    public MazeController(AlgorithmResultDAO dao) {
        this.dao = dao;
        this.algoritmos = new HashMap<>();
        inicializarAlgoritmos();
    }

    private void inicializarAlgoritmos() {
        algoritmos.put("Recursivo 2D", new MazeSolverRecursivo());
        algoritmos.put("Recursivo 4D", new MazeSolverRecursivoCompleto());
        algoritmos.put("Recursivo 4D BT", new MazeSolverRecursivoCompletoBT());
        algoritmos.put("BFS", new MazeSolverBFS());
        algoritmos.put("DFS", new MazeSolverDFS());
    }

    public SolveResults resolverLaberinto(String algoritmoNombre, CellState[][] estados, Cell inicio, Cell fin) {
        MazeSolver solver = algoritmos.get(algoritmoNombre);
        if (solver == null) return null;

        boolean[][] maze = convertirAMatrizBooleana(estados);
        long inicioTiempo = System.nanoTime();
        SolveResults resultado = solver.solve(maze, inicio, fin);
        long tiempoTotal = System.nanoTime() - inicioTiempo;

        if (resultado != null && resultado.getPath() != null) {
            resultado.setTiempo(tiempoTotal);
            resultado.setAlgoritmo(algoritmoNombre);
            dao.guardar(new AlgorithmResult(algoritmoNombre, resultado.getPath().size(), tiempoTotal));
        }

        return resultado;
    }

    private boolean[][] convertirAMatrizBooleana(CellState[][] estados) {
        int filas = estados.length;
        int columnas = estados[0].length;
        boolean[][] matriz = new boolean[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = estados[i][j] != CellState.WALL;
            }
        }

        return matriz;
    }

    public List<AlgorithmResult> obtenerResultados() {
        return dao.listar();
    }

    public void limpiarResultados() {
        dao.limpiar();
    }

    public Map<String, MazeSolver> getAlgoritmos() {
        return algoritmos;
    }
    // Agrega al final de la clase MazeController:

    public void resolver(views.MazePanel panel, String algoritmoNombre) {
        SolveResults resultado = resolverLaberinto(
                algoritmoNombre,
                panel.getCellStates(),
                panel.getStartCell(),
                panel.getEndCell()
        );

        if (resultado != null) {
            panel.clearPathAndVisited();
            panel.setVisited(resultado.getVisited());
            panel.setPath(resultado.getPath());
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "No se pudo encontrar un camino.");
        }
    }

    public void paso(views.MazePanel panel, String algoritmoNombre) {
        SolveResults resultado = resolverLaberinto(
                algoritmoNombre,
                panel.getCellStates(),
                panel.getStartCell(),
                panel.getEndCell()
        );

        if (resultado != null) {
            panel.clearPathAndVisited();
            panel.setVisited(resultado.getVisited());  // Solo muestra nodos visitados
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "No se pudo encontrar un camino.");
        }
    }

    public void nuevoLaberinto() {
        try {
            int filas = Integer.parseInt(javax.swing.JOptionPane.showInputDialog("Número de filas:"));
            int columnas = Integer.parseInt(javax.swing.JOptionPane.showInputDialog("Número de columnas:"));
            views.MazeFrame nuevo = new views.MazeFrame(filas, columnas, this);
            nuevo.setVisible(true);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Entrada inválida");
        }
    }

    public void mostrarResultados(javax.swing.JFrame parent) {
        views.ResultadosDialog dialog = new views.ResultadosDialog(parent, dao);
        dialog.setVisible(true);
    }

}

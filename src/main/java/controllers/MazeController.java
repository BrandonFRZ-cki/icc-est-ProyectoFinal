package controllers;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;
import models.Cell;
import models.CellState;
import models.SolveResults;
import solver.MazeSolver;
import solver.impl.*;

import views.MazeFrame;
import views.MazePanel;
import views.ResultadosDialog;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MazeController {
    private final Map<String, MazeSolver> algoritmos = new HashMap<>();
    private final AlgorithmResultDAO dao;

    public MazeController(AlgorithmResultDAO dao) {
        this.dao = dao;
        algoritmos.put("Recursivo", new MazeSolverRecursivo());
        algoritmos.put("Recursivo Completo", new MazeSolverRecursivoCompleto());
        algoritmos.put("Recursivo Completo BT", new MazeSolverRecursivoCompletoBT());
        algoritmos.put("BFS", new MazeSolverBFS());
        algoritmos.put("DFS", new MazeSolverDFS());
    }

    public Map<String, MazeSolver> getAlgoritmos() {
        return algoritmos;
    }

    /**
     * Método principal que se llama cuando el usuario presiona el botón "Resolver".
     */
    public void resolver(MazePanel panel, String algoritmoNombre) {
        Cell start = panel.getStartCell();
        Cell end = panel.getEndCell();
        CellState[][] estados = panel.getCellStates();

        if (start == null || end == null) {
            JOptionPane.showMessageDialog(null, "Debes establecer un punto de inicio (verde) y fin (rojo).");
            return;
        }

        // Convertir CellState[][] a boolean[][]
        boolean[][] maze = new boolean[estados.length][estados[0].length];
        for (int i = 0; i < estados.length; i++) {
            for (int j = 0; j < estados[0].length; j++) {
                maze[i][j] = estados[i][j] != CellState.WALL;
            }
        }

        MazeSolver solver = algoritmos.get(algoritmoNombre);
        SolveResults resultados = solver.solve(maze, start, end);

        panel.clearPathAndVisited();  // Limpiar anteriores
        panel.setVisited(resultados.getVisited());  // Pintar gris
        panel.repaint();

        // Pausa para visualización de celdas visitadas antes del camino
        try {
            Thread.sleep(200); // milisegundos
        } catch (InterruptedException ignored) {}

        List<Cell> path = resultados.getPath();
        if (path.isEmpty() || !path.contains(end)) {
            JOptionPane.showMessageDialog(null, "No se encontró un camino hasta el objetivo.");
            return;
        }

        panel.setPath(path);  // Pintar camino

        // Guardar resultado
        dao.guardar(new AlgorithmResult(algoritmoNombre, path.size(), System.nanoTime())); // Guardamos con tiempo actual
    }

    /**
     * Método paso a paso. (Puede quedar como idea futura, aquí solo lanzamos alerta).
     */
    public void paso(MazePanel panel, String algoritmo) {
        JOptionPane.showMessageDialog(null, "Funcionalidad paso a paso aún no implementada.");
    }

    /**
     * Reinicia el laberinto (solicita nuevo tamaño).
     */
    public void nuevoLaberinto() {
        SwingUtilities.invokeLater(() -> {
            int filas = pedirNumero("Ingrese nuevas filas:", 5);
            int cols = pedirNumero("Ingrese nuevas columnas:", 5);
            MazeFrame nuevo = new MazeFrame(filas, cols, this);
            nuevo.setVisible(true);
        });
    }

    /**
     * Muestra ventana con tabla de resultados.
     */
    public void mostrarResultados(JFrame parent) {
        ResultadosDialog dialog = new ResultadosDialog(parent, dao);
        dialog.setVisible(true);
    }

    /**
     * Pide número mediante JOptionPane con mínimo.
     */
    private int pedirNumero(String mensaje, int minimo) {
        int valor = -1;
        while (valor < minimo) {
            String input = JOptionPane.showInputDialog(null, mensaje);
            if (input == null) System.exit(0);
            try {
                valor = Integer.parseInt(input);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Valor inválido.");
            }
        }
        return valor;
    }
}

package controllers;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;
import models.Cell;
import models.CellState;
import models.SolveResults;
import solver.MazeSolver;
import solver.impl.*;
import views.MazePanel;

import javax.swing.*;
import java.util.*;

/**
 * Controlador principal del sistema. Se encarga de gestionar los algoritmos de resolución,
 * coordinar la lógica entre el modelo (datos), la vista (interfaz) y el almacenamiento (DAO).
 */
public class MazeController {

    // Objeto DAO para guardar y recuperar resultados
    private final AlgorithmResultDAO dao;

    // Mapa que asocia nombres de algoritmos con sus implementaciones
    private final Map<String, MazeSolver> algoritmos;

    // Lista de celdas visitadas para modo paso a paso
    private List<Cell> pasoAPasoCeldas;

    // Índice actual del paso en el modo paso a paso
    private int pasoActual = 0;

    // Camino final encontrado por el algoritmo
    private List<Cell> caminoEncontrado;

    /**
     * Constructor que recibe el DAO y configura los algoritmos disponibles.
     */
    public MazeController(AlgorithmResultDAO dao) {
        this.dao = dao;
        this.algoritmos = new HashMap<>();
        inicializarAlgoritmos(); // inicializa los algoritmos
    }

    /**
     * Registra los algoritmos disponibles en el sistema.
     */
    private void inicializarAlgoritmos() {
        algoritmos.put("Recursivo 2D", new MazeSolverRecursivo());
        algoritmos.put("Recursivo 4D", new MazeSolverRecursivoCompleto());
        algoritmos.put("Recursivo 4D BT", new MazeSolverRecursivoCompletoBT());
        algoritmos.put("BFS", new MazeSolverBFS());
        algoritmos.put("DFS", new MazeSolverDFS());
    }

    /**
     * Lógica principal para resolver el laberinto usando el algoritmo especificado.
     * Registra el tiempo que tarda y guarda el resultado si fue exitoso.
     */
    public SolveResults resolverLaberinto(String algoritmoNombre, CellState[][] estados, Cell inicio, Cell fin) {
        if (inicio == null || fin == null) return null;

        MazeSolver solver = algoritmos.get(algoritmoNombre);
        if (solver == null) return null;

        // Convierte la matriz de celdas a matriz booleana
        boolean[][] maze = convertirAMatrizBooleana(estados);

        long inicioTiempo = System.nanoTime(); // mide tiempo de inicio
        SolveResults resultado = solver.solve(maze, inicio, fin); // ejecuta algoritmo
        long tiempoTotal = System.nanoTime() - inicioTiempo; // mide tiempo final

        // Guarda resultado si es válido
        if (resultado != null && resultado.getPath() != null && !resultado.getPath().isEmpty()) {
            resultado.setTiempo(tiempoTotal);
            resultado.setAlgoritmo(algoritmoNombre);
            dao.guardar(new AlgorithmResult(algoritmoNombre, resultado.getPath().size(), tiempoTotal));
        }

        return resultado;
    }

    /**
     * Convierte la matriz de estados (CellState) a una booleana (true si es transitable).
     */
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

    // Devuelve la lista de resultados guardados
    public List<AlgorithmResult> obtenerResultados() {
        return dao.listar();
    }

    // Elimina todos los resultados guardados
    public void limpiarResultados() {
        dao.limpiar();
    }

    // Devuelve el mapa de algoritmos disponibles
    public Map<String, MazeSolver> getAlgoritmos() {
        return algoritmos;
    }

    /**
     * Ejecuta el algoritmo completo y actualiza el panel con el resultado.
     */
    public void resolver(views.MazePanel panel, String algoritmoNombre) {
        SolveResults resultado = resolverLaberinto(
                algoritmoNombre,
                panel.getCellStates(),
                panel.getStartCell(),
                panel.getEndCell()
        );

        panel.clearPathAndVisited(); // limpia el panel antes de pintar

        if (resultado != null && resultado.getPath() != null && !resultado.getPath().isEmpty()) {
            panel.setVisited(resultado.getVisited()); // muestra nodos visitados
            panel.setPath(resultado.getPath()); // muestra el camino final
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar un camino.");
        }
    }

    /**
     * Ejecuta el algoritmo de forma animada, mostrando paso a paso los nodos explorados
     * y luego el camino final encontrado.
     */
    public void resolverAnimado(MazePanel panel, String algoritmoNombre) {
        SolveResults resultado = resolverLaberinto(
                algoritmoNombre,
                panel.getCellStates(),
                panel.getStartCell(),
                panel.getEndCell()
        );

        panel.clearPathAndVisited(); // limpia antes de animar

        if (resultado == null || resultado.getPath() == null || resultado.getPath().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar un camino.");
            return;
        }

        List<Cell> visitadas = new ArrayList<>(resultado.getVisited());
        List<Cell> camino = new ArrayList<>(resultado.getPath());

        // Hilo para animación visual (visitas + camino)
        new Thread(() -> {
            try {
                for (Cell v : visitadas) {
                    Thread.sleep(10); // pausa breve
                    SwingUtilities.invokeLater(() -> panel.setVisited(Set.of(v)));
                }

                for (Cell c : camino) {
                    Thread.sleep(40); // pausa mayor
                    SwingUtilities.invokeLater(() -> panel.setPath(List.of(c)));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Muestra paso a paso las celdas visitadas. Al finalizar muestra el camino final.
     */
    public void paso(MazePanel panel, String algoritmoNombre) {
        // Si es la primera vez que se llama
        if (pasoAPasoCeldas == null) {
            SolveResults resultado = resolverLaberinto(
                    algoritmoNombre,
                    panel.getCellStates(),
                    panel.getStartCell(),
                    panel.getEndCell()
            );

            if (resultado == null || resultado.getVisited() == null || resultado.getVisited().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se pudo encontrar un camino.");
                return;
            }

            // Inicializa datos del paso a paso
            pasoAPasoCeldas = new ArrayList<>(resultado.getVisited());
            caminoEncontrado = new ArrayList<>(resultado.getPath());
            pasoActual = 0;
            panel.clearPathAndVisited();
        }

        // Muestra una celda visitada por paso
        if (pasoActual < pasoAPasoCeldas.size()) {
            Cell siguiente = pasoAPasoCeldas.get(pasoActual++);
            panel.setVisited(Set.of(siguiente));
        } else {
            // Cuando termina, muestra camino final y resetea
            panel.setPath(caminoEncontrado);
            JOptionPane.showMessageDialog(null, "Camino encontrado mostrado.");
            pasoActual = 0;
            pasoAPasoCeldas = null;
            caminoEncontrado = null;
        }
    }

    // Permite reiniciar el modo paso a paso
    public void reiniciarPasoAPaso() {
        pasoActual = 0;
        pasoAPasoCeldas = null;
        caminoEncontrado = null;
    }

    /**
     * Abre una nueva ventana para crear un nuevo laberinto.
     */
    public void nuevoLaberinto() {
        try {
            int filas = Integer.parseInt(JOptionPane.showInputDialog("Número de filas:"));
            int columnas = Integer.parseInt(JOptionPane.showInputDialog("Número de columnas:"));
            views.MazeFrame nuevo = new views.MazeFrame(filas, columnas, this);
            nuevo.setVisible(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida");
        }
    }

    /**
     * Abre el cuadro de diálogo para mostrar los resultados almacenados.
     */
    public void mostrarResultados(JFrame parent) {
        views.ResultadosDialog dialog = new views.ResultadosDialog(parent, dao);
        dialog.setVisible(true);
    }
}

package views;

import controllers.MazeController;
import models.Cell;
import models.SolveResults;
import solver.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MazeFrame extends JFrame {
    private boolean[][] laberinto;
    private MazePanel panel;
    private JComboBox<String> algoritmoCombo;
    private String modo = "";

    public MazeFrame(boolean[][] laberinto, Cell inicio, Cell fin) {
        this.laberinto = laberinto;

        setTitle("Maze Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem nuevo = new JMenuItem("Nuevo laberinto");
        JMenuItem verResultados = new JMenuItem("Ver resultados");
        archivo.add(nuevo);
        archivo.add(verResultados);

        JMenu ayuda = new JMenu("Ayuda");
        JMenuItem acerca = new JMenuItem("Acerca de");
        ayuda.add(acerca);

        menuBar.add(archivo);
        menuBar.add(ayuda);
        setJMenuBar(menuBar);

        JPanel botonera = new JPanel();
        JButton start = new JButton("Set Start");
        JButton end = new JButton("Set End");
        JButton toggle = new JButton("Toggle Wall");
        botonera.add(start);
        botonera.add(end);
        botonera.add(toggle);
        add(botonera, BorderLayout.NORTH);

        algoritmoCombo = new JComboBox<>(new String[]{"Recursivo", "Recursivo Completo", "Recursivo Completo BT", "BFS", "DFS"});
        JButton resolver = new JButton("Resolver");
        JButton paso = new JButton("Paso a paso");
        JButton limpiar = new JButton("Limpiar");

        JPanel controles = new JPanel();
        controles.add(new JLabel("Algoritmo:"));
        controles.add(algoritmoCombo);
        controles.add(resolver);
        controles.add(paso);
        controles.add(limpiar);
        add(controles, BorderLayout.SOUTH);

        MazeSolver solver = obtenerSolver();
        MazeController controller = new MazeController(solver);
        SolveResults resultado = controller.resolverLaberinto(laberinto, inicio, fin);

        panel = new MazePanel(laberinto, resultado.getCaminos().get(0), resultado.getVisitados().get(0), inicio, fin);
        add(panel, BorderLayout.CENTER);

        verResultados.addActionListener(e -> new ResultadosDialog(this, resultado.getCaminos().get(0), resultado.getVisitados().get(0)));
        resolver.addActionListener(e -> {
            MazeSolver nuevoSolver = obtenerSolver();
            MazeController nuevoController = new MazeController(nuevoSolver);
            SolveResults nuevoResultado = nuevoController.resolverLaberinto(
                    laberinto,
                    panel.getInicio(),
                    panel.getFin()
            );
            remove(panel);
            panel = new MazePanel(laberinto, nuevoResultado.getCaminos().get(0), nuevoResultado.getVisitados().get(0), panel.getInicio(), panel.getFin());
            add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        start.addActionListener(e -> modo = "start");
        end.addActionListener(e -> modo = "end");
        toggle.addActionListener(e -> modo = "toggle");

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = e.getY() / (panel.getHeight() / laberinto.length);
                int columna = e.getX() / (panel.getWidth() / laberinto[0].length);

                if (modo.equals("start")) {
                    panel.setInicio(new Cell(fila, columna));
                } else if (modo.equals("end")) {
                    panel.setFin(new Cell(fila, columna));
                } else if (modo.equals("toggle")) {
                    Cell ini = panel.getInicio();
                    Cell f = panel.getFin();
                    if ((ini != null && ini.getRow() == fila && ini.getCol() == columna) ||
                            (f != null && f.getRow() == fila && f.getCol() == columna)) {
                        return;
                    }
                    panel.toggleWall(fila, columna);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private MazeSolver obtenerSolver() {
        String opcion = (String) algoritmoCombo.getSelectedItem();
        return switch (opcion) {
            case "BFS" -> new solver.impl.MazeSolverBFS();
            case "DFS" -> new solver.impl.MazeSolverDFS();
            case "Recursivo" -> new solver.impl.MazeSolverRecursivo();
            case "Recursivo Completo" -> new solver.impl.MazeSolverRecursivoCompleto();
            case "Recursivo Completo BT" -> new solver.impl.MazeSolverRecursivoCompletoBT();
            default -> new solver.impl.MazeSolverBFS();
        };
    }
}

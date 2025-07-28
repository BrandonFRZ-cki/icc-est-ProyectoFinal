package views;

import controllers.MazeController;
import models.Cell;
import solver.MazeSolver;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MazeFrame extends JFrame {
    private MazePanel mazePanel;
    private JComboBox<String> comboBoxAlgoritmos;
    private MazeController controller;
    private Map<String, MazeSolver> solverMap;

    public MazeFrame(int filas, int columnas, MazeController controller) {
        this.controller = controller;
        this.solverMap = controller.getAlgoritmos();
        setTitle("Resolución de Laberintos");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents(filas, columnas);
    }

    private void initComponents(int filas, int columnas) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem nuevo = new JMenuItem("Nuevo laberinto");
        JMenuItem verResultados = new JMenuItem("Ver resultados");
        menuArchivo.add(nuevo);
        menuArchivo.add(verResultados);
        menuBar.add(menuArchivo);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem ayuda = new JMenuItem("¿Cómo usar?");
        JMenuItem acercaDe = new JMenuItem("Acerca de");
        menuAyuda.add(ayuda);
        menuAyuda.add(acercaDe);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);

        JPanel panelBotones = new JPanel();
        JButton btnSetStart = new JButton("Set Start");
        JButton btnSetEnd = new JButton("Set End");
        JButton btnToggleWall = new JButton("Toggle Wall");
        JButton btnResolver = new JButton("Resolver");
        JButton btnPaso = new JButton("Paso a paso");
        JButton btnLimpiar = new JButton("Limpiar");

        comboBoxAlgoritmos = new JComboBox<>(new String[]{
                "Recursivo 2D",
                "Recursivo 4D",
                "Recursivo 4D BT",
                "DFS",
                "BFS"
        });

        panelBotones.add(btnSetStart);
        panelBotones.add(btnSetEnd);
        panelBotones.add(btnToggleWall);
        panelBotones.add(comboBoxAlgoritmos);
        panelBotones.add(btnResolver);
        panelBotones.add(btnPaso);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.NORTH);

        // ✅ Crear MazePanel con filas y columnas
        mazePanel = new MazePanel(filas, columnas);
        add(mazePanel, BorderLayout.CENTER);

        // ✅ Acciones
        btnSetStart.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.SET_START));
        btnSetEnd.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.SET_END));
        btnToggleWall.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.TOGGLE_WALL));
        btnResolver.addActionListener(e -> controller.resolver(mazePanel, getAlgoritmoSeleccionado()));
        btnPaso.addActionListener(e -> controller.paso(mazePanel, getAlgoritmoSeleccionado()));
        btnLimpiar.addActionListener(e -> mazePanel.resetGrid());

        nuevo.addActionListener(e -> controller.nuevoLaberinto());
        verResultados.addActionListener(e -> controller.mostrarResultados(this));
        ayuda.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Primero elige tamaño. Luego marca inicio, fin y muros. Escoge algoritmo y resuelve."));
        acercaDe.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Autores: Erick Yunga y Brandon Rivera\nGitHub: ErickJYC, BrandonFRZ-cki"));
    }

    private String getAlgoritmoSeleccionado() {
        return (String) comboBoxAlgoritmos.getSelectedItem();
    }

    public void setMazePanel(MazePanel panel) {
        remove(mazePanel);
        this.mazePanel = panel;
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}

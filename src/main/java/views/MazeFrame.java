package views;

import controllers.MazeController;
import solver.MazeSolver;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MazeFrame extends JFrame {
    private MazePanel mazePanel;
    private JComboBox<String> comboBoxAlgoritmos;
    private MazeController controller;
    private Map<String, MazeSolver> solverMap;

    // Mapeo visible -> interno
    private final Map<String, String> nombreInternoAlgoritmo = Map.of(
            "Recursivo", "Recursivo 2D",
            "Recursivo Completo", "Recursivo 4D",
            "Recursivo Completo BT", "Recursivo 4D BT",
            "BFS", "BFS",
            "DFS", "DFS"
    );

    public MazeFrame(int filas, int columnas, MazeController controller) {
        this.controller = controller;
        this.solverMap = controller.getAlgoritmos();
        setTitle("Resolución de Laberintos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
        initComponents(filas, columnas);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // iniciar maximizado
    }

    private void initComponents(int filas, int columnas) {
        // Menú
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

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSetStart = new JButton("Set Start");
        JButton btnSetEnd = new JButton("Set End");
        JButton btnToggleWall = new JButton("Toggle Wall");
        topPanel.add(btnSetStart);
        topPanel.add(btnSetEnd);
        topPanel.add(btnToggleWall);
        add(topPanel, BorderLayout.NORTH);

        // MazePanel centrado y escalable
        mazePanel = new MazePanel(filas, columnas);
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.LIGHT_GRAY);
        centerWrapper.add(mazePanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(new JLabel("Algoritmo:"));

        comboBoxAlgoritmos = new JComboBox<>(new String[]{
                "Recursivo", "Recursivo Completo", "Recursivo Completo BT", "BFS", "DFS"
        });
        JButton btnResolver = new JButton("Resolver");
        JButton btnPaso = new JButton("Paso a paso");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnLimpiarCamino = new JButton("Limpiar camino");
        JButton btnResolverAnimado = new JButton("Resolver animado");


        bottomPanel.add(comboBoxAlgoritmos);
        bottomPanel.add(btnResolver);
        bottomPanel.add(btnResolverAnimado);
        bottomPanel.add(btnPaso);
        bottomPanel.add(btnLimpiar);
        bottomPanel.add(btnLimpiarCamino);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        btnSetStart.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.SET_START));
        btnSetEnd.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.SET_END));
        btnToggleWall.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.TOGGLE_WALL));
        btnResolver.addActionListener(e -> {
            controller.reiniciarPasoAPaso();
            controller.resolver(mazePanel, getAlgoritmoSeleccionado());
        });
        btnResolverAnimado.addActionListener(e -> {
            controller.reiniciarPasoAPaso(); // opcional, para evitar conflicto
            controller.resolverAnimado(mazePanel, getAlgoritmoSeleccionado());
        });

        btnPaso.addActionListener(e -> controller.paso(mazePanel, getAlgoritmoSeleccionado()));
        btnLimpiar.addActionListener(e -> {
            controller.reiniciarPasoAPaso();
            mazePanel.resetGrid();
        });

        btnLimpiarCamino.addActionListener(e -> {
            controller.reiniciarPasoAPaso();
            mazePanel.clearPathAndVisited();
        });

        comboBoxAlgoritmos.addActionListener(e -> controller.reiniciarPasoAPaso());


        nuevo.addActionListener(e -> controller.nuevoLaberinto());
        verResultados.addActionListener(e -> controller.mostrarResultados(this));
        ayuda.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Primero elige tamaño. Luego marca inicio, fin y muros. Escoge algoritmo y resuelve."));
        acercaDe.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Autores: Erick Yunga y Brandon Rivera\nGitHub: ErickJYC, BrandonFRZ-cki"));

        SwingUtilities.invokeLater(() -> btnSetStart.requestFocusInWindow());
    }

    private String getAlgoritmoSeleccionado() {
        String visible = (String) comboBoxAlgoritmos.getSelectedItem();
        return nombreInternoAlgoritmo.getOrDefault(visible, "BFS");
    }

    public void setMazePanel(MazePanel panel) {
        remove(mazePanel);
        this.mazePanel = panel;
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(mazePanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}


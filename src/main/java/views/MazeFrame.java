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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // 🔷 Icono de la app (maze.png)
        Image icono = new ImageIcon(getClass().getClassLoader().getResource("icons/maze.png")).getImage();
        setIconImage(icono);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents(filas, columnas);
    }

    private void initComponents(int filas, int columnas) {
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));

        // Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem nuevo = new JMenuItem("🆕 Nuevo laberinto");
        JMenuItem verResultados = new JMenuItem("📊 Ver resultados");
        menuArchivo.add(nuevo);
        menuArchivo.add(verResultados);
        menuBar.add(menuArchivo);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem ayuda = new JMenuItem("❓ ¿Cómo usar?");
        JMenuItem acercaDe = new JMenuItem("👥 Acerca de");
        menuAyuda.add(ayuda);
        menuAyuda.add(acercaDe);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(new Color(240, 248, 255));

        JButton btnSetStart = new JButton("Start");
        btnSetStart.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/start.png")));
        btnSetStart.setToolTipText("Establece la celda de inicio");

        JButton btnSetEnd = new JButton("End");
        btnSetEnd.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/finish-flag.png")));
        btnSetEnd.setToolTipText("Establece la celda de destino");

        JButton btnToggleWall = new JButton("Muro");
        btnToggleWall.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/firewall.png")));
        btnToggleWall.setToolTipText("Activa o desactiva un muro");

        topPanel.add(btnSetStart);
        topPanel.add(btnSetEnd);
        topPanel.add(btnToggleWall);
        add(topPanel, BorderLayout.NORTH);

        // MazePanel centrado
        mazePanel = new MazePanel(filas, columnas);
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(220, 220, 220));
        centerWrapper.add(mazePanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));

        comboBoxAlgoritmos = new JComboBox<>(new String[]{
                "Recursivo", "Recursivo Completo", "Recursivo Completo BT", "BFS", "DFS"
        });

        JButton btnResolver = new JButton("Resolver");
        JButton btnResolverAnimado = new JButton("Animado");
        JButton btnPaso = new JButton("Paso a paso");
        JButton btnLimpiar = new JButton("Limpiar todo");
        JButton btnLimpiarCamino = new JButton("Limpiar camino");

        bottomPanel.add(new JLabel("Algoritmo:"));
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
            controller.reiniciarPasoAPaso();
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
                "🔹 Paso 1: Marca INICIO y FIN\n🔹 Paso 2: Añade muros (opcional)\n🔹 Paso 3: Elige algoritmo\n🔹 Paso 4: ¡Resuelve!",
                "Guía rápida", JOptionPane.INFORMATION_MESSAGE));
        acercaDe.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Autores: Erick Yunga y Brandon Rivera\nRepositorio: github.com/ErickJYC / BrandonFRZ-cki",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));

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
        centerWrapper.setBackground(new Color(220, 220, 220));
        centerWrapper.add(mazePanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}

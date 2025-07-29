package views;

import controllers.MazeController;
import solver.MazeSolver;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Ventana principal de la aplicación que representa la interfaz gráfica para interactuar con el laberinto.
 */
public class MazeFrame extends JFrame {

    // Panel gráfico del laberinto
    private MazePanel mazePanel;

    // ComboBox para elegir el algoritmo
    private JComboBox<String> comboBoxAlgoritmos;

    // Controlador que maneja la lógica de resolución
    private MazeController controller;

    // Mapa de nombre interno del algoritmo → implementación
    private Map<String, MazeSolver> solverMap;

    // Mapa de nombres visibles en el JComboBox → nombre interno real del algoritmo
    private final Map<String, String> nombreInternoAlgoritmo = Map.of(
            "Recursivo", "Recursivo 2D",
            "Recursivo Completo", "Recursivo 4D",
            "Recursivo Completo BT", "Recursivo 4D BT",
            "BFS", "BFS",
            "DFS", "DFS"
    );

    /**
     * Constructor principal de la ventana.
     * @param filas número de filas del laberinto
     * @param columnas número de columnas del laberinto
     * @param controller controlador del laberinto
     */
    public MazeFrame(int filas, int columnas, MazeController controller) {
        this.controller = controller;
        this.solverMap = controller.getAlgoritmos(); // obtiene los algoritmos disponibles
        setTitle("Resolución de Laberintos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // cerrar solo esta ventana
        setMinimumSize(new Dimension(600, 500)); // tamaño mínimo
        setLocationRelativeTo(null); // centrar ventana
        initComponents(filas, columnas); // inicializa componentes visuales
        setExtendedState(JFrame.MAXIMIZED_BOTH); // inicia en pantalla completa
    }

    /**
     * Inicializa todos los componentes visuales de la ventana.
     */
    private void initComponents(int filas, int columnas) {
        // ======== MENÚ SUPERIOR ========
        JMenuBar menuBar = new JMenuBar();

        // Menú "Archivo"
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem nuevo = new JMenuItem("Nuevo laberinto");
        JMenuItem verResultados = new JMenuItem("Ver resultados");
        menuArchivo.add(nuevo);
        menuArchivo.add(verResultados);
        menuBar.add(menuArchivo);

        // Menú "Ayuda"
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem ayuda = new JMenuItem("¿Cómo usar?");
        JMenuItem acercaDe = new JMenuItem("Acerca de");
        menuAyuda.add(ayuda);
        menuAyuda.add(acercaDe);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar); // establece barra de menú

        // ======== PANEL SUPERIOR (botones de edición) ========
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSetStart = new JButton("Set Start");
        JButton btnSetEnd = new JButton("Set End");
        JButton btnToggleWall = new JButton("Toggle Wall");
        topPanel.add(btnSetStart);
        topPanel.add(btnSetEnd);
        topPanel.add(btnToggleWall);
        add(topPanel, BorderLayout.NORTH);

        // ======== PANEL CENTRAL (laberinto) ========
        mazePanel = new MazePanel(filas, columnas); // crea el laberinto con las dimensiones dadas
        JPanel centerWrapper = new JPanel(new GridBagLayout()); // para centrar el laberinto
        centerWrapper.setBackground(Color.LIGHT_GRAY);
        centerWrapper.add(mazePanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);

        // ======== PANEL INFERIOR (acciones del algoritmo) ========
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(new JLabel("Algoritmo:"));

        // ComboBox con nombres visibles para seleccionar algoritmo
        comboBoxAlgoritmos = new JComboBox<>(new String[]{
                "Recursivo", "Recursivo Completo", "Recursivo Completo BT", "BFS", "DFS"
        });

        // Botones de acción
        JButton btnResolver = new JButton("Resolver");
        JButton btnPaso = new JButton("Paso a paso");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnLimpiarCamino = new JButton("Limpiar camino");
        JButton btnResolverAnimado = new JButton("Resolver animado");

        // Añadir elementos al panel inferior
        bottomPanel.add(comboBoxAlgoritmos);
        bottomPanel.add(btnResolver);
        bottomPanel.add(btnResolverAnimado);
        bottomPanel.add(btnPaso);
        bottomPanel.add(btnLimpiar);
        bottomPanel.add(btnLimpiarCamino);

        add(bottomPanel, BorderLayout.SOUTH);

        // ======== LISTENERS (acción de botones y menú) ========
        btnSetStart.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.SET_START));
        btnSetEnd.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.SET_END));
        btnToggleWall.addActionListener(e -> mazePanel.setCurrentMode(MazePanel.Mode.TOGGLE_WALL));

        // Resolución directa del laberinto
        btnResolver.addActionListener(e -> {
            controller.reiniciarPasoAPaso(); // reinicia animación anterior
            controller.resolver(mazePanel, getAlgoritmoSeleccionado());
        });

        // Resolución animada
        btnResolverAnimado.addActionListener(e -> {
            controller.reiniciarPasoAPaso(); // evita conflictos
            controller.resolverAnimado(mazePanel, getAlgoritmoSeleccionado());
        });

        // Resolución paso a paso
        btnPaso.addActionListener(e -> controller.paso(mazePanel, getAlgoritmoSeleccionado()));

        // Limpiar todo (laberinto completo)
        btnLimpiar.addActionListener(e -> {
            controller.reiniciarPasoAPaso();
            mazePanel.resetGrid();
        });

        // Limpiar solo camino y celdas visitadas
        btnLimpiarCamino.addActionListener(e -> {
            controller.reiniciarPasoAPaso();
            mazePanel.clearPathAndVisited();
        });

        // Cambiar algoritmo → reinicia paso a paso
        comboBoxAlgoritmos.addActionListener(e -> controller.reiniciarPasoAPaso());

        // Menú: nuevo laberinto
        nuevo.addActionListener(e -> controller.nuevoLaberinto());

        // Menú: mostrar resultados
        verResultados.addActionListener(e -> controller.mostrarResultados(this));

        // Menú: ayuda
        ayuda.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Primero elige tamaño. Luego marca inicio, fin y muros. Escoge algoritmo y resuelve."));

        // Menú: acerca de
        acercaDe.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Autores: Erick Yunga y Brandon Rivera\nGitHub: ErickJYC, BrandonFRZ-cki"));

        // Pone el foco por defecto en el botón de inicio
        SwingUtilities.invokeLater(() -> btnSetStart.requestFocusInWindow());
    }

    /**
     * Devuelve el nombre interno del algoritmo seleccionado en el combo box.
     */
    private String getAlgoritmoSeleccionado() {
        String visible = (String) comboBoxAlgoritmos.getSelectedItem();
        return nombreInternoAlgoritmo.getOrDefault(visible, "BFS");
    }

    /**
     * Cambia dinámicamente el panel del laberinto.
     */
    public void setMazePanel(MazePanel panel) {
        remove(mazePanel); // elimina el panel anterior
        this.mazePanel = panel;
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(mazePanel, new GridBagConstraints());
        add(centerWrapper, BorderLayout.CENTER);
        revalidate(); // actualiza la interfaz
        repaint();    // repinta la ventana
    }
}

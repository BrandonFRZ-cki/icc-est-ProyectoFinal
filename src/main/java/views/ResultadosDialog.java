package views;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Diálogo que muestra los resultados de ejecución de los algoritmos de resolución.
 */
public class ResultadosDialog extends JDialog {

    private final AlgorithmResultDAO resultDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    /**
     * Constructor del diálogo de resultados.
     * @param parent Ventana padre.
     * @param resultDAO DAO que gestiona los resultados.
     */
    public ResultadosDialog(Frame parent, AlgorithmResultDAO resultDAO) {
        super(parent, "Resultados de Algoritmos", true);
        this.resultDAO = resultDAO;
        initializeUI();
    }

    /**
     * Inicializa los componentes gráficos del diálogo.
     */
    private void initializeUI() {
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla de resultados
        tableModel = new DefaultTableModel(new String[]{"Algoritmo", "Celdas", "Tiempo (ns)"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        JButton btnGraficar = new JButton("Graficar Resultados");
        btnGraficar.setEnabled(false); // aún no implementado
        btnGraficar.addActionListener(e -> graficarResultados());
        panelBotones.add(btnGraficar);

        JButton btnLimpiar = new JButton("Limpiar Resultados");
        btnLimpiar.addActionListener(e -> limpiarResultados());
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);

        cargarResultados();
    }

    /**
     * Carga los resultados almacenados en la tabla.
     */
    private void cargarResultados() {
        tableModel.setRowCount(0); // limpia tabla
        List<AlgorithmResult> resultados = resultDAO.listar();
        for (AlgorithmResult r : resultados) {
            tableModel.addRow(new Object[]{r.getAlgorithmName(), r.getSteps(), r.getTimeNano()});
        }
    }

    /**
     * Método de placeholder para futura visualización gráfica.
     */
    private void graficarResultados() {
        JOptionPane.showMessageDialog(this,
                "Funcionalidad de gráficos aún no implementada.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Limpia todos los resultados después de confirmación del usuario.
     */
    private void limpiarResultados() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar todos los resultados?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            resultDAO.limpiar();
            cargarResultados();
        }
    }
}

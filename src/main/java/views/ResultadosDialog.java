package views;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;
import models.Cell;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class ResultadosDialog extends JDialog {
    private final AlgorithmResultDAO resultDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public ResultadosDialog(Frame parent, AlgorithmResultDAO resultDAO) {
        super(parent, "Resultados de Algoritmos", true);
        this.resultDAO = resultDAO;
        initializeUI();
    }

    private void initializeUI() {
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla
        tableModel = new DefaultTableModel(new String[]{"Algoritmo", "Celdas", "Tiempo (ns)"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnGraficar = new JButton("Graficar Resultados");
        btnGraficar.setEnabled(false); // Desactivado hasta implementar
        panelBotones.add(btnGraficar);

        JButton btnLimpiar = new JButton("Limpiar Resultados");
        btnLimpiar.addActionListener(e -> limpiarResultados());
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);

        cargarResultados();
    }

    private void cargarResultados() {
        tableModel.setRowCount(0);
        List<AlgorithmResult> resultados = resultDAO.listar(); // método correcto del DAO
        for (AlgorithmResult r : resultados) {
            tableModel.addRow(new Object[]{r.getAlgorithmName(), r.getSteps(), r.getTimeNano()});
        }
    }

    // Método aún no implementado
    private void graficarResultados() {
        // Aquí iría la lógica de gráficos si decides agregarla después
    }

    private void limpiarResultados() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar todos los resultados?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            resultDAO.limpiar();
            cargarResultados();
        }
    }
}

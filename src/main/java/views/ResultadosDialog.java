package views;

import models.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class ResultadosDialog extends JDialog {
    public ResultadosDialog(JFrame parent, List<Cell> camino, Set<Cell> visitadas) {
        super(parent, "Resultados", true);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.append("Camino encontrado:\n");
        for (Cell c : camino) {
            area.append("(" + c.getRow() + "," + c.getCol() + ")\n");
        }
        area.append("\nCeldas visitadas:\n");
        for (Cell c : visitadas) {
            area.append("(" + c.getRow() + "," + c.getCol() + ")\n");
        }

        add(new JScrollPane(area), BorderLayout.CENTER);
        JButton cerrar = new JButton("Cerrar");
        cerrar.addActionListener(e -> dispose());
        add(cerrar, BorderLayout.SOUTH);

        setSize(300, 400);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}

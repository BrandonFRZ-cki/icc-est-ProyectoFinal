import controllers.MazeController;
import dao.AlgorithmResultDAO;
import dao.impl.AlgorithmResultDAOFile;
import views.MazeFrame;

import javax.swing.*;

public class MazeApp {

    public static void main(String[] args) {
        // Aplicar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el look and feel: " + e.getMessage());
        }

        // Ejecutar la interfaz en el hilo de eventos
        SwingUtilities.invokeLater(() -> {
            int filas = pedirNumero("Ingrese el número de filas del laberinto (mínimo 5):", 5);
            int columnas = pedirNumero("Ingrese el número de columnas del laberinto (mínimo 5):", 5);

            // Crear DAO e instanciar el controlador
            AlgorithmResultDAO dao = new AlgorithmResultDAOFile();
            MazeController controller = new MazeController(dao);

            // Crear y mostrar el frame principal
            MazeFrame frame = new MazeFrame(filas, columnas, controller);
            frame.setVisible(true);
        });
    }

    /**
     * Muestra un JOptionPane para pedir un número entero >= mínimo.
     */
    private static int pedirNumero(String mensaje, int minimo) {
        int valor = -1;
        while (valor < minimo) {
            String input = JOptionPane.showInputDialog(null, mensaje, "Configuración", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                System.exit(0); // Usuario canceló
            }
            try {
                valor = Integer.parseInt(input);
                if (valor < minimo) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un número mayor o igual a " + minimo);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingrese un número válido.");
            }
        }
        return valor;
    }
}

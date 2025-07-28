import controllers.MazeController;
import dao.AlgorithmResultDAO;
import dao.impl.AlgorithmResultDAOFile;
import views.MazeFrame;

import javax.swing.*;

public class MazeApp {

    public static void main(String[] args) {
        // Establecer el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            // Pedir al usuario el tamaño del laberinto
            int filas = pedirNumero("Ingrese el número de filas del laberinto (mínimo 5):", 5);
            int columnas = pedirNumero("Ingrese el número de columnas del laberinto (mínimo 5):", 5);

            // Crear DAO y controlador
            AlgorithmResultDAO dao = new AlgorithmResultDAOFile();
            MazeController controller = new MazeController(dao);

            // Crear y mostrar la interfaz principal
            MazeFrame frame = new MazeFrame(filas, columnas, controller);
            frame.setVisible(true);
        });
    }

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
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un número mayor o igual a " + minimo);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, ingrese un número válido.");
            }
        }
        return valor;
    }
}

import controllers.MazeController;
import dao.AlgorithmResultDAO;
import dao.impl.AlgorithmResultDAOFile;
import views.MazeFrame;

import javax.swing.*;

/**
 * Clase principal que inicia la aplicación de resolución de laberintos.
 */
public class MazeApp {

    /**
     * Punto de entrada de la aplicación.
     */
    public static void main(String[] args) {
        // Aplicar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el look and feel: " + e.getMessage());
        }

        // Ejecutar en hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            int filas = pedirNumero("Ingrese el número de filas del laberinto (mínimo 5):", 5);
            int columnas = pedirNumero("Ingrese el número de columnas del laberinto (mínimo 5):", 5);

            // DAO e inicialización del controlador
            AlgorithmResultDAO dao = new AlgorithmResultDAOFile();
            MazeController controller = new MazeController(dao);

            // Crear y mostrar la ventana principal
            MazeFrame frame = new MazeFrame(filas, columnas, controller);
            frame.setVisible(true);
        });
    }

    /**
     * Muestra un JOptionPane que solicita un número entero mayor o igual al mínimo requerido.
     *
     * @param mensaje Mensaje a mostrar en el cuadro de diálogo.
     * @param minimo  Valor mínimo permitido.
     * @return Número entero válido ingresado por el usuario.
     */
    private static int pedirNumero(String mensaje, int minimo) {
        int valor = -1;
        while (valor < minimo) {
            String input = JOptionPane.showInputDialog(
                    null, mensaje, "Configuración", JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) {
                System.exit(0); // Cancelado por el usuario
            }

            try {
                valor = Integer.parseInt(input.trim());
                if (valor < minimo) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Por favor, ingrese un número mayor o igual a " + minimo,
                            "Valor no válido",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Entrada inválida. Por favor, ingrese un número válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return valor;
    }
}

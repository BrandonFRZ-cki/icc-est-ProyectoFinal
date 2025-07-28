
import models.Cell;
import views.MazeFrame;

public class MazeApp {
    public static void main(String[] args) {
        // Matriz del laberinto: true = libre, false = muro
        boolean[][] laberinto = {
                { true,  true,  false, true },
                { false, true,  false, true },
                { true,  true,  true,  true },
                { false, false, true,  true }
        };

        // Definimos punto de inicio y fin por defecto
        Cell inicio = new Cell(0, 0); // Verde
        Cell fin = new Cell(3, 3);    // Rojo

        // Iniciar la aplicación de forma segura en el hilo de eventos Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MazeFrame(laberinto, inicio, fin);
        });
    }
}

package views;

import models.Cell;
import models.CellState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

/**
 * Panel gráfico que representa el laberinto con celdas interactivas.
 * Permite establecer inicio, fin, muros y mostrar el camino recorrido por los algoritmos.
 */
public class MazePanel extends JPanel {

    private CellState[][] cellStates; // matriz que representa el estado de cada celda
    private int rows;  // número de filas
    private int cols;  // número de columnas

    private Cell startCell; // celda de inicio
    private Cell endCell;   // celda de fin

    private Mode currentMode = Mode.NONE; // modo actual de interacción del mouse

    /**
     * Modos de interacción del mouse.
     * NONE: sin acción
     * SET_START: definir punto de inicio
     * SET_END: definir punto de fin
     * TOGGLE_WALL: alternar muro/libre
     */
    public enum Mode {
        NONE, SET_START, SET_END, TOGGLE_WALL
    }

    /**
     * Constructor del panel del laberinto.
     * @param rows número de filas
     * @param cols número de columnas
     */
    public MazePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cellStates = new CellState[rows][cols];
        resetGrid(); // inicializa el grid a vacío

        setBackground(Color.WHITE); // fondo del panel

        // Detecta clics del mouse en el panel
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int anchoDisponible = getWidth();
                int altoDisponible = getHeight();
                int cellSize = Math.min(anchoDisponible / cols, altoDisponible / rows);

                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;

                // Validar límites
                if (row < 0 || row >= rows || col < 0 || col >= cols) return;

                Cell cell = new Cell(row, col);

                // Según el modo actual, se establece una acción
                switch (currentMode) {
                    case SET_START -> {
                        if (startCell != null) {
                            cellStates[startCell.getRow()][startCell.getCol()] = CellState.EMPTY;
                        }
                        startCell = cell;
                        cellStates[row][col] = CellState.START;
                    }
                    case SET_END -> {
                        if (endCell != null) {
                            cellStates[endCell.getRow()][endCell.getCol()] = CellState.EMPTY;
                        }
                        endCell = cell;
                        cellStates[row][col] = CellState.END;
                    }
                    case TOGGLE_WALL -> {
                        // No se puede cambiar el estado de inicio/fin
                        if (!cell.equals(startCell) && !cell.equals(endCell)) {
                            if (cellStates[row][col] == CellState.WALL) {
                                cellStates[row][col] = CellState.EMPTY;
                            } else {
                                cellStates[row][col] = CellState.WALL;
                            }
                        }
                    }
                    default -> {}
                }

                repaint(); // redibuja el panel
            }
        });
    }

    /**
     * Reinicia toda la cuadrícula, dejándola vacía y sin inicio ni fin.
     */
    public void resetGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellStates[i][j] = CellState.EMPTY;
            }
        }
        startCell = null;
        endCell = null;
        repaint(); // redibuja
    }

    // Setter para establecer el modo de interacción
    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
    }

    // Getters necesarios para el controlador
    public Cell getStartCell() {
        return startCell;
    }

    public Cell getEndCell() {
        return endCell;
    }

    public CellState[][] getCellStates() {
        return cellStates;
    }

    /**
     * Dibuja el camino resuelto por el algoritmo en el laberinto.
     * @param path lista de celdas que representan el camino
     */
    public void setPath(List<Cell> path) {
        for (Cell cell : path) {
            if (!cell.equals(startCell) && !cell.equals(endCell)) {
                cellStates[cell.getRow()][cell.getCol()] = CellState.PATH;
            }
        }
        repaint();
    }

    /**
     * Muestra las celdas que fueron visitadas por el algoritmo.
     * @param visited conjunto de celdas visitadas
     */
    public void setVisited(Set<Cell> visited) {
        for (Cell cell : visited) {
            if (!cell.equals(startCell) && !cell.equals(endCell)
                    && cellStates[cell.getRow()][cell.getCol()] == CellState.EMPTY) {
                cellStates[cell.getRow()][cell.getCol()] = CellState.VISITED;
            }
        }
        repaint();
    }

    /**
     * Elimina el camino y las celdas visitadas, pero mantiene inicio, fin y muros.
     */
    public void clearPathAndVisited() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cellStates[i][j] == CellState.PATH || cellStates[i][j] == CellState.VISITED) {
                    cellStates[i][j] = CellState.EMPTY;
                }
            }
        }
        repaint();
    }

    /**
     * Tamaño preferido del panel. Sirve para ayudar al layout manager.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cols * 35, rows * 35); // 35 píxeles por celda
    }

    /**
     * Método que pinta visualmente el estado actual de cada celda.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoPanel = getWidth();
        int altoPanel = getHeight();

        int cellWidth = anchoPanel / cols;
        int cellHeight = altoPanel / rows;
        int cellSize = Math.min(cellWidth, cellHeight); // tamaño cuadrado por celda

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Selección del color según el estado de la celda
                switch (cellStates[i][j]) {
                    case EMPTY -> g.setColor(Color.WHITE);
                    case WALL -> g.setColor(Color.BLACK);
                    case START -> g.setColor(Color.GREEN);
                    case END -> g.setColor(Color.RED);
                    case PATH -> g.setColor(Color.CYAN);
                    case VISITED -> g.setColor(Color.LIGHT_GRAY);
                }

                int x = j * cellSize;
                int y = i * cellSize;

                g.fillRect(x, y, cellSize, cellSize); // pinta la celda
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize); // dibuja el borde
            }
        }
    }
}

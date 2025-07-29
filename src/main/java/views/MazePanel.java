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
 */
public class MazePanel extends JPanel {

    private CellState[][] cellStates;
    private int rows;
    private int cols;

    private Cell startCell;
    private Cell endCell;

    private Mode currentMode = Mode.NONE;

    /**
     * Modos de interacción del mouse.
     */
    public enum Mode {
        NONE, SET_START, SET_END, TOGGLE_WALL
    }

    /**
     * Constructor del panel del laberinto.
     */
    public MazePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cellStates = new CellState[rows][cols];
        resetGrid();

        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int anchoDisponible = getWidth();
                int altoDisponible = getHeight();
                int cellSize = Math.min(anchoDisponible / cols, altoDisponible / rows);

                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;

                if (row < 0 || row >= rows || col < 0 || col >= cols) return;

                Cell cell = new Cell(row, col);

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

                repaint();
            }
        });
    }

    /**
     * Reinicia la cuadrícula del laberinto.
     */
    public void resetGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellStates[i][j] = CellState.EMPTY;
            }
        }
        startCell = null;
        endCell = null;
        repaint();
    }

    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
    }

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
     * Dibuja el camino solucionado en el laberinto.
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
     * Muestra las celdas visitadas por el algoritmo.
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
     * Limpia el camino y los nodos visitados.
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
     * Define el tamaño preferido del panel.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cols * 35, rows * 35);
    }

    /**
     * Dibuja las celdas del laberinto.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoPanel = getWidth();
        int altoPanel = getHeight();

        int cellWidth = anchoPanel / cols;
        int cellHeight = altoPanel / rows;
        int cellSize = Math.min(cellWidth, cellHeight);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
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
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }
}

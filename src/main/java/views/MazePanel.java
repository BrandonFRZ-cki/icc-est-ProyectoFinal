package views;

import models.Cell;
import models.CellState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

public class MazePanel extends JPanel {

    private CellState[][] cellStates;
    private int rows;
    private int cols;

    private Cell startCell;
    private Cell endCell;

    private Mode currentMode = Mode.NONE;

    public enum Mode {
        NONE, SET_START, SET_END, TOGGLE_WALL
    }

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
                    case SET_START:
                        if (startCell != null) {
                            cellStates[startCell.getRow()][startCell.getCol()] = CellState.EMPTY;
                        }
                        startCell = cell;
                        cellStates[row][col] = CellState.START;
                        break;

                    case SET_END:
                        if (endCell != null) {
                            cellStates[endCell.getRow()][endCell.getCol()] = CellState.EMPTY;
                        }
                        endCell = cell;
                        cellStates[row][col] = CellState.END;
                        break;

                    case TOGGLE_WALL:
                        if (!cell.equals(startCell) && !cell.equals(endCell)) {
                            if (cellStates[row][col] == CellState.WALL) {
                                cellStates[row][col] = CellState.EMPTY;
                            } else {
                                cellStates[row][col] = CellState.WALL;
                            }
                        }
                        break;

                    default:
                        break;
                }

                repaint();
            }
        });
    }

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

    public void setPath(List<Cell> path) {
        for (Cell cell : path) {
            if (!cell.equals(startCell) && !cell.equals(endCell)) {
                cellStates[cell.getRow()][cell.getCol()] = CellState.PATH;
            }
        }
        repaint();
    }

    public void setVisited(Set<Cell> visited) {
        for (Cell cell : visited) {
            if (!cell.equals(startCell) && !cell.equals(endCell)
                    && cellStates[cell.getRow()][cell.getCol()] == CellState.EMPTY) {
                cellStates[cell.getRow()][cell.getCol()] = CellState.VISITED;
            }
        }
        repaint();
    }

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoDisponible = getWidth();
        int altoDisponible = getHeight();
        int cellSize = Math.min(anchoDisponible / cols, altoDisponible / rows);

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
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }
}

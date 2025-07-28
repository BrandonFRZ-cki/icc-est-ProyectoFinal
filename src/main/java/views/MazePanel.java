package views;

import models.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class MazePanel extends JPanel {
    private boolean[][] laberinto;
    private List<Cell> camino;
    private Set<Cell> visitadas;
    private Cell inicio;
    private Cell fin;

    public MazePanel(boolean[][] laberinto, List<Cell> camino, Set<Cell> visitadas, Cell inicio, Cell fin) {
        this.laberinto = laberinto;
        this.camino = camino;
        this.visitadas = visitadas;
        this.inicio = inicio;
        this.fin = fin;
        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int filas = laberinto.length;
        int columnas = laberinto[0].length;
        int ancho = getWidth() / columnas;
        int alto = getHeight() / filas;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!laberinto[i][j]) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * ancho, i * alto, ancho, alto);
                g.setColor(Color.GRAY);
                g.drawRect(j * ancho, i * alto, ancho, alto);
            }
        }

        g.setColor(Color.YELLOW);
        for (Cell c : visitadas) {
            g.fillRect(c.getCol() * ancho, c.getRow() * alto, ancho, alto);
        }

        g.setColor(Color.GREEN);
        for (Cell c : camino) {
            g.fillRect(c.getCol() * ancho, c.getRow() * alto, ancho, alto);
        }

        if (inicio != null) {
            g.setColor(Color.GREEN);
            g.fillRect(inicio.getCol() * ancho, inicio.getRow() * alto, ancho, alto);
        }

        if (fin != null) {
            g.setColor(Color.RED);
            g.fillRect(fin.getCol() * ancho, fin.getRow() * alto, ancho, alto);
        }
    }

    public void setInicio(Cell inicio) {
        this.inicio = inicio;
        repaint();
    }

    public void setFin(Cell fin) {
        this.fin = fin;
        repaint();
    }

    public void toggleWall(int fila, int columna) {
        laberinto[fila][columna] = !laberinto[fila][columna];
        repaint();
    }

    public Cell getInicio() {
        return inicio;
    }

    public Cell getFin() {
        return fin;
    }
}

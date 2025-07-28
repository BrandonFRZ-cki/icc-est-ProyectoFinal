// Cell.java
package models;

import java.util.Objects;

/**
 * Representa una celda en el laberinto mediante sus coordenadas (fila, columna).
 */
public class Cell {
    private final int row;
    private final int col;

    /**
     * Crea una nueva celda con la fila y columna especificadas.
     * @param row fila de la celda
     * @param col columna de la celda
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return la fila de la celda.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return la columna de la celda.
     */
    public int getCol() {
        return col;
    }

    /**
     * Compara si dos celdas son iguales por su posición (fila y columna).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell cell)) return false;
        return row == cell.row && col == cell.col;
    }

    /**
     * Genera un hash único basado en fila y columna.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Devuelve una representación en texto de la celda como "(fila,columna)".
     */
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}

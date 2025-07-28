package models;

public enum CellState {
    EMPTY,     // Celda vacía/transitable
    WALL,      // Celda con muro
    START,     // Punto de inicio (verde)
    END,       // Punto de fin (rojo)
    VISITED,   // Celda que ha sido visitada
    PATH,      // Celda que forma parte del camino encontrado
    CURRENT    // Celda actual analizada (para visualización paso a paso)


}
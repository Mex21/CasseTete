package CasseTete.Cell;

import javafx.scene.text.Text;

public abstract class Cell {
    private int x, y;
    private boolean occupied;
    private String symbol;

    public Cell(int x, int y, boolean occupied, String symbol) {
        this.x = x;
        this.y = y;
        this.occupied = occupied;
        this.symbol = symbol;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

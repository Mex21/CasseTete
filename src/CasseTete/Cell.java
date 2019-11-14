package CasseTete;

import javafx.scene.text.Text;

public abstract class Cell {
    private int x, y;
    private boolean occupied;
    private Text symbol;

    public Cell(int x, int y, boolean occupied, Text symbol) {
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

    public Text getSymbol() {
        return symbol;
    }

    public void setSymbol(Text symbol) {
        this.symbol = symbol;
    }
}

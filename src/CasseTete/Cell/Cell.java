package CasseTete.Cell;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public abstract class Cell {
    private int x, y;
    private String symbol;

    public Cell(int x, int y, String symbol) {
        this.x = x;
        this.y = y;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


}

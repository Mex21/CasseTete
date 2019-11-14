package CasseTete;

import javafx.scene.text.Text;

public class CellSymbol extends Cell {

    private Text symbole;

    public CellSymbol(int x, int y, boolean occupied, Text symbole) {
        super(x, y, occupied);
        this.symbole = symbole;
    }

    public Text getSymbole() {
        return symbole;
    }

    public void setSymbole(Text symbole) {
        this.symbole = symbole;
    }
}

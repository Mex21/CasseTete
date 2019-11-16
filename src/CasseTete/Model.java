package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
import CasseTete.Cell.CellSymbol;

import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements Observer {
    private Controller controller = new Controller();

    private int lastX, lastY;
    private String O = "S1";
    private String X = "S2";
    private Cell[][] board;

    public Model(int x, int y) {
        this.board = GenerateBoard(x, y);

    }

    private void CreateEmptyBoard(int x, int y) {
        board = new Cell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                board[i][j] = new CellPath(i, j, false,"E");
            }
        }
    }

    private void GenerateRandomSymbol() {
        //Random Fonction to do
        board[0][0] = new CellSymbol(0, 0, O);
        board[0][2] = new CellSymbol(0, 2, O);
        board[1][2] = new CellSymbol(1, 2, X);
        board[1][1] = new CellSymbol(1, 1, X);
    }

    public Cell[][] GenerateBoard(int x, int y) {
        CreateEmptyBoard(x, y);
        GenerateRandomSymbol();
        setChanged();
        notifyObservers();
        return getBoard();
    }


    public void startDD(int x, int y) {
        System.out.println("startDD : " + x + "-" + y);
        Cell[][] board = getBoard();
        if (board[x][y] instanceof CellSymbol) {
        }
        CellSymbol cellSymbol = new CellSymbol(x, y, "P");
        setChanged();
        notifyObservers(cellSymbol);
    }

    public void stopDD(int x, int y) {
        System.out.println("stopDD : " + x + "-" + y + " -> " + lastX + "-" + lastY);
        setChanged();
        notifyObservers();
    }

    public void parcoursDD(int x, int y) {
        // TODO
        lastX = x;
        lastY = y;
        System.out.println("parcoursDD : " + x + "-" + y);
        CellSymbol cellSymbol = new CellSymbol(x, y, "P");
        setChanged();
        notifyObservers(cellSymbol);
    }

    public Cell[][] getBoard() {
        return board;
    }

    @Override
    public void update(Observable o, Object arg) {
        int x = ((CellPath)arg).getX();
        int y = ((CellPath)arg).getY();
        parcoursDD(x,y);
    }
}

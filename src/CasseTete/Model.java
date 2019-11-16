package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellEmpty;
import CasseTete.Cell.CellSymbol;

import java.util.Observable;

public class Model extends Observable {

    private int lastX, lastY;
    private String O = "O";
    private String X = "X";
    private Cell[][] board;

    public Model(int x,int y) {
        this.board = GenerateBoard(x,y);
    }

    private void CreateEmptyBoard(int x, int y) {
        board = new Cell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                board[i][j] = new CellEmpty(i, j, false, "E");
            }
        }
    }

    private void GenerateRandomSymbol() {
        //Random Fonction to do
        board[0][0] = new CellSymbol(0, 0, true, O);
        board[0][2] = new CellSymbol(0, 2, true, O);
        board[1][2] = new CellSymbol(1, 2, true, X);
        board[1][1] = new CellSymbol(1, 1, true, X);
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
        if (board[x][y] instanceof CellSymbol){
            System.out.println("C'est un symbole");
        }
        setChanged();
        notifyObservers();
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
        setChanged();
        notifyObservers();
    }

    public Cell[][] getBoard() {
        return board;
    }
}

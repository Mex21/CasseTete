package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
import CasseTete.Cell.CellSymbol;

import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {

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

    private Cell[][] GenerateBoard(int x, int y) {
        CreateEmptyBoard(x, y);
        GenerateRandomSymbol();
        setChanged();
        notifyObservers();
        return getBoard();
    }

    private void GenerateRandomSymbol() {
        //Random Fonction to do
        board[0][0] = new CellSymbol(0, 0, O);
        board[0][2] = new CellSymbol(0, 2, O);
        board[1][2] = new CellSymbol(1, 2, X);
        board[1][1] = new CellSymbol(1, 1, X);
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
        setChanged();
        notifyObservers(cellSymbol);
    }

    public Cell[][] getBoard() {
        return board;
    }


            /*int x = ((Coordinate)arg).getX();
            int y = ((Coordinate)arg).getY();
            int type = ((Coordinate)arg).getType();
            Cell cell = board[x][y];
            System.out.println("Update");
            switch (type){
                case 1:
                    if (cell instanceof CellSymbol){
                        System.out.println("C'est un symbole");
                    }else if (cell instanceof CellPath){
                        System.out.println("Commencez le chemin sur un symbole !!");
                    }
                    break;
                case 2:
                    if (cell instanceof CellSymbol){
                        System.out.println("C'est un symbol");
                    }else if (cell instanceof CellPath){
                        System.out.println("Tracez Chemin");
                    }
                    break;
                case 3:
                    if (cell instanceof CellSymbol){
                        System.out.println("Bien");
                    }else if (cell instanceof CellPath){
                        System.out.println("Vous devez finir sur un symbole");
                    }
                    break;
            }*/
    }

package CasseTete;

import javafx.scene.text.Text;

import java.util.Observable;

public class Model extends Observable {

    private int lastC, lastR;
    private Text textO = new Text("O");
    private Text textX = new Text("X");


    private Cell[][] CreateEmptyBoard(int x, int y) {
        Cell[][] boardTab = new Cell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Text textEmpty = new Text("E");
                boardTab[i][j] = new CellEmpty(i, j,false,textEmpty);
            }
        }
        return boardTab;
    }


    private Cell[][] GenerateRandomSymbol(Cell[][] boardTab) {
        //Random Fonction to do
        boardTab[0][0] = new CellSymbol(0,0,true,new Text("O"));
        boardTab[0][2] = new CellSymbol(0,2,true,new Text("O"));
        boardTab[1][2] = new CellSymbol(1,2,true,new Text("X"));
        boardTab[1][1] = new CellSymbol(1,1,true,new Text("X"));
        return boardTab;
    }

    public Cell[][] GenerateBoard(int x, int y) {
        Cell[][] board = CreateEmptyBoard(x, y);
        GenerateRandomSymbol(board);
        return board;
    }


    public void startDD(int c, int r) {
        System.out.println("startDD : " + c + "-" + r);
        setChanged();
        notifyObservers();
    }

    public void stopDD(int c, int r) {
        // mémoriser le dernier objet renvoyé par parcoursDD pour connaitre la case de relachement

        System.out.println("stopDD : " + c + "-" + r + " -> " + lastC + "-" + lastR);
        setChanged();
        notifyObservers();
    }

    public void parcoursDD(int c, int r) {
        // TODO
        lastC = c;
        lastR = r;
        System.out.println("parcoursDD : " + c + "-" + r);
        setChanged();
        notifyObservers();
    }
}

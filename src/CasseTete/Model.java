package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
import CasseTete.Cell.CellSymbol;

import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {

    private int lastX, lastY;
    private String O = "S1";
    private String X = "S2";
    private Cell[][] board, boardTemp;
    private ArrayList<Cell> pathList, pathListTemp;

    public Model(int x, int y) {
        this.board = GenerateBoard(x, y);
    }

    private void CreateEmptyBoard(int x, int y) {
        board = new Cell[x][y];
        PathCoordinate p = new PathCoordinate(0, 0);
        pathList = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                board[i][j] = new CellPath(i, j, p, p);
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
        //System.out.println("startDD : " + x + "-" + y);
        boardTemp = board.clone();
        pathListTemp = new ArrayList<>();
        Cell cell = board[x][y];
        if (cell instanceof CellSymbol) {
            pathListTemp.add(cell);
        }
    }

    public void stopDD(int x, int y) {
        //System.out.println("stopDD : " + x + "-" + y + " -> " + lastX + "-" + lastY);
        int size = pathListTemp.size();
        Cell cell = pathListTemp.get(size);
        if (cell instanceof CellSymbol){
            board = boardTemp.clone();
            pathList.addAll(pathListTemp);
        }else {
            //fonction effacer !!!
        }
    }

    private boolean isEmpty(Cell c) {
        if (c instanceof CellPath) {
            //System.out.println("derp");
            return (((CellPath) c).getPathEntry().getX()) == 0 & ((CellPath) c).getPathEntry().getY() == 0 & ((CellPath) c).getPathExit().getX() == 0 & ((CellPath) c).getPathExit().getY() == 0;
        }
        //System.out.println("henlo");
        return false;
    }

    public void parcoursDD(int x, int y) {

        CellPath cellPath = null;
        System.out.println("parcoursDD1 : " + x + "-" + y);
        if (isEmpty(boardTemp[x][y])) {
            lastX = x;
            lastY = y;
            int listLength = pathListTemp.size();
            if (listLength == 0) {
                System.out.println("Veuillez commencer par un chemin !!!");
            } else if (listLength == 1) {
                Cell previousCell = pathListTemp.get(listLength - 1);
                if (previousCell instanceof CellSymbol) {
                    int previousX = previousCell.getX();
                    int previousY = previousCell.getY();
                    int nextX = previousX - x;
                    int nextY = y - previousY;
                    System.out.println("Path :" + nextX + "/" + nextY);
                    PathCoordinate pathX = new PathCoordinate(nextX, nextY);
                    PathCoordinate pathY = new PathCoordinate(0, 0);
                    cellPath = new CellPath(x, y, pathX, pathY);
                    boardTemp[x][y] = cellPath;
                    setChanged();
                    notifyObservers(cellPath);
                }
            } else {
                // Function to erase path !!!
            }
        } else if (boardTemp[x][y] instanceof CellSymbol) {
            System.out.println(((CellSymbol) pathListTemp.get(0)).getSymbol());
            System.out.println(((CellSymbol) boardTemp[x][y]).getSymbol());
            if (!checkSymbol((((CellSymbol) pathListTemp.get(0)).getSymbol()), (((CellSymbol) boardTemp[x][y]).getSymbol()))) {
                System.out.println("WTF ERROR");
            }

        }
        System.out.println("parcoursDD2 : " + x + "-" + y);

    }

    private boolean checkSymbol(String s, String v) {
        return s.equals(v);
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

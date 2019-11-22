package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
import CasseTete.Cell.CellSymbol;

import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {
    private String O = "S1";
    private String X = "S2";
    private Cell[][] board;
    private Cell[][] boardTemp;
    private ArrayList<Cell> pathList, pathListTemp;
    private static int BOARDSIZE;


    public Model(int x, int y) {
        this.board = GenerateBoard(x, y);
    }

    private void CreateEmptyBoard(int x, int y) {
        BOARDSIZE = x;
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
        //setChanged();
        //notifyObservers();
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
        //boardTemp = board.clone();
        boardTemp = new Cell[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) {
            System.arraycopy(board[i], 0, boardTemp[i], 0, BOARDSIZE);
        }
        //System.arraycopy(board,0,boardTemp,0,boardTemp.length);
        //System.out.println("StartDD");
        //System.out.println(getStringBoard(board));
        pathListTemp = new ArrayList<>();
        Cell cell = board[x][y];
        if (cell instanceof CellSymbol) {
            pathListTemp.add(cell);
        }
    }

    public void stopDD(int x, int y) {
        int size = pathListTemp.size();
        Cell cell = pathListTemp.get(size - 1);
        if (cell instanceof CellSymbol) {
            for (int i = 0; i < BOARDSIZE; i++) {
                System.arraycopy(boardTemp[i], 0, board[i], 0, BOARDSIZE);
            }
            pathList.addAll(pathListTemp);
        } else {
            for (int i = 0; i < pathListTemp.size(); i++) {
                Cell c = pathListTemp.get(i);
                if (c instanceof CellPath) {
                    ((CellPath) c).getPathExit().setX(0);
                    ((CellPath) c).getPathExit().setY(0);
                    ((CellPath) c).getPathEntry().setX(0);
                    ((CellPath) c).getPathEntry().setY(0);
                    setChanged();
                    notifyObservers(c);
                }
            }
        }
    }

    private boolean isEmpty(Cell c) {
        if (c instanceof CellPath) {
            return (((CellPath) c).getPathEntry().getX()) == 0 & ((CellPath) c).getPathEntry().getY() == 0 & ((CellPath) c).getPathExit().getX() == 0 & ((CellPath) c).getPathExit().getY() == 0;
        }
        return false;
    }

    public void parcoursDD(int x, int y) {
        //System.out.println(getStringBoard(boardTemp));
        //System.out.println(getStringBoard(board));
        //System.out.println(((CellPath)board[2][0]).getPathEntry().getX());
        //System.out.println(((CellPath)boardTemp[2][0]).getPathEntry().getX());
        System.out.println(pathListTemp.size());
        CellPath cellPath;
        Cell currentCell = boardTemp[x][y];
        //System.out.println("parcoursDD1 : " + x + "-" + y);
        if (isEmpty(boardTemp[x][y])) {
            int listLength = pathListTemp.size();
            //System.out.println(listLength);

            if (listLength == 0) {
                System.out.println("Veuillez commencer par un chemin !!!");
            }
            else if (listLength == 1) {
                Cell previousCell = pathListTemp.get(listLength - 1);
                if (AcceptedJump(previousCell,currentCell)){
                    if (previousCell instanceof CellSymbol) {
                        int previousX = previousCell.getX();
                        int previousY = previousCell.getY();
                        int jumpX = previousX - x;
                        int jumpY = y - previousY;
                        System.out.println("Path :" + jumpX + "/" + jumpY);
                        PathCoordinate pathX = new PathCoordinate(jumpX, jumpY);
                        PathCoordinate pathY = new PathCoordinate(0, 0);
                        cellPath = new CellPath(x, y, pathX, pathY);
                        boardTemp[x][y] = cellPath;
                        pathListTemp.add(cellPath);
                        setChanged();
                        notifyObservers(cellPath);
                    }
                }
            }
            else {
                Cell previousCell = pathListTemp.get(listLength - 1);
                if (AcceptedJump(previousCell,currentCell)){
                int previousX = previousCell.getX();
                int previousY = previousCell.getY();
                int jumpX = previousX - x;
                int jumpY = y - previousY;
                System.out.println("Path 2 :" + jumpX + "/" + jumpY);
                PathCoordinate pathEntry = new PathCoordinate(jumpX, jumpY);
                PathCoordinate pathExit = new PathCoordinate(0, 0);
                cellPath = new CellPath(x, y, pathEntry, pathExit);
                boardTemp[x][y] = cellPath;
                pathListTemp.add(cellPath);
                setChanged();
                notifyObservers(cellPath);

                int jumpPreviousX = x - previousX;
                int jumpPreviousY = previousY - y;
                ((CellPath) previousCell).getPathExit().setX(jumpPreviousX);
                ((CellPath) previousCell).getPathExit().setY(jumpPreviousY);
                setChanged();
                notifyObservers(previousCell);
                }
            }


        } else if (currentCell instanceof CellSymbol) {
            //System.out.println(((CellSymbol) pathListTemp.get(0)).getSymbol());
            //System.out.println(((CellSymbol) boardTemp[x][y]).getSymbol());
            if (pathListTemp.size() == 0) {
                pathListTemp.add(currentCell);
            }
            if (pathListTemp.size() > 1) {
                if (!checkSymbol((((CellSymbol) pathListTemp.get(0)).getSymbol()), (((CellSymbol) boardTemp[x][y]).getSymbol()))) {
                    System.out.println("Veuillez finir par le même symbole !!!");
                } else {
                    pathListTemp.add(currentCell);
                }
            }

        } else {
            //Delete path
        }
        //System.out.println("parcoursDD2 : " + x + "-" + y);

    }

    private boolean checkSymbol(String s, String v) {
        return s.equals(v);
    }

    public Cell[][] getBoard() {
        return board;
    }

    public String getStringBoard(Cell[][] board) {
        StringBuilder s = new StringBuilder("{");
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                Cell cell = board[i][j];
                if (cell instanceof CellSymbol) {
                    s.append(((CellSymbol) cell).getSymbol());
                    s.append(" , ");
                } else {
                    if (((CellPath) cell).getPathEntry().getY() == 0 & ((CellPath) cell).getPathEntry().getX() == 0 & ((CellPath) cell).getPathExit().getY() == 0 & ((CellPath) cell).getPathExit().getX() == 0) {
                        s.append("Empty , ");
                    } else {
                        s.append("path , ");
                    }
                }
            }
        }
        return s.toString();
    }

    private boolean AcceptedJump(Cell previousCell, Cell currentCell) {
        int jumpX = Math.abs(previousCell.getX() - currentCell.getX());
        int jumpY = Math.abs(previousCell.getY() - currentCell.getY());
        return jumpX < 2 & jumpY < 2;
    }
}

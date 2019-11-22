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
        boardTemp = new Cell[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) {
            System.arraycopy(board[i], 0, boardTemp[i], 0, BOARDSIZE);
        }
        pathListTemp = new ArrayList<>();
        Cell cell = board[x][y];
        if (cell instanceof CellSymbol && !CellInsidePathList(cell)) {
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
        //System.out.println(pathListTemp.size());
        CellPath cellPath;
        Cell currentCell = boardTemp[x][y];
        System.out.println("parcoursDD1 : " + x + "-" + y);
        if (isEmpty(boardTemp[x][y])) {
            int listLength = pathListTemp.size();
            System.out.println("Taille de la list : " + listLength);
            if (listLength == 0) {
                System.out.println("Veuillez commencer par un chemin !!!");
            } else if (listLength == 1) {
                Cell previousCell = pathListTemp.get(listLength - 1);
                if (AcceptedJump(previousCell, currentCell)) {
                    if (previousCell instanceof CellSymbol) {
                        cellPath = GeneratePath(x, y, previousCell);
                        boardTemp[x][y] = cellPath;
                        pathListTemp.add(cellPath);
                        setChanged();
                        notifyObservers(cellPath);
                    }
                }
            } else {
                Cell previousCell = pathListTemp.get(listLength - 1);
                System.out.println(previousCell.toString());
                System.out.println(currentCell.toString());
                System.out.println();
                if (AcceptedJump(previousCell, currentCell) && !CellSymbolInsidePathListTemp(previousCell)) {
                    cellPath = GeneratePath(x, y, previousCell);
                    boardTemp[x][y] = cellPath;
                    pathListTemp.add(cellPath);
                    setChanged();
                    notifyObservers(cellPath);
                    ModifyPreviousCell(x, y, (CellPath) previousCell);
                    setChanged();
                    notifyObservers(previousCell);
                }
            }
        } else if (currentCell instanceof CellSymbol && !CellInsidePathList(currentCell)) {
            //System.out.println(((CellSymbol) pathListTemp.get(0)).getSymbol());
            //System.out.println(((CellSymbol) boardTemp[x][y]).getSymbol());
            if (pathListTemp.size() == 0) {
                pathListTemp.add(currentCell);
            }
            if (pathListTemp.size() > 1) {
                if (!checkSymbol((CellSymbol) pathListTemp.get(0), (CellSymbol) boardTemp[x][y])) {
                    System.out.println("Veuillez finir par le même symbole !!!");
                } else {
                    System.out.println("Symbol ajouter pathlisttemp");
                    int listLength = pathListTemp.size();
                    Cell previousCell = pathListTemp.get(listLength - 1);
                    ModifyPreviousCell(x, y, (CellPath) previousCell);
                    pathListTemp.add(currentCell);
                    setChanged();
                    notifyObservers(previousCell);
                }
            }

        } else {
           /* if (currentCell instanceof CellPath) {
                if (CellPathInsidePathListTemp(currentCell)) {
                    if ((((CellPath) currentCell).getPathExit().getX() != 0 && ((CellPath) currentCell).getPathExit().getY() != 0)) {
                        pathListTemp.remove(pathListTemp.size());
                        Cell cell = new CellPath(x, y, new PathCoordinate(0, 0), new PathCoordinate(0, 0));
                        setChanged();
                        notifyObservers(cell);
                    }
                }
            }*/
        }
    }

    private boolean checkSymbol(CellSymbol c1, CellSymbol c2) {
        //System.out.println(c1.getX() + "/" + c2.getX());
        //System.out.println(c1.getY() + "/" + c2.getY());
        //System.out.println(c1.getSymbol() + "/" + c2.getSymbol());
        return ((c1.getX() != c2.getX() || c1.getY() != c2.getY()) && c1.getSymbol().equals(c2.getSymbol()));
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
        int jump = jumpX + jumpY;
        return jump < 2;
    }

    private CellPath GeneratePath(int x, int y, Cell previousCell) {
        CellPath cellPath;
        int previousX = previousCell.getX();
        int previousY = previousCell.getY();
        int jumpX = previousX - x;
        int jumpY = y - previousY;
        //System.out.println("Path :" + jumpX + "/" + jumpY);
        PathCoordinate pathX = new PathCoordinate(jumpX, jumpY);
        PathCoordinate pathY = new PathCoordinate(0, 0);
        cellPath = new CellPath(x, y, pathX, pathY);
        return cellPath;
    }


    private void ModifyPreviousCell(int x, int y, CellPath previousCell) {
        int jumpPreviousX = x - previousCell.getX();
        int jumpPreviousY = previousCell.getY() - y;
        (previousCell).getPathExit().setX(jumpPreviousX);
        (previousCell).getPathExit().setY(jumpPreviousY);
    }

    // Return true if @cell is inside @pathList
    private boolean CellInsidePathList(Cell cell) {
        for (Cell value : pathList) {
            if (cell.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean CellSymbolInsidePathListTemp(Cell cell) {
        if (cell instanceof CellSymbol) {
            for (Cell value : pathListTemp) {
                if (cell.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean CellPathInsidePathListTemp(Cell cell) {
        if (cell instanceof CellPath) {
            for (Cell value : pathListTemp) {
                if (cell.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}

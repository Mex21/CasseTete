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
    private static int BOARDSIZE_X;
    private static int BOARDSIZE_Y;
    private static int NBRSYMBOL = 4;


    public Model(int x, int y) {
        this.board = GenerateBoard(x, y);
    }

    private void CreateEmptyBoard(int x, int y) {
        BOARDSIZE_X = x;
        BOARDSIZE_Y = y;
        board = new Cell[x][y];
        PathCoordinate p = new PathCoordinate(0, 0);
        pathList = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                CellPath cell = new CellPath(i, j, p, p);
                board[i][j] = cell;
                setChanged();
                notifyObservers(cell);
            }
        }
    }

    public Cell[][] GenerateBoard(int x, int y) {
        CreateEmptyBoard(x, y);
        GenerateRandomSymbol();
        return getBoard();
    }

    private void GenerateRandomSymbol() {
        //Random Fonction to do
        CellSymbol cell = new CellSymbol(0, 0, O);
        board[0][0] = cell;
        setChanged();
        notifyObservers(cell);
        cell = new CellSymbol(0, 2, O);
        board[0][2] = cell;
        setChanged();
        notifyObservers(cell);
        cell = new CellSymbol(1, 0, X);
        board[1][0] = cell;
        setChanged();
        notifyObservers(cell);
        cell = new CellSymbol(2, 2, X);
        board[2][2] = cell;
        setChanged();
        notifyObservers(cell);
    }

    public void startDD(int x, int y) {
        boardTemp = new Cell[BOARDSIZE_X][BOARDSIZE_Y];
        for (int i = 0; i < BOARDSIZE_X; i++) {
            System.arraycopy(board[i], 0, boardTemp[i], 0, BOARDSIZE_Y);
        }
        pathListTemp = new ArrayList<>();
        Cell cell = board[x][y];
        if (cell instanceof CellSymbol && !CellInsidePathList(cell)) {
            pathListTemp.add(cell);
            System.out.println(cell.toString());
        }
    }

    public void stopDD() {
        int size = pathListTemp.size();
        if (size > 0) {
            Cell cell = pathListTemp.get(size - 1);
            if (cell instanceof CellSymbol) {
                for (int i = 0; i < BOARDSIZE_X; i++) {
                    System.arraycopy(boardTemp[i], 0, board[i], 0, BOARDSIZE_Y);
                }
                pathList.addAll(pathListTemp);
                hasWon();
            } else {
                for (Cell c : pathListTemp) {
                    deleteCellPath(c);
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
        Cell previousCell;
        Cell currentCell = boardTemp[x][y];
        int listSize = pathListTemp.size();
        if (listSize > 0) {
            previousCell = pathListTemp.get(listSize - 1);
        } else {
            previousCell = null;
        }
        //System.out.println("parcoursDD1 : " + x + "-" + y);
        if (isEmpty(boardTemp[x][y])) {
            System.out.println("Taille de la list : " + listSize);
            if (previousCell == null) {
                System.out.println("Veuillez commencer par un chemin !!!");
            } else if (previousCell instanceof CellSymbol) {
                if (AcceptedJump(previousCell, currentCell) && listSize < 2) {
                    cellPath = GeneratePath(x, y, previousCell);
                    boardTemp[x][y] = cellPath;
                    pathListTemp.add(cellPath);
                    System.out.println(cellPath.toString());
                    setChanged();
                    notifyObservers(cellPath);
                }
            } else {
                // On peux enlever cellsymbolinsidepath et plutot verifier si la list fait plus de 3 !!!
                if (AcceptedJump(previousCell, currentCell) && !CellSymbolInsidePathListTemp(previousCell)) {
                    cellPath = GeneratePath(x, y, previousCell);
                    boardTemp[x][y] = cellPath;
                    pathListTemp.add(cellPath);
                    System.out.println(cellPath.toString());
                    setChanged();
                    notifyObservers(cellPath);
                    ModifyPreviousCell(x, y, (CellPath) previousCell);
                    setChanged();
                    notifyObservers(previousCell);
                }
            }
        } else if (currentCell instanceof CellSymbol && !CellInsidePathList(currentCell) && AcceptedJump(previousCell, currentCell)) {
            //System.out.println(((CellSymbol) pathListTemp.get(0)).getSymbol());
            //System.out.println(((CellSymbol) boardTemp[x][y]).getSymbol());
            if (pathListTemp.size() == 0) {
                pathListTemp.add(currentCell);
                System.out.println(currentCell.toString());
            }
            if (pathListTemp.size() > 1) {
                if (!checkSymbol((CellSymbol) pathListTemp.get(0), (CellSymbol) boardTemp[x][y])) {
                    System.out.println("Veuillez finir par le même symbole !!!");
                } else {
                    //System.out.println("Symbol ajouter pathlisttemp");
                    int listLength = pathListTemp.size();
                    previousCell = pathListTemp.get(listLength - 1);
                    ModifyPreviousCell(x, y, (CellPath)previousCell);
                    pathListTemp.add(currentCell);
                    System.out.println(currentCell.toString());
                    setChanged();
                    notifyObservers(previousCell);
                }
            }

        } else if (currentCell instanceof CellPath && currentCell == pathListTemp.get(listSize - 2) && !(previousCell instanceof CellSymbol)) {

            CellPath lastCell = (CellPath) pathListTemp.get(listSize - 1);
            ((CellPath) currentCell).setPathExit(new PathCoordinate(0, 0));
            pathListTemp.set(listSize - 2, currentCell);

            pathListTemp.remove(pathListTemp.size() - 1);
            removeCellPath(lastCell);

            setChanged();
            notifyObservers(currentCell);
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
        for (int i = 0; i < BOARDSIZE_X; i++) {
            for (int j = 0; j < BOARDSIZE_Y; j++) {
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
        previousCell.getPathExit().setX(jumpPreviousX);
        previousCell.getPathExit().setY(jumpPreviousY);
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

    public void OnClickUndo() {
        int listSize = pathList.size();
        int compCellSymbol = 0;
        for (int i = listSize - 1; i >= 0; i--) {
            Cell cell = pathList.get(i);
            if (cell instanceof CellSymbol && compCellSymbol < 2) {
                compCellSymbol++;
                pathList.remove(pathList.size() - 1);
            } else if (cell instanceof CellPath) {
                deleteCellPath(cell);
                resetCellPath((CellPath) cell);
                System.out.println(cell.toString());
                board[cell.getX()][cell.getY()] = cell;
                pathList.remove(pathList.size() - 1);
            } else {
                break;
            }
        }
        System.out.println(pathList.size());

    }

    private void deleteCellPath(Cell c) {
        if (c instanceof CellPath) {
            resetCellPath((CellPath) c);
            setChanged();
            notifyObservers(c);
        }
    }

    private void removeCellPath(CellPath cellPath) {
        int x = cellPath.getX();
        int y = cellPath.getY();
        resetCellPath(cellPath);
        board[x][y] = cellPath;
        setChanged();
        notifyObservers(cellPath);
    }

    private void resetCellPath(CellPath cell) {
        cell.getPathExit().setX(0);
        cell.getPathExit().setY(0);
        cell.getPathEntry().setX(0);
        cell.getPathEntry().setY(0);

    }

    private void hasWon() {
        /*for(int i = 0 ; i<= BOARDSIZE_X; i++){
            for(int j = 0; j<=BOARDSIZE_Y; j++){
                if(!isEmpty(board[i][j])){
                    if((((CellSymbol) pathList.get(0)).getSymbol().equals(((CellSymbol) board[i][j]).getSymbol()))  && (((CellSymbol)board[i][j]).getSymbol()).equals(((CellSymbol) pathList.lastIndexOf((CellSymbol))))) {
                        System.out.println("Hello world");
                    }
                }
            }
        }*/
        int listSize = pathList.size();
        int boardSize = BOARDSIZE_X * BOARDSIZE_Y;
        int compCellSymbol = 0;
        for (Cell cell : pathList) {
            if (cell instanceof CellSymbol) {
                compCellSymbol++;
            }
        }
        System.out.println(listSize + "/" + boardSize + "/" + compCellSymbol + "/" + NBRSYMBOL);
        if (listSize == boardSize && compCellSymbol == NBRSYMBOL) {
            System.out.println("Hello world");
            setChanged();
            notifyObservers(1);
        }
    }
}

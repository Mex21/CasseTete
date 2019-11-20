package CasseTete;

import CasseTete.Cell.Cell;
import CasseTete.Cell.CellPath;
import CasseTete.Cell.CellSymbol;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {

    private int lastX, lastY;
    private String O = "S1";
    private String X = "S2";
    private Cell[][] board;
    private ArrayList<Cell> pathList;
    public Model(int x, int y) {
        this.board = GenerateBoard(x, y);

    }

    private void CreateEmptyBoard(int x, int y) {
        board = new Cell[x][y];
        pathList = new ArrayList();
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                board[i][j] = new CellPath(i, j,PathCoordinate.CUR, PathCoordinate.CUR);
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

    public boolean isEmpty(Cell c) {
    	if(c instanceof CellPath) {
    		if((((CellPath)c).getPathX()) == PathCoordinate.CUR && ((CellPath)c).getPathY() == PathCoordinate.CUR) {
    			System.out.println("derp");
    			return true;
    			
    		}
    	}
    	System.out.println("henlo");
    	return false;
    }

    public void startDD(int x, int y) {
        System.out.println("startDD : " + x + "-" + y);
        Cell[][] board = getBoard();
        Cell cell = board[x][y];
        if (cell instanceof CellSymbol) {
        	System.out.println("NewList");
        	pathList.add(cell);
        }
       
    }

    public void stopDD(int x, int y) {
        System.out.println("stopDD : " + x + "-" + y + " -> " + lastX + "-" + lastY);
        setChanged();
        notifyObservers();
    }

    public void parcoursDD(int x, int y) {
        // TODO
    	CellPath cellPath = null;
    	if(isEmpty(board[x][y])) {
    			lastX = x;
    			lastY = y;
    			int listLength = pathList.size()-1;
    			Cell previousCell = pathList.get(listLength);
            if(previousCell instanceof CellSymbol) {
            	int previousX = previousCell.getX();
            	int previousY = previousCell.getY();
            	int nextX = x - previousX;
            	int nextY = y - previousY;
            	PathCoordinate pathX = PathCoordinate.NEG;
                PathCoordinate pathY = PathCoordinate.CUR;
                cellPath = new CellPath(x, y, pathX, pathY);
                board[x][y] = cellPath;
            }
    	}
            System.out.println("parcoursDD : " + x + "-" + y);
            
            setChanged();
            notifyObservers(cellPath);
        	
    	}
        

    public Cell[][] getBoard() {
        return board;
    }

    
}

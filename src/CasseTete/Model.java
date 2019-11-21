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
    private Cell[][] board;
    private ArrayList<Cell> pathList;
    public Model(int x, int y) {
        this.board = GenerateBoard(x, y);

    }

    private void CreateEmptyBoard(int x, int y) {
        board = new Cell[x][y];
        pathList = new ArrayList<>();
        PathCoordinate p = new PathCoordinate(0,0);
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                board[i][j] = new CellPath(i, j,p,p);
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

    private Cell[][] GenerateBoard(int x, int y) {
        CreateEmptyBoard(x, y);
        GenerateRandomSymbol();
        setChanged();
        notifyObservers();
        return getBoard();
    }

    private boolean isEmpty(Cell c) {
    	if(c instanceof CellPath) {
    		if((((CellPath)c).getPathEntry().getX()) == 0 & ((CellPath)c).getPathEntry().getY() == 0 & ((CellPath)c).getPathExit().getX() == 0 & ((CellPath)c).getPathExit().getY() == 0) {
    			//System.out.println("derp");
    			return true;
    			
    		}
    	}
    	//System.out.println("henlo");
    	return false;
    }

    public void startDD(int x, int y) {
        //System.out.println("startDD : " + x + "-" + y);
        Cell[][] board = getBoard();
        Cell cell = board[x][y];
        if (cell instanceof CellSymbol) {
        	//System.out.println("NewList");
        	pathList.add(cell);
        }
    }

    public void stopDD(int x, int y) {
        System.out.println("stopDD : " + x + "-" + y + " -> " + lastX + "-" + lastY);
        setChanged();
        notifyObservers();
    }

    public void parcoursDD(int x, int y) {
    	CellPath cellPath = null;
        System.out.println("parcoursDD1 : " + x + "-" + y);
    	if(isEmpty(board[x][y])) {
            lastX = x;
            lastY = y;
            int listLength = pathList.size() - 1;
            System.out.println(listLength);
            Cell previousCell = pathList.get(listLength);
            if (previousCell instanceof CellSymbol) {
                int previousX = previousCell.getX();
                int previousY = previousCell.getY();
                int nextX = x - previousX;
                int nextY = y - previousY;
                PathCoordinate pathX = new PathCoordinate(0,1);
                PathCoordinate pathY = new PathCoordinate(0,0);
                cellPath = new CellPath(x, y, pathX, pathY);
                board[x][y] = cellPath;
            }
        }
    	else if(board[x][y] instanceof CellSymbol) {
    		System.out.println(((CellSymbol) pathList.get(0)).getSymbol());
    		System.out.println(((CellSymbol) board[x][y]).getSymbol());
    		if(checkSymbol((((CellSymbol) pathList.get(0)).getSymbol()), (((CellSymbol) board[x][y]).getSymbol())) == false) {
    	        System.out.println("WTF ERROR");
    	       }
    	    		
    	}
    	System.out.println("parcoursDD2 : " + x + "-" + y);
            
        setChanged();
    	notifyObservers(cellPath);
        	
    	}
        
    public boolean checkSymbol(String s, String v) {
    	if(s != v ) {
    		return false;
    	}
    	return true;
    }

    public Cell[][] getBoard() {
        return board;
    }

    
}

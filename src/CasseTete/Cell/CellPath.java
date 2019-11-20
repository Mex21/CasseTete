package CasseTete.Cell;

import CasseTete.PathCoordinate;

public class CellPath extends Cell {
    
	PathCoordinate pathX, pathY;

    public PathCoordinate getPathX() {
		return pathX;
	}

	public void setPathX(PathCoordinate pathX) {
		this.pathX = pathX;
	}

	public PathCoordinate getPathY() {
		return pathY;
	}

	public void setPathY(PathCoordinate pathY) {
		this.pathY = pathY;
	}

	public CellPath(int x, int y,PathCoordinate pathX, PathCoordinate pathY) {
        super(x, y);
        this.pathX  = pathX;
        this.pathY = pathY;
        
    }
}

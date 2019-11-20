package CasseTete.Cell;

import CasseTete.PathCoordinate;

public class CellPath extends Cell {
    
	private PathCoordinate pathEntry, pathExit;

    public PathCoordinate getPathEntry() {
		return pathEntry;
	}

	public void setPathX(PathCoordinate pathEntry) {
		this.pathEntry = pathEntry;
	}

	public PathCoordinate getPathExit() {
		return pathExit;
	}

	public void setPathY(PathCoordinate pathEntry) {
		this.pathEntry = pathEntry;
	}

	public CellPath(int x, int y,PathCoordinate pathEntry, PathCoordinate pathExit) {
        super(x, y);
        this.pathEntry  = pathEntry;
        this.pathExit = pathExit;
        
    }
}

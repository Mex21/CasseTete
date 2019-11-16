package CasseTete.Cell;


public class CellPath extends Cell {
    private boolean occupied;

    public CellPath(int x, int y, boolean occupied, String pathType) {
        super(x, y, pathType);
        this.occupied = occupied;
    }
}

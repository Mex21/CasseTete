package CasseTete;

public abstract class Cell {
    private int x, y;
    private boolean occupied;

    public Cell(int x, int y, boolean occupied) {
        this.x = x;
        this.y = y;
        this.occupied = occupied;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}

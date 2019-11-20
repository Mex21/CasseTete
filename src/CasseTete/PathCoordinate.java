package CasseTete;

public enum PathCoordinate {
    NEG(-1),
    CUR(0),
    POS(1);

    private final int value;

    PathCoordinate(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}


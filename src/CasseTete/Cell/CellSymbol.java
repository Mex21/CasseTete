package CasseTete.Cell;


public class CellSymbol extends Cell {
    private String symbol;

    public CellSymbol(int x, int y, String symbol) {
        super(x, y);
        this.symbol = symbol;
    }

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


}

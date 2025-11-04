public class Move {
    public int fromRow;
    public int fromCol;
    public int toRow;
    public int toCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    public String toString() {
        char fromCol = (char) ('A' + this.fromCol);
        int fromRow = 8 - this.fromRow;
        char toCol = (char) ('A' + this.toCol);
        int toRow = 8 - this.toRow;
        return "" + fromCol + fromRow + "-" + toCol + toRow;
    }
}

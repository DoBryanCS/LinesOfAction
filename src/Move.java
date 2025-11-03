public class Move {
    public int fromX;
    public int fromY;
    public int toX;
    public int toY;

    public Move(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public String toString() {
        char fromCol = (char) ('A' + fromY);
        int fromRow = 8 - fromX;
        char toCol = (char) ('A' + toY);
        int toRow = 8 - toX;
        return "" + fromCol + fromRow + "-" + toCol + toRow;
    }
}

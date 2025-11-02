public class Game {
    public static final int EMPTY = 0;
    public static final int BLACK = 2;
    public static final int RED = 4;

    private int[][] board = new int[8][8];

    public Game(int[][] initialBoard) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = initialBoard[i][j];
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public Game applyMove(Move move) {
        Game copy = new Game(this.board);
        int fromX = move.fromX;
        int fromY = move.fromY;
        int toX = move.toX;
        int toY = move.toY;

        int piece = copy.board[fromX][fromY];
        copy.board[fromX][fromY] = EMPTY;
        copy.board[toX][toY] = piece;

        return copy;
    }
}

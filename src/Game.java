public class Game {
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
}

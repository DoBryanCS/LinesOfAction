import java.util.ArrayList;
import java.util.List;

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
        int fromRow = move.fromRow;
        int fromCol = move.fromCol;
        int toRow = move.toRow;
        int toCol = move.toCol;

        int piece = copy.board[fromRow][fromCol];
        copy.board[fromRow][fromCol] = EMPTY;
        copy.board[toRow][toCol] = piece;

        return copy;
    }

    public boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public int countPiecesInLine(int row, int col, int directionRow, int directionCol) {
        int count = 0;

        for (int i = -7; i <= 7; i++) {
            int currentX = row + i * directionRow;
            int currentY = col + i * directionCol;

            if (isInsideBoard(currentX, currentY) && board[currentX][currentY] != EMPTY) {
                count++;
            }
        }

        return count;
    }

    public boolean isOpponent(int piece, int player) {
        return (player == BLACK && piece == RED) || (player == RED && piece == BLACK);
    }

    public boolean hasOpponentBetween(int row, int col, int directionRow, int directionCol, int distance, int player) {
        for (int i = 1; i < distance; i++) {
            int currentRow = row + directionRow * i;
            int currentCol = col + directionCol * i;

            if (isInsideBoard(currentRow, currentCol)) {
                int piece = board[currentRow][currentCol];

                if (isOpponent(piece, player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<Move> generateMoves(int player) {
        List<Move> moves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player) {
                    int[][] directions = {
                            {-1, 0}, {1, 0}, {0, 1}, {0, -1},
                            {-1, 1}, {-1, -1}, {1, 1}, {1, -1}
                    };

                    for (int[] direction : directions) {
                        int directionRow = direction[0];
                        int directionColumn = direction[1];
                        int distance = countPiecesInLine(row, col, directionRow, directionColumn);
                        int newRow = row + directionRow * distance;
                        int newColumn = col + directionColumn * distance;

                        if (isInsideBoard(newRow, newColumn)) {
                            int target = board[newRow][newColumn];

                            if (!hasOpponentBetween(row, col, directionRow, directionColumn, distance, player)) {

                                if (target == EMPTY || isOpponent(target, player)) {
                                    moves.add(new Move(row, col, newRow, newColumn));
                                }
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }

    public int evaluate(int player) {
        double random = Math.random();

        if (random < 0.4) {
            return 100;
        } else if (random < 0.8 ) {
            return -100;
        } else {
            return 0;
        }
    }

    public String toString() {
        return "";
    }
}

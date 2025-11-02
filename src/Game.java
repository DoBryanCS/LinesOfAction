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
        int fromX = move.fromX;
        int fromY = move.fromY;
        int toX = move.toX;
        int toY = move.toY;

        int piece = copy.board[fromX][fromY];
        copy.board[fromX][fromY] = EMPTY;
        copy.board[toX][toY] = piece;

        return copy;
    }

    public boolean isIndiseBoard(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public int countPiecesInLine(int x, int y, int dx, int dy) {
        int count = 0;

        for (int i = -7; i <= 7; i++) {
            int currentX = x + i * dx;
            int currentY = y + i * dy;

            if (isIndiseBoard(currentX, currentY) && board[currentX][currentY] != EMPTY) {
                count++;
            }
        }

        return count;
    }

    public boolean isOpponent(int piece, int player) {
        return (player == BLACK && piece == RED) || (player == RED && piece == BLACK);
    }

    public boolean hasOpponentBetween(int x, int y, int dx, int dy, int distance, int player) {
        for (int i = 1; i < distance; i++) {
            int currentX = x + dx * i;
            int currentY = y + dy * i;

            if (isIndiseBoard(currentX, currentY)) {
                int piece = board[currentX][currentY];

                if (isOpponent(piece, player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<Move> generateMoves(int player) {
        List<Move> moves = new ArrayList<>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] == player) {
                    int[][] directions = {
                            {-1, 0}, {1, 0}, {0, 1}, {0, -1},
                            {-1, 1}, {-1, -1}, {1, 1}, {1, -1}
                    };

                    for (int[] direction : directions) {
                        int dx = direction[0];
                        int dy = direction[1];
                        int distance = countPiecesInLine(x, y, dx, dy);
                        int newX = x + dx * distance;
                        int newY = y + dy * distance;

                        if (isIndiseBoard(newX, newY)) {
                            int target = board[newX][newY];

                            if (!hasOpponentBetween(x, y, dx, dy, distance, player)) {

                                if (target == EMPTY || isOpponent(target, player)) {
                                    moves.add(new Move(x, y, newX, newY));
                                }
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }

    public String toString() {
        return "";
    }
}

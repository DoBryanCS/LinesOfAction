import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int EMPTY = 0;
    public static final int BLACK = 2;
    public static final int RED = 4;

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, 1}, {0, -1},
            {-1, 1}, {-1, -1}, {1, 1}, {1, -1}
    };

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

    public Game previewMove(Move move) {
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
                    for (int[] direction : DIRECTIONS) {
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
        int opponent = (player == RED) ? BLACK : RED;
        
        GroupInfo playerGroupInfo   = defineGroups(player);
        GroupInfo opponentGroupInfo = defineGroups(opponent);
        
        if (isConnected(playerGroupInfo)) {
            return 1_000_000;
        }
        if (isConnected(opponentGroupInfo)) {
            return -1_000_000;
        }

        int score = 0;
        
        score += (opponentGroupInfo.groupCount - playerGroupInfo.groupCount) * 500;
        score += (playerGroupInfo.biggestGroupSize - opponentGroupInfo.biggestGroupSize) * 40;
        
        int playerPieceDispersion   = computePieceDispersion(player);
        int opponentPieceDispersion = computePieceDispersion(opponent);
        score += (opponentPieceDispersion - playerPieceDispersion) * 15;

        return score;
    }


    private static class GroupInfo {
        int groupCount;
        int biggestGroupSize;
        int totalPieces;

        GroupInfo(int groupCount, int biggestGroupSize, int totalPieces) {
            this.groupCount = groupCount;
            this.biggestGroupSize = biggestGroupSize;
            this.totalPieces = totalPieces;
        }
    }

    private GroupInfo defineGroups(int player) {
        boolean[][] visited = new boolean[8][8];
        int groupCount = 0;
        int biggestGroupSize = 0;
        int totalPieces = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player) totalPieces++;
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player && !visited[row][col]) {
                    groupCount++;
                    biggestGroupSize = Math.max(biggestGroupSize, searchConnected(row, col, player, visited));
                }
            }
        }

        return new GroupInfo(groupCount, biggestGroupSize, totalPieces);
    }

    private int computePieceDispersion(int player) {
        int sumRow = 0;
        int sumCol = 0;
        int pieceCount  = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player) {
                    sumRow += row;
                    sumCol += col;
                    pieceCount++;
                }
            }
        }

        if (pieceCount  == 0) return 0;

        double centroidRow = (double) sumRow / pieceCount ;
        double centroidCol = (double) sumCol / pieceCount ;

        double dispersion = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player) {
                    double directionRow = row - centroidRow;
                    double directionColumn = col - centroidCol;
                    dispersion += directionRow * directionRow + directionColumn * directionColumn;
                }
            }
        }

        return (int) dispersion;
    }

    private int searchConnected(int row, int col, int player, boolean[][] visited) {
        if (!isInsideBoard(row, col) || visited[row][col] || board[row][col] != player) {
            return 0;
        }

        visited[row][col] = true;
        int size = 1;

        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            size += searchConnected(newRow, newCol, player, visited);
        }

        return size;
    }

    private boolean isConnected(GroupInfo info) {
        return info.totalPieces > 0
                && info.groupCount == 1
                && info.biggestGroupSize == info.totalPieces;
    }

    public boolean isGameOver() {
        return isConnected(defineGroups(RED)) || isConnected(defineGroups(BLACK));
    }

    public String toString() {
        return "";
    }
}

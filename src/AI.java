import java.util.List;

public class AI {
    private final int MAX_PLAYER;
    private final int MIN_PLAYER;
    private static final int MAX_DEPTH = 4 ;


    public AI(int MAX_PLAYER) {
        if (MAX_PLAYER != Game.RED && MAX_PLAYER != Game.BLACK) {
            throw new IllegalArgumentException("Invalid player color");
        }
        this.MAX_PLAYER = MAX_PLAYER;
        this.MIN_PLAYER = (MAX_PLAYER == Game.RED) ? Game.BLACK : Game.RED;
    }

    public Move getBestMove(Game game) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> moves = game.generateMoves(MAX_PLAYER);

        if (moves.isEmpty()) {
            return null;
        }

        for (Move move: moves) {
            Game newGame = game.previewMove(move);
            int score = alphaBeta(newGame, MAX_DEPTH - 1, MIN_PLAYER, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int alphaBeta(Game game, int depth, int currentPlayer, int alpha, int beta) {

        if (depth == 0 || game.isGameOver()) {
            return game.evaluate(MAX_PLAYER);
        }

        List<Move> moves = game.generateMoves(currentPlayer);
        if (moves.isEmpty()) {
            return game.evaluate(MAX_PLAYER);
        }

        if (currentPlayer == MAX_PLAYER) {
            int score = Integer.MIN_VALUE;
            for (Move m : moves) {
                Game newGame = game.previewMove(m);
                score = Math.max(score, alphaBeta(newGame, depth - 1, MIN_PLAYER, alpha, beta));
                if (score >= beta) break;
                alpha = Math.max(alpha, score);
            }
            return score;
        } else {
            int score = Integer.MAX_VALUE;

            for (Move m : moves) {
                Game newGame = game.previewMove(m);
                score = Math.min(score, alphaBeta(newGame, depth - 1, MAX_PLAYER, alpha, beta));
                if (score <= alpha) break;
                beta = Math.min(beta, score);

            }
            return score;
        }
    }
}

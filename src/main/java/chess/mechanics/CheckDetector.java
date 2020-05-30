package chess.mechanics;

import chess.mechanics.pieces.*;

import java.util.ArrayList;

public class CheckDetector {
    public enum State {none, check, checkmate}

    public static State isOpponentChecked(Board board, Cell.Colour playerColour) {
        Cell[][] cells = board.getCells();
        Cell.Colour opponentColour = playerColour == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;

        ArrayList<Move> opponentMoves = new ArrayList<>();
        for (int y = 0; y < board.getBoardSize(); ++y) {
            for (int x = 0; x < board.getBoardSize(); ++x) {
                Piece piece = cells[y][x].getPiece();
                if (piece != null && piece.getColour() == opponentColour)
                    opponentMoves.addAll(piece.getMoves());
            }
        }
        if (!isPlayerChecked(board, opponentColour))
            return State.none;
        Board afterMove = new Board(board);
        Piece captured;
        for (Move opponentMove : opponentMoves) {
            Piece piece = afterMove.getCells()[opponentMove.before.y][opponentMove.before.x].getPiece();
            Cell.Colour colour = piece.getColour();
            captured = afterMove.getCells()[opponentMove.after.y][opponentMove.after.x].getPiece();
            afterMove.getCells()[opponentMove.after.y][opponentMove.after.x].setPiece(piece);
            afterMove.getCells()[opponentMove.before.y][opponentMove.before.x].removePiece();
            if (!isPlayerChecked(afterMove, colour))
                return State.check;
            afterMove.getCells()[opponentMove.before.y][opponentMove.before.x].setPiece(afterMove.getCells()[opponentMove.after.y][opponentMove.after.x].getPiece());
            afterMove.getCells()[opponentMove.after.y][opponentMove.after.x].setPiece(captured);
        }
        return State.checkmate;
    }

    public static boolean isPlayerChecked(Board board, Cell.Colour colour) {
        board.clearMoves();
        ArrayList<Move> opponentMoves = new ArrayList<>();
        for (int y = 0; y < board.getBoardSize(); ++y) {
            for (int x = 0; x < board.getBoardSize(); ++x) {
                Piece piece = board.getCells()[y][x].getPiece();
                if (piece != null && piece.getColour() != colour)
                    opponentMoves.addAll(piece.getMoves());
            }
        }
        for (Move move : opponentMoves) {
            int y = move.after.y;
            int x = move.after.x;
            Piece piece = board.getCells()[y][x].getPiece();
            if (piece instanceof King && piece.getColour() == colour)
                return true;
        }
        return false;
    }
}

package chess.mechanics;

import chess.mechanics.pieces.*;

import java.awt.*;
import java.util.ArrayList;

public class CheckDetector {
    public enum State {none, check, checkmate}

    public static State getOpponentsState(Board board, Cell.Colour playerColour) {
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
        for (Move opponentMove : opponentMoves) {
            if (!doesMoveLeadToCheck(board, opponentMove))
                return State.check;
        }
        return State.checkmate;
    }

    public static boolean doesMoveLeadToCheck(Board board, Move move) {
        Piece piece = board.getPiece(move.before.x, move.before.y);
        Cell.Colour colour = piece.getColour();
        Piece captured = board.getPiece(move.after.x, move.after.y);
        board.getCells()[move.after.y][move.after.x].setPiece(piece);
        board.getCells()[move.before.y][move.before.x].removePiece();
        boolean answer = isPlayerChecked(board, colour);
        board.getCells()[move.before.y][move.before.x].setPiece(board.getPiece(move.after.x, move.after.y));
        board.getCells()[move.after.y][move.after.x].setPiece(captured);
        return answer;
    }

    public static boolean isPlayerChecked(Board board, Cell.Colour colour) {
        King king = board.getKing(colour);
        Point position = king.getPosition();
        int x = position.x - 1;
        int y = position.y - 1;
        while (x >= 0 && y >= 0) {
            Piece piece = board.getCells()[y--][x--].getPiece();
            if (piece != null) {
                if (piece.colour != king.colour && (piece instanceof Queen || piece instanceof Bishop))
                    return true;
                break;
            }
        }

        x = position.x - 1;
        y = position.y + 1;
        while (x >= 0 && y < board.getBoardSize()) {
            Piece piece = board.getCells()[y++][x--].getPiece();
            if (piece != null) {
                if (piece.colour != king.colour && (piece instanceof Queen || piece instanceof Bishop))
                    return true;
                break;
            }
        }

        x = position.x + 1;
        y = position.y - 1;
        while (x < board.getBoardSize() && y >= 0) {
            Piece piece = board.getCells()[y--][x++].getPiece();
            if (piece != null) {
                if (piece.colour != king.colour && (piece instanceof Queen || piece instanceof Bishop))
                    return true;
                break;
            }
        }

        x = position.x + 1;
        y = position.y + 1;
        while (x < board.getBoardSize() && y < board.getBoardSize()) {
            Piece piece = board.getCells()[y++][x++].getPiece();
            if (piece != null) {
                if (piece.colour != king.colour && (piece instanceof Queen || piece instanceof Bishop))
                    return true;
                break;
            }
        }

        x = position.x - 1;
        y = position.y;
        while (x >= 0) {
            Piece piece = board.getCells()[y][x--].getPiece();
            if (piece != null) {
                if (piece.colour != king.colour && (piece instanceof Queen || piece instanceof Rook))
                    return true;
                break;
            }
        }

        x = position.x + 1;
        while (x < board.getBoardSize()) {
            Piece piece = board.getCells()[y][x++].getPiece();
            if (piece != null) {
                if (piece.colour != king.colour && (piece instanceof Queen || piece instanceof Rook))
                    return true;
                break;
            }
        }

        x = position.x;
        y = position.y - 1;
        while (y >= 0) {
            Piece piece = board.getCells()[y--][x].getPiece();
            if (piece != null) {
                if (piece.colour != king.colour && (piece instanceof Queen || piece instanceof Rook))
                    return true;
                break;
            }
        }

        x = position.x;
        y = position.y + 1;
        while (y < board.getBoardSize()) {
            Piece piece = board.getCells()[y++][x].getPiece();
            if (piece != null) {
                if (piece.colour != king.colour && (piece instanceof Queen || piece instanceof Rook))
                    return true;
                break;
            }
        }

        y = position.y;
        x = position.x;
        Point[] knightPositions = {new Point(x - 2, y - 1),
                new Point(x - 2, y + 1),
                new Point(x - 1, y - 2),
                new Point(x - 1, y + 2),
                new Point(x + 1, y - 2),
                new Point(x + 1, y + 2),
                new Point(x + 2, y - 1),
                new Point(x + 2, y + 1)};
        for (Point knightPosition : knightPositions) {
            y = knightPosition.y;
            x = knightPosition.x;
            if (y >= 0 && x >= 0 && y < board.getBoardSize() && x < board.getBoardSize()) {
                Piece piece = board.getPiece(x, y);
                if (piece instanceof Knight && piece.colour != king.colour)
                    return true;
            }
        }

        y = position.y;
        x = position.x;
        if (king.colour == Cell.Colour.white && y + 1 < board.getBoardSize()) {
            if (x > 0) {
                Piece piece = board.getCells()[y + 1][x - 1].getPiece();
                if (piece instanceof Pawn && piece.colour == Cell.Colour.black)
                    return true;
            }
            if (x + 1 < board.getBoardSize()) {
                Piece piece = board.getCells()[y + 1][x + 1].getPiece();
                return piece instanceof Pawn && piece.colour == Cell.Colour.black;
            }
        } else if (king.colour == Cell.Colour.black && y > 0) {
            if (x > 0) {
                Piece piece = board.getCells()[y - 1][x - 1].getPiece();
                if (piece instanceof Pawn && piece.colour == Cell.Colour.white)
                    return true;
            }
            if (x + 1 < board.getBoardSize()) {
                Piece piece = board.getCells()[y - 1][x + 1].getPiece();
                return piece instanceof Pawn && piece.colour == Cell.Colour.white;
            }
        }
        return false;
    }
}

package chess.mechanics.pieces;

import chess.mechanics.*;

import java.awt.Point;
import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(Cell.Colour colour) {
        this(colour, false);
    }

    public Pawn(Cell.Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    @Override
    public Piece copy() {
        return new Pawn(colour);
    }

    @Override
    public String getName() {
        return "Pawn";
    }

    @Override
    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = position.y;
        int x = position.x;
        addDoubleStep(moves, x, y);
        addSingleStep(moves, x, y);
        addBeatSteps(moves, x, y);
        addEnPassantSteps(moves, x, y);
        return removeMovesThatLeadToCheck(moves);
    }

    private void addDoubleStep(ArrayList<Move> moves, int x, int y) {
        if (colour == Cell.Colour.black) {
            if (y == getBoardSize() - 2 && findPiece(x, y - 2) == null)
                moves.add(new Move(position, x, y - 2));
        } else {
            if (y == 1 && findPiece(x, y + 2) == null)
                moves.add(new Move(position, x, y + 2));
        }
    }

    private void addSingleStep(ArrayList<Move> moves, int x, int y) {
        if (colour == Cell.Colour.black) {
            if (findPiece(x, y - 1) == null)
                moves.add(new Move(position, x, y - 1));
        } else {
            if (findPiece(x, y + 1) == null)
                moves.add(new Move(position, x, y + 1));
        }
    }

    private void addBeatSteps(ArrayList<Move> moves, int x, int y) {
        if (colour == Cell.Colour.black) {
            addBeatStep(moves, x - 1, y - 1);
            addBeatStep(moves, x + 1, y - 1);
        } else {
            addBeatStep(moves, x - 1, y + 1);
            addBeatStep(moves, x + 1, y + 1);
        }
    }

    private void addBeatStep(ArrayList<Move> moves, int x, int y) {
        if (x >= 0 && x < getBoardSize()) {
            Piece piece = findPiece(x, y);
            if (piece != null && piece.getColour() != colour)
                moves.add(new Move(position, x, y));
        }
    }

    private void addEnPassantSteps(ArrayList<Move> moves, int x, int y) {
        if (colour == Cell.Colour.black) {
            addEnPassantStep(moves, x - 1, y, y - 1);
            addEnPassantStep(moves, x + 1, y, y - 1);
        } else {
            addEnPassantStep(moves, x - 1, y, y + 1);
            addEnPassantStep(moves, x + 1, y, y + 1);
        }
    }

    private void addEnPassantStep(ArrayList<Move> moves, int x, int y, int yMove) {
        Cell.Colour opponentColour = colour == Cell.Colour.black ? Cell.Colour.white : Cell.Colour.black;
        if (x >= 0 && x < getBoardSize()) {
            Piece piece = findPiece(x, y);
            if (piece != null && piece.getColour() != colour && cell.getBoard().getLastDoubleStep(opponentColour) == piece) {
                Move move = new Move(position, x, yMove);
                move.secondMove = new Move(new Point(x, y), null);
                moves.add(move);
            }
        }
    }
}

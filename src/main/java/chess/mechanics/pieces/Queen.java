package chess.mechanics.pieces;

import chess.mechanics.*;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(Cell.Colour colour) {
        this(colour, false);
    }

    public Queen(Cell.Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    @Override
    public Piece copy() {
        return new Queen(colour);
    }

    @Override
    public String getName() {
        return "Queen";
    }

    @Override
    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        moves.addAll(getHorizontalMoves());
        moves.addAll(getVerticalMoves());
        moves.addAll(getDiagonalMoves());
        return removeMovesThatLeadToCheck(moves);
    }
}

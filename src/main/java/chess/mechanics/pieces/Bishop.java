package chess.mechanics.pieces;

import chess.mechanics.*;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Cell.Colour colour) {
        this(colour, false);
    }

    public Bishop(Cell.Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    @Override
    public Piece copy() {
        return new Bishop(colour);
    }

    @Override
    public String getName() {
        return "Bishop";
    }

    @Override
    protected ArrayList<Move> getPossibleMoves() {
        return removeMovesThatLeadToCheck(getDiagonalMoves());
    }
}

package chess.mechanics.pieces;

import chess.mechanics.*;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Cell.Colour colour) {
        super(colour);
    }

    public Bishop(Cell.Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    public Piece copy() {
        return new Bishop(colour);
    }

    @Override
    public String getName() {
        return "Bishop";
    }

    protected ArrayList<Move> getPossibleMoves() {
        return removeMovesThatLeadToCheck(getDiagonalMoves());
    }
}

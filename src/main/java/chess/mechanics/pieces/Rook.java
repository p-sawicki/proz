package chess.mechanics.pieces;

import chess.mechanics.*;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(Cell.Colour colour) {
        this(colour, false);
    }

    public Rook(Cell.Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    @Override
    public Piece copy() {
        return new Rook(colour);
    }

    @Override
    public String getName() {
        return "Rook";
    }

    @Override
    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        moves.addAll(getVerticalMoves());
        moves.addAll(getHorizontalMoves());
        return removeMovesThatLeadToCheck(moves);
    }
}

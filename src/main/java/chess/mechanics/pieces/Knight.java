package chess.mechanics.pieces;

import chess.mechanics.*;

import java.awt.Point;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Cell.Colour colour) {
        this(colour, false);
    }

    public Knight(Cell.Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    @Override
    public Piece copy() {
        return new Knight(colour);
    }

    @Override
    public String getName() {
        return "Knight";
    }

    @Override
    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = position.y;
        int x = position.x;
        moves.add(new Move(position, x - 1, y - 2));
        moves.add(new Move(position, x + 1, y - 2));
        moves.add(new Move(position, x - 2, y - 1));
        moves.add(new Move(position, x + 2, y - 1));
        moves.add(new Move(position, x - 2, y + 1));
        moves.add(new Move(position, x + 2, y + 1));
        moves.add(new Move(position, x - 1, y + 2));
        moves.add(new Move(position, x + 1, y + 2));
        return trimPossibleMoves(moves);
    }
}

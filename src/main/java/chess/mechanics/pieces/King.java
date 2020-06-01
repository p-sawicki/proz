package chess.mechanics.pieces;

import chess.mechanics.*;

import java.util.ArrayList;

public class King extends Piece {
    public King(Cell.Colour colour) {
        this(colour, false);
    }

    public King(Cell.Colour colour, boolean hasMoved) {
        super(colour, hasMoved);
    }

    @Override
    public Piece copy() {
        return new King(colour);
    }

    @Override
    public String getName() {
        return "King";
    }

    @Override
    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();

        for (int x = position.x - 1; x <= position.x + 1; ++x) {
            for (int y = position.y - 1; y <= position.y + 1; ++y) {
                if (x != y)
                    moves.add(new Move(position, x, y));
            }
        }
        addCastling(moves, true);
        addCastling(moves, false);
        return trimPossibleMoves(moves);
    }

    private void addCastling(ArrayList<Move> moves, boolean left) {
        int rookX = left ? 0 : getBoardSize() - 1;
        int x = position.x;
        int y = position.y;
        int neighbor = left ? x - 1 : x + 1;
        int castle = left ? x - 2 : x + 2;

        Piece piece = findPiece(rookX, y);
        if (!hasMoved && piece instanceof Rook && piece.getColour() == colour && !piece.getHasMoved()
                && findPiece(neighbor, position.y) == null
                && !CheckDetector.doesMoveLeadToCheck(cell.getBoard(), new Move(position, position))
                && !CheckDetector.doesMoveLeadToCheck(cell.getBoard(), new Move(position, neighbor, y))) {
            Move move = new Move(position, castle, y);
            move.secondMove = new Move(piece.getPosition(), neighbor, y);
            moves.add(move);
        }
    }
}

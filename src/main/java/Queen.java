import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(Cell.Colour colour) {
        super(colour);
    }

    public Piece copy() {
        return new Queen(colour);
    }

    @Override
    public String getName() {
        return "Queen";
    }

    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        moves.addAll(getHorizontalMoves());
        moves.addAll(getVerticalMoves());
        moves.addAll(getDiagonalMoves());
        return moves;
    }
}

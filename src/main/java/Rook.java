import java.util.ArrayList;

public class Rook extends Piece{
    public Rook(Cell.Colour colour) {
        super(colour);
    }

    public Piece copy(){
        return new Rook(colour);
    }

    @Override
    public String getName() {
        return "Rook";
    }

    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        moves.addAll(getVerticalMoves());
        moves.addAll(getHorizontalMoves());
        return moves;
    }
}

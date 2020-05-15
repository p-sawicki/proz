import java.awt.Point;
import java.util.ArrayList;

public class King extends Piece {
    public King(Cell.Colour colour) {
        super(colour);
    }

    public Piece copy(){
        return new King(colour);
    }

    @Override
    public String getName() {
        return "King";
    }

    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = cell.getPosition().y;
        int x = cell.getPosition().x;
        Point start = cell.getPosition();
        moves.add(new Move(start, new Point(x - 1, y - 1)));
        moves.add(new Move(start, new Point(x, y - 1)));
        moves.add(new Move(start, new Point(x + 1, y - 1)));
        moves.add(new Move(start, new Point(x - 1, y)));
        moves.add(new Move(start, new Point(x + 1, y)));
        moves.add(new Move(start, new Point(x - 1, y + 1)));
        moves.add(new Move(start, new Point(x, y + 1)));
        moves.add(new Move(start, new Point(x + 1, y + 1)));
        return trimPossibleMoves(moves);
    }
}

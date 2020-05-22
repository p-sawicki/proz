import java.awt.Point;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Cell.Colour colour) {
        super(colour);
    }

    public Piece copy() {
        return new Knight(colour);
    }

    @Override
    public String getName() {
        return "Knight";
    }

    protected ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int y = cell.getPosition().y;
        int x = cell.getPosition().x;
        Point start = cell.getPosition();
        moves.add(new Move(start, new Point(x - 1, y - 2)));
        moves.add(new Move(start, new Point(x + 1, y - 2)));
        moves.add(new Move(start, new Point(x - 2, y - 1)));
        moves.add(new Move(start, new Point(x + 2, y - 1)));
        moves.add(new Move(start, new Point(x - 2, y + 1)));
        moves.add(new Move(start, new Point(x + 2, y + 1)));
        moves.add(new Move(start, new Point(x - 1, y + 2)));
        moves.add(new Move(start, new Point(x + 1, y + 2)));
        return trimPossibleMoves(moves);
    }
}

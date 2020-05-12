import java.util.HashMap;

public class Move {
    public Point before;
    public Point after;
    private static final HashMap<Point, HashMap<Point, Move>> moves = new HashMap<>();

    private Move(Point before, Point after){
        this.before = before;
        this.after = after;
    }

    public static Move get(Point before, Point after){
        if(!moves.containsKey(before))
            moves.put(before, new HashMap<>());
        if(!moves.get(before).containsKey(after))
            moves.get(before).put(after, new Move(before, after));
        return moves.get(before).get(after);
    }
}

import java.awt.*;
import java.util.HashMap;

public class Move {
    public Point before;
    public Point after;
    private static final HashMap<Point, HashMap<Point, Move>> moves = new HashMap<>();

    public Move(Point before, Point after){
        this.before = before;
        this.after = after;
    }
}

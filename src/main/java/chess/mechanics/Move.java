package chess.mechanics;

import java.awt.*;
import java.io.Serializable;

public class Move implements Serializable {
    public enum Promotion {queen, knight, bishop, rook}

    public Point before;
    public Point after;
    public Move secondMove;
    public Promotion promotion;

    public Move(Point before, Point after) {
        this.before = before;
        this.after = after;
        secondMove = null;
        promotion = null;
    }

    public Move(Point before, int x, int y) {
        this(before, new Point(x, y));
    }

    public Move(int x1, int y1, int x2, int y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }
}

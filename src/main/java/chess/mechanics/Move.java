package chess.mechanics;

import java.awt.*;
import java.io.Serializable;

public class Move implements Serializable {
    public enum Promotion {Queen, Knight, Bishop, Rook};
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
}

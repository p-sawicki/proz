package chess.mechanics;

import java.awt.*;
import java.io.Serializable;

public class Move implements Serializable {
    public Point before;
    public Point after;

    public Move(Point before, Point after) {
        this.before = before;
        this.after = after;
    }
}

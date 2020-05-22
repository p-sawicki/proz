import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

public class Move implements Serializable {
    public Point before;
    public Point after;

    public Move(Point before, Point after) {
        this.before = before;
        this.after = after;
    }
}

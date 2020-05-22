import java.io.Serializable;

public class Message implements Serializable {
    public Move move;
    public CheckDetector.State state;

    Message(Move move, CheckDetector.State state) {
        this.move = move;
        this.state = state;
    }
}

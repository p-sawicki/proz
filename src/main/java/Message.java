public class Message {
    public Move move;
    public CheckDetector.State state;

    Message(Move move, CheckDetector.State state){
        this.move = move;
        this.state = state;
    }
}

package chess.network;

import chess.mechanics.*;

import java.io.Serializable;

public class Message implements Serializable {
    public String messageType;
    public Move move;
    public CheckDetector.State state;

    public Message(Move move, CheckDetector.State state, String messageType) {
        this.messageType = messageType;
        this.move = move;
        this.state = state;
    }

    public Message(String messageType) {
        this.messageType = messageType;
        this.move = null;
        this.state = null;
    }
}

package chess.network;

import chess.mechanics.*;

import java.io.Serializable;

public class Message implements Serializable {
    public enum MessageType {quit, move}

    public MessageType messageType;
    public Move move;
    public CheckDetector.State state;

    public Message(Move move, CheckDetector.State state, MessageType messageType) {
        this.messageType = messageType;
        this.move = move;
        this.state = state;
    }

    public Message(MessageType messageType) {
        this(null, null, messageType);
    }
}

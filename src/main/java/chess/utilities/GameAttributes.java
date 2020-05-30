package chess.utilities;

import chess.mechanics.*;
import chess.network.ConnectionHandler;

public class GameAttributes {
    private Board board;
    private boolean singlePlayer;
    private String playerName;
    private Cell.Colour playerColour;
    private String opponentName;
    private String opponentIP;
    private ConnectionHandler connectionHandler;

    // getters
    public Board getBoard() {
        return board;
    }

    public boolean isSinglePlayer() {
        return singlePlayer;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Cell.Colour getPlayerColour() {
        return this.playerColour;
    }

    public String getOpponentName() {
        return this.opponentName;
    }

    public String getOpponentIP() {
        return this.opponentIP;
    }

    public ConnectionHandler getConnectionHandler() {
        return this.connectionHandler;
    }

    // setters
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setSinglePlayer(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void setPlayerColour(Cell.Colour colour) {
        this.playerColour = colour;
    }

    public void setOpponentName(String name) {
        this.opponentName = name;
    }

    public void setOpponentIP(String ip) {
        this.opponentIP = ip;
    }

    public void setConnectionHandler(ConnectionHandler connHandler) {
        this.connectionHandler = connHandler;
    }
}

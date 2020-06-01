package chess.network;

import chess.gui.*;
import chess.mechanics.Board;
import chess.utilities.Utility;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ConnectionHandler extends ConnectionHandlerBase {
    private final String peerName;
    private Board board;
    private JFrame gameWindow;

    public ConnectionHandler(String peerName, Board board) {
        this.board = board;
        this.peerName = peerName;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void send(Message message) {
        try {
            Socket outgoingConnection = new Socket(peerName, peerPort);
            ObjectOutputStream outputStream = new ObjectOutputStream(outgoingConnection.getOutputStream());
            outputStream.writeObject(message);
            outgoingConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket incomingConnection = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(incomingConnection.getInputStream());
                Message message = (Message) objectInputStream.readObject();
                incomingConnection.close();
                switch (message.messageType) {
                    case move:
                        board.onMessageReceived(message);
                        break;
                    case quit:
                        if (Utility.ignoredWarning("Your opponent has quit the game! Do you want to end the game?")) {
                            SwingUtilities.invokeLater(new Menu());
                            gameWindow.dispose();
                        }
                        break;
                }
            } catch (SocketTimeoutException ignored) {
            } catch (Exception e) {
                break;
            }
        }
    }

    public void setGameWindow(JFrame gameWindow) {
        this.gameWindow = gameWindow;
    }
}

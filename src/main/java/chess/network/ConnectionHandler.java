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

public class ConnectionHandler extends Thread {
    private final String peerName;
    private final int peerPort = 6970;
    private Board board;
    private boolean running;
    private ServerSocket serverSocket;
    private JFrame gameWindow;

    public ConnectionHandler(String peerName, Board board) {
        this.peerName = peerName;
        this.board = board;
        running = false;
        try {
            serverSocket = new ServerSocket(peerPort);
            serverSocket.setSoTimeout(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    case "M":
                        board.onMessageReceived(message);
                        break;
                    case "Q":
                        if (Utility.ignoredWarning("Your opponent quit game! Do you want to end game?")) {
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

    public void stopReceiving() {
        running = false;
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeReceiving() {
        running = true;
        try {
            serverSocket = new ServerSocket(peerPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGameWindow(JFrame gameWindow) {
        this.gameWindow = gameWindow;
    }
}

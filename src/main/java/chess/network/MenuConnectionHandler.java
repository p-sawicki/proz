package chess.network;

import chess.gui.Menu;
import chess.mechanics.Cell;
import chess.utilities.GameAttributes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MenuConnectionHandler extends ConnectionHandlerBase {
    private final String challenge = "C";
    private final String accepted = "A";
    private final String declined = "D";
    private final String black = "B";
    private final Menu menu;
    private Multiplayer multiplayer;

    public MenuConnectionHandler(Menu menu) {
        this.menu = menu;
        this.multiplayer = new Multiplayer(menu);
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket incomingConnection = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(incomingConnection.getInputStream());
                String[] message = dataInputStream.readUTF().split(" ");
                String opponentAddress = incomingConnection.getInetAddress().getHostAddress();
                incomingConnection.close();
                switch (message[0]) {
                    case challenge:
                        multiplayer.onOpponentChallenge(message[1], opponentAddress);
                        break;
                    case accepted:
                        multiplayer.onChallengeAccepted(message[1].equals(black) ? Cell.Colour.black : Cell.Colour.white, opponentAddress, message[2]);
                        break;
                    case declined:
                        multiplayer.onChallengeDeclined();
                        break;
                }
            } catch (SocketTimeoutException ignored) {
            } catch (Exception e) {
                break;
            }
        }
    }

    private void sendMessage(String opponentAddress, String message) {
        try {
            Socket socket = new Socket(opponentAddress, peerPort);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void challenge(String myName, String opponentAddress, GameAttributes gameAttributes) {
        sendMessage(opponentAddress, challenge + " " + myName);
        this.multiplayer = new Multiplayer(this.menu, gameAttributes);
    }

    public void accept(String opponentAddress, Cell.Colour colour, String myName) {
        final String white = "W";
        sendMessage(opponentAddress, accepted + " " + (colour == Cell.Colour.black ? black : white) + " " + myName);
    }

    public void decline(String opponentAddress) {
        sendMessage(opponentAddress, declined);
    }
}

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MenuConnectionHandler extends Thread {
    private final int port = 6969;
    private final Menu menu;
    private boolean running;
    private ServerSocket serverSocket;

    MenuConnectionHandler(Menu menu) {
        this.menu = menu;
        running = false;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket incomingConnection = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(incomingConnection.getInputStream());
                String[] message = dataInputStream.readUTF().split(" ");
                String address = incomingConnection.getInetAddress().getHostAddress();
                incomingConnection.close();
                switch (message[0]) {
                    case "C":
                        menu.onOpponentChallenge(message[1], address);
                        break;
                    case "A":
                        menu.onChallengeAccepted(message[1].equals("B") ? Cell.Colour.black : Cell.Colour.white, address, message[2]);
                        break;
                    case "D":
                        menu.onChallengeDeclined();
                        break;
                }
            } catch (SocketTimeoutException ignored) {
            } catch (Exception e) {
                break;
            }
        }
    }

    private void sendMessage(String address, String message) {
        try {
            Socket socket = new Socket(address, port);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void challenge(String name, String address) {
        sendMessage(address, "C " + name);
    }

    public void accept(String address, Cell.Colour colour, String myName) {
        sendMessage(address, "A " + (colour == Cell.Colour.black ? "B" : "W") + " " + myName);
    }

    public void decline(String address) {
        sendMessage(address, "D");
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
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

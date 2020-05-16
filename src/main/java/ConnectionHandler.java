import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private final String peerName;
    private final int peerPort;

    public ConnectionHandler(String peerName, int peerPort){
        this.peerName = peerName;
        this.peerPort = peerPort;
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

    public Message receive() {
        try {
            ServerSocket serverSocket = new ServerSocket(peerPort);
            Socket incomingConnection = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(incomingConnection.getInputStream());
            Message message = (Message) objectInputStream.readObject();
            incomingConnection.close();
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

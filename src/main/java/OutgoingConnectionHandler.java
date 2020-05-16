import java.io.*;
import java.net.Socket;

public class OutgoingConnectionHandler extends Thread {
    private final String peerName;
    private final int peerPort;
    private final String message;

    OutgoingConnectionHandler(String peerName, int peerPort, String message) {
        this.peerName = peerName;
        this.peerPort = peerPort;
        this.message = message;
    }

    public void run() {
        try {
            System.out.println("Connecting to " + peerName + " on port " + peerPort);
            Socket outgoingConnection = new Socket(peerName, peerPort);

            System.out.println("Connected to " + outgoingConnection.getRemoteSocketAddress());
            OutputStream outputStream = outgoingConnection.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);

            out.writeUTF(message);
            InputStream inputStream = outgoingConnection.getInputStream();
            DataInputStream in = new DataInputStream(inputStream);

            System.out.println("Received: " + in.readUTF());
            outgoingConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

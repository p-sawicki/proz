import java.io.IOException;

public class ConnectionHandler extends Thread{
    private String peerName;
    private int peerPort;

    public ConnectionHandler(String peerName, int peerPort) throws IOException {
        this.peerName = peerName;
        this.peerPort = peerPort;
        Thread incoming = new IncomingConnectionHandler(peerPort);
        incoming.start();
    }

    public void send(String message){
        Thread outgoing = new OutgoingConnectionHandler(peerName, peerPort, message);
    }
}

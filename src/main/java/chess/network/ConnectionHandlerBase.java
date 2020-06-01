package chess.network;

import java.net.ServerSocket;

abstract public class ConnectionHandlerBase extends Thread{
    protected final int peerPort = 11111;
    protected boolean running;
    protected ServerSocket serverSocket;

    protected ConnectionHandlerBase() {
        running = false;
        try {
            serverSocket = new ServerSocket(peerPort);
            serverSocket.setSoTimeout(1000);
        } catch (Exception e) {
            e.printStackTrace();
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
}

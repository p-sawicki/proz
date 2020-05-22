import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
                incomingConnection.close();
                if (message[0].equals("C"))
                    menu.onOpponentChallenge(message[1], incomingConnection.getInetAddress().getHostAddress());
            } catch (SocketTimeoutException ignored) {
            } catch (Exception e) {
                break;
            }
        }
    }

    public void challenge(String name, String address) {
        try {
            Socket socket = new Socket(address, port);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("C " + name);
            socket.close();
        } catch (IOException e) {
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
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

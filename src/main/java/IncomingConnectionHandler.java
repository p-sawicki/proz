import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class IncomingConnectionHandler extends Thread{
    private final ServerSocket serverSocket;

    public IncomingConnectionHandler(int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }

    public void run(){
        while(true){
            try{
                System.out.println("Waiting for client...");
                Socket incomingConnection = serverSocket.accept();

                System.out.println("Connected to " + incomingConnection.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(incomingConnection.getInputStream());

                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(incomingConnection.getOutputStream());
                out.writeUTF("RETURN");
                incomingConnection.close();
            } catch (SocketTimeoutException s){
                System.out.println("Timed out");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

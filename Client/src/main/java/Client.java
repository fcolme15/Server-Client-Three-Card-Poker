import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
    private static Client clientThread;
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    int port;
    String ip;
    private Consumer<Serializable> callback;

    PokerInfo gameData;

    //Client constructor
    private Client(Consumer<Serializable> call, int portNum, String clientIP)
    {
        callback = call;
        port = portNum;
        ip = clientIP;
        gameData = PokerInfo.getInstance();
    }

    public static Client getInstance(Consumer<Serializable> call, int portNum, String clientIP)
    {
        if(clientThread == null)
        {
            clientThread = new Client(call, portNum, clientIP);
            return clientThread;
        }
        return clientThread;
    }

    public void run()
    {
        try
        {
            clientSocket = new Socket(ip, port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            clientSocket.setTcpNoDelay(true);

            System.out.println("connected to server?");

            while(true)
            {
                // Serializable data = (Serializable)in.readObject();
                // callback.accept(data);

                //whenever server sends us some gameData back, determine what next action is
                // PokerInfo receivedData = (PokerInfo)in.readObject();
                // gameData = receivedData;
                // System.out.println("IN CLIENT'S RUN()");
            }
        }
        catch(Exception e) { System.out.println(e.toString()); }
    }

    public void send(PokerInfo info) 
    {
        try { 
            System.out.println("SENDING INFFOOOO");
            out.reset();
            out.writeObject(info); 
            out.flush();
        }
        catch(Exception e) { e.printStackTrace(); }
    }
}

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class Server {
    private static Server serverObject;
    //initialize Server object
    private int count = 0;
    private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    private TheServer server;
    public ListView<String> clientList = new ListView<>();
    static public ObservableList<String> realClientList = FXCollections.observableArrayList();
    private int port;
    private PokerInfo gamedata;



    private Server(Consumer<Serializable> call, int portNum) 
    {
        port = portNum;
        server = new TheServer();
        server.start();
    }

    public static Server getInstance(Consumer<Serializable> call, int portNum)
    {
        if(serverObject == null)
        {
            serverObject = new Server(call, portNum);
            return serverObject;
        }
        return serverObject;
    }

    //Server thread
    public class TheServer extends Thread {
        public void run() 
        {
            try(ServerSocket mysocket = new ServerSocket(port))
            {
                Platform.runLater(() -> { realClientList.addAll("Server is waiting for a client!\n"); });
                System.out.println("CONNECTED TO SERVER");
                //event loop
                while(true)
                {
                    //Client has been found
                    ClientThread c = new ClientThread(mysocket.accept(), count);

                    //Blocking call waiting for client to connect to server and then add client
                    Platform.runLater(() -> {realClientList.addAll("A client has connected to server: client #" + count + "\n");});
                    synchronized(clients) { clients.add(c); }
                    c.start();

                    System.out.println("client has connected!");
                    
                    //start poker info
                    // gamedata = new PokerInfo();

                    count++;
                }
            }
            catch(Exception e)
            {
                // callback.accept("Server did not launch");
                System.out.println("SERVER CRASH WOMPWOMP");
            }
        }
    }

    class ClientThread extends Thread {
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;
    
        ClientThread(Socket s, int count) throws IOException
        {
            this.connection = s;
            this.count = count;
            in = new ObjectInputStream(connection.getInputStream());
            out = new ObjectOutputStream(connection.getOutputStream());
            System.out.println("yay okay client thread created");
        }

        public void run() 
        {
            System.out.println("in runnnnn in client thread");
            try { connection.setTcpNoDelay(true); }
            catch(Exception e) { System.out.println("Streams are not open"); }
        
            //event loop; game logic goes in here
            while(true)
            {   
                try 
                {
                    PokerInfo data = (PokerInfo)in.readObject();
                    Player clientPlayer = data.getPlayerOne();
                    Dealer clientDealer = data.getDealer();
                    String anteBetString = Integer.toString(clientPlayer.getAnteBet());

                    System.out.println("TRYING TO READ IN INFOOOO");

                    switch(data.getGameState())
                    {
                        //    11 -> Server deals player's hand
                        case 11: 
                        {
                            System.out.println("IN CASE 11");
                            clientPlayer.setHand(clientDealer.dealHand());
                            data.setGameState(12);

                            out.writeObject(data);
                            out.flush();

                            realClientList.addAll("A new game is being played");
                            clientList.setItems(realClientList);

                            break;
                        }
                        //    13 -> Server deals Dealer's hand
                        case 13:
                        {
                            System.out.println("IN CASE 13");
                            clientDealer.setDealersHand(clientDealer.dealHand());
                            data.setGameState(14);

                            out.writeObject(data);
                            out.flush();
                            break;
                        }
                        //    15 -> Server evaluates hands
                        case 15:
                        {
                            System.out.println("IN CASE 15");
                            ThreeCardLogic.settlePlayerWinnings(data.getPlayedHand(), data.getPlayerOne(),
                                                        "Player", data.getDealer(), data.getChat());
                            data.setGameState(16);

                            out.writeObject(data);
                            out.flush();
                            break;
                        }
                        //    17 -> Server nulls dealer's hand
                        case 17:
                        {
                            System.out.println("IN CASE 17");
                            clientDealer.setDealersHand(null);
                            data.setGameState(18);

                            out.writeObject(data);
                            out.flush();
                            break;
                        }
                        default:
                            System.out.println("OKAY James BROKE IT");
                    }

                    /*GAME LOGIC*/
                    Platform.runLater(() -> { realClientList.addAll(
                        "Player " + count + "'s ante bet: " + anteBetString); });
                }
                catch(Exception e)
                {
                    Platform.runLater(() -> { realClientList.addAll("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!"); });
                    break;
                }
    
            }
        }
    }
}

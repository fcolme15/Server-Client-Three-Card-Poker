import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.beans.property.SimpleIntegerProperty;

public class Server {
    private static Server serverObject;
    //initialize Server object
    private int count = 0;
    private IntegerProperty clientConnectedCount = new SimpleIntegerProperty(0); //use for real-time client count
    private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    private TheServer server;
    public ListView<String> clientList = new ListView<>();
    public ListView<String> statList = new ListView<>();
    static public ObservableList<String> realClientList = FXCollections.observableArrayList();
    static public ObservableList<String> realStatsList = FXCollections.observableArrayList();

    private int port;

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
                Platform.runLater(() -> { realClientList.addAll("Waiting for a client!\n"); });
                System.out.println("CONNECTED TO SERVER");
                //event loop
                while(true)
                {
                    try
                    {
                        //Client has been found
                        ClientThread c = new ClientThread(mysocket.accept(), count);
                        //Blocking call waiting for client to connect to server and then add client
                        Platform.runLater(() -> {realClientList.addAll("Client #" + count + " has connected to the server");});
                        c.start();
                        synchronized(clients) { clients.add(c); setClientCount(getClientCount() + 1); }
                        System.out.println("client has connected!");
                        count++;
                    }
                    catch(Exception ex) { ex.toString(); }
                }
            }
            catch(Exception e)
            {
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
                    // String anteBetString = Integer.toString(clientPlayer.getAnteBet());

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

                            Platform.runLater(() -> {realStatsList.addAll("Client " + count + " is playing another hand");});

                            break;
                        }
                        //    13 -> Server deals Dealer's hand
                        case 13:
                        {
                            System.out.println("IN CASE 13");
                            clientDealer.setDealersHand(clientDealer.dealHand());
                            data.setGameState(14);
                            Player player = data.getPlayerOne();
                            Platform.runLater(() -> {realStatsList.addAll("Client " + count + " bet $" + player.getAnteBet() + " as ante bet");});
                            Platform.runLater(() -> {realStatsList.addAll("Client " + count + " bet $" + player.getPairPlusBet() + " as PairPlus bet");});

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

                            Player player = data.getPlayerOne();
                            int winningsBefore = player.getLastestHandWinnings();
                            int whoWon = player.getWonLastHand();
                            int winnings;
                            if (data.getPlayedHand()){
                                winnings = 2 * winningsBefore;
                            }
                            else{
                                winnings = winningsBefore;
                            }

                            if (whoWon == 0){
                                Platform.runLater(() -> {realStatsList.addAll("Dealer didn't have Queen or higher so client " + count + " pushed: $" + winnings);});
                            }
                            else if (whoWon == 1){
                                Platform.runLater(() -> {realStatsList.addAll("Client " + count + " lost: $" + winnings + " from ante wager");});
                            }
                            else if (whoWon == 2){
                                Platform.runLater(() -> {realStatsList.addAll("Client " + count + " won: $" + winnings + " from ante wager");});
                            }
                            else if (whoWon == 3){
                                Platform.runLater(() -> {realStatsList.addAll("Client " + count + " folded and lost: $" + winnings + " from ante wager");});
                            }
                            else{
                                System.out.println("Error, Bad");
                                System.exit(0);
                            }

                            int ppBet = player.getPairPlusBet();
                            if (!(ppBet == 0)){
                                int ppWinnings = player.getPairPlusWon();
                                if (ppWinnings > 0){
                                    Platform.runLater(() -> {realStatsList.addAll("Client " + count + " won: $" + ppWinnings + " from pair plus wager");});
                                }
                                else{
                                    Platform.runLater(() -> {realStatsList.addAll("Client " + count + " lost: $" + ppBet + " from pair plus wager");});
                                }
                            }

                            Platform.runLater(() -> {realStatsList.addAll("Client " + count + " currently has $" + player.getTotalWinnings() + " earnings");});

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
                }
                catch(Exception e)
                {
                    Platform.runLater(() -> { realClientList.addAll("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!"); });
                    synchronized(clients) { clients.remove(this); }
                    setClientCount(getClientCount() - 1);
                    break;
                }
    
            }
        }
    }

    public IntegerProperty connectedCountProperty()
    {
        return clientConnectedCount;
    }

    public int getClientCount() 
    {
        return clientConnectedCount.get();
    }

    public void setClientCount(int c)
    {
        clientConnectedCount.set(c);
    }
}

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import java.util.Queue;
import javafx.application.Platform;

//Start Images includes
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//End of Images includes

public class PlayScreen1 implements Initializable{
    //at this point client should already have connection to server so these can just be null and we'll get the right connection
    Client clientConnection = Client.getInstance(null, 0, null); 

    //BLOCKING OBJECT BEFORE TRANSITIONING TO NEW SCREEN IN lockBet()
    private static final Object lock = new Object();

    PokerInfo gameData = PokerInfo.getInstance();
    Player playerOne = gameData.getPlayerOne();
    Dealer theDealer = gameData.getDealer();
    Queue<String> chat = gameData.getChat();
    ImageView c1Img;

    @FXML
    private BorderPane ps1Root;

    @FXML
    private Button deckCard;

    int ante1;
    @FXML
    private Slider player1Ante;
    @FXML
    private Label displayAnte1;
    int pp1;
    @FXML
    private Slider player1PP;
    @FXML
    private Label displayPP1;
    @FXML
    private Button noPP1;
    @FXML
    private Button player1Lock;
    @FXML
    private Label player1TW;

    @FXML
    private TextArea chatLog;

    @Override
    //public void initialize() {
    public void initialize(URL location, ResourceBundle resources) {
        gameData.setGameState(0);

        anteSliders(player1Ante, displayAnte1, playerOne);
        ppSliders(player1PP, displayPP1, playerOne);

        updateUI();
    }

    public void anteSliders(Slider s, Label l, Player p) {
        s.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                p.setAnteBet((int)s.getValue());
                l.setText("$"+Integer.toString(p.getAnteBet()));
            }
        });
        s.setMin(5);
        s.setMax(25);
        s.setValue(5);
        s.setBlockIncrement(1);
        s.setMajorTickUnit(1);
        s.setSnapToTicks(true);
    }

    public void ppSliders(Slider s, Label l, Player p) {
        s.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                p.setPairPlusBet((int)s.getValue());
                l.setText("$"+Integer.toString(p.getPairPlusBet()));
            }
        });
        s.setMin(5);
        s.setMax(25);
        s.setValue(5);
        s.setBlockIncrement(1);
        s.setMajorTickUnit(1);
        s.setSnapToTicks(true);
    }

    public void ppAction1(ActionEvent e) {
        if(!player1PP.isDisable()) 
        { 
            playerOne.setPairPlusBet(0); 
            displayPP1.setText("$"+Integer.toString(playerOne.getPairPlusBet()));
            player1PP.setDisable(true); 
        }
        else 
        { 
            player1PP.setDisable(false); 
            ppSliders(player1PP, displayPP1, playerOne);
            playerOne.setPairPlusBet(5);
            displayPP1.setText("$"+Integer.toString(playerOne.getPairPlusBet()));
        }
    }

    public void lockBet1(ActionEvent e) throws IOException {
        //lock bets and disable button
        player1Ante.setDisable(true);
        player1PP.setDisable(true);
        noPP1.setDisable(true);
        player1Lock.setVisible(false);
        
        //Let server know we need to get dealt hand
        gameData.setGameState(11);

        //send over gameData to Server to deal hands
        clientConnection.send(gameData);
        
        //wait for response from the server
        PokerInfo receivedInfo = PokerInfo.getInstance();
        while(receivedInfo.getGameState() != 12)
        {
            System.out.println("IN GS 12 LOOP");
            try
            {
                receivedInfo = (PokerInfo)clientConnection.in.readObject();
                Deck receivedDeck = receivedInfo.getDealer().getTheDeck();
                gameData.getDealer().setTheDeck(receivedDeck);
                playerOne = receivedInfo.getPlayerOne();
            } 
            catch(Exception ex) 
            { 
                /*server crashed here*/ 
                ex.toString();
                Platform.runLater(() -> {
                    chat.add("Server Crashed!!! Ending exiting game...");
                    updateUI();
                });

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(p -> {
                    System.exit(0);
                });
                
                pause.play();
                break;
            }
        }
        System.out.println("AFTER GS 12 LOOP");
        gameData.getPlayerOne().setHand(playerOne.getHand());

        // playerOne.setHand(theDealer.dealHand()); // move to server instead
        //only switch screens if other player has also locked bet

        loadPS2();
    }

    public void loadPS2() throws IOException {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PlayScreen2.fxml"));
        Parent ps2Root = loader.load(); //load view into parent

        pause.setOnFinished(e-> {
                ps2Root.getStylesheets().add(gameData.getStyle(1));
                ps1Root.getScene().setRoot(ps2Root);//update scene graph
            }
        );

        pause.play();
    }

    /******************************************************/
    /*                   Options Methods                  */
    /******************************************************/
    @FXML
    public void freshStart() throws IOException {
        gameData.resetGame();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PlayScreen1.fxml"));
        Parent resetRoot = loader.load(); //load view into parent
        ps1Root.getScene().setRoot(resetRoot);//update scene graph
    }

    @FXML
    public void newLook() throws IOException {
        ps1Root.getStylesheets().clear();
        ps1Root.getStylesheets().add(gameData.swapStyle(1));
    }

    @FXML
    public void exitGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ExitScreen.fxml"));
        Parent exitRoot = loader.load(); //load view into parent

		exitRoot.getStylesheets().add(gameData.getStyle(0));
        ps1Root.getScene().setRoot(exitRoot);//update scene graph
    }

    /******************************************************/
    /*               Returning to Game State              */
    /******************************************************/
    public void updateUI() {
        //update css
        ps1Root.getStylesheets().add(gameData.getStyle(1));

        String cardS1;
        if(gameData.getStyle(1).equals("/styles/PlayScreen1.css")){
            cardS1 = "bc.png";
        }
        else {
            cardS1 = "rc.png";
        }

        Image pic1 = new Image(cardS1);
        c1Img = new ImageView(pic1);
        c1Img.setFitHeight(150);
        c1Img.setFitWidth(150);
        c1Img.setPreserveRatio(true);
        deckCard.setGraphic(c1Img);
        
        //update total winnings display
        System.out.println(playerOne.getTotalWinnings());
        if(playerOne.getTotalWinnings() >= 0) player1TW.setText("Total Winnings: $" + Integer.toString(playerOne.getTotalWinnings()));
        else player1TW.setText("Total Winnings: -$" + Integer.toString(playerOne.getTotalWinnings()*-1));

        //update chat
        chatLog.clear();
        int i = 0;
        for(String text : chat) 
        {
            if(i == 0) chatLog.appendText(text); //to get rid of trailing whitespace
            else chatLog.appendText("\n" + text);
            i++;
        }
    }
}
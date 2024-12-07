import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.TextArea;
import java.util.Queue;

//Start Images includes
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//End of Images includes

public class PlayScreen2 implements Initializable{
    //at this point client should already have connection to server so these can just be null and we'll get the right connection
    Client clientConnection = Client.getInstance(null, 0, null); 
    PokerInfo gameData = PokerInfo.getInstance();
    Player playerOne = gameData.getPlayerOne();
    Dealer theDealer = gameData.getDealer();
    Queue<String> chat = gameData.getChat();

    @FXML
    private BorderPane ps2Root;

    @FXML
    private Label dealerLabel;
    @FXML
    private Button card7;
    @FXML
    private Button card8;
    @FXML
    private Button card9;

	@FXML
    private Label player1TW;
    @FXML
    private Button playHand1;
    @FXML
    private Button foldHand1;
    @FXML
    private Button card1;
    @FXML
    private Button card2;
    @FXML
    private Button card3;

    @FXML
    private TextArea chatLog;

    @FXML
    private MenuItem exitOption;
    @FXML
    private MenuItem restartOption;

    @FXML
    private Button playAgainButton;
    @FXML
    private Button exitButton;

	@Override
    public void initialize(URL location, ResourceBundle resources) {
        gameData.setGameState(1);

		gameData = PokerInfo.getInstance();
		playerOne = gameData.getPlayerOne();
		theDealer = gameData.getDealer();

        // System.out.print(playerOne.getHand().get(0).getSuit());
        // System.out.println(playerOne.getHand().get(0).getValue());

        System.out.println("PLAYER 1 ANTE" + playerOne.getAnteBet());
        System.out.println("PLAYER 1 PP" + playerOne.getPairPlusBet());

        //always update UI in case we return from game from options
        playAgainButton.setVisible(false);
        exitButton.setVisible(false);
        updateUI();
    }
    
    @FXML
    public void playAction1(ActionEvent e) {
        //discard hand and lose money
        playHand1.setVisible(false);
        foldHand1.setVisible(false);
        // playedHand1 = true;
        gameData.setPlayedHand(true);
        evalHands();
    }

    @FXML
    public void foldAction1(ActionEvent e) {
        //discard hand and lose money
        playHand1.setVisible(false);
        foldHand1.setVisible(false);
        // playedHand1 = false;
        gameData.setPlayedHand(false);
        //Player player = gameData.getPlayerOne();
        //player.setWonLastHand(3);
        //player.setTotalWinnings(player.getTotalWinnings() - (player.getPushedAnte() + player.getAnteBet()));
        evalHands();
    }

    public void evalHands() {
        //only evaluate hands if both players have decided to fold or play (whether their buttons are visible or not)
        if(playHand1.isVisible()) return;
        
        //pause durations of dealer "flipping" cards and evaluating hands in case we want animations
        PauseTransition flipDealerPause = new PauseTransition(Duration.seconds(2));
        PauseTransition evalHandsPause = new PauseTransition(Duration.seconds(3));
        PauseTransition transitionScene = new PauseTransition(Duration.seconds(7)); //NEEDED BC PLAYERS GOTTA PROCESS WHATS GOING ON

        exitOption.setDisable(true); //DO NOT ALLOW PLAYERS TO EXIT GAME AND RETURN TO HAND WHILE EVALUATING CARDS
        restartOption.setDisable(true); //HAHA THEY CANT RESET IF THEY LOSE THEY HAVE TO WATCH

        //deal dealer's hand and pause on displaying it bc it's cool (in case we want to set up animations)
        gameData.setGameState(13);
        clientConnection.send(gameData);
        PokerInfo receivedInfo = PokerInfo.getInstance();
        while(receivedInfo.getGameState() != 14)
        {
            System.out.println("IN GS 14 LOOP");
            try
            {
                receivedInfo = (PokerInfo)clientConnection.in.readObject();
                Deck receivedDeck = receivedInfo.getDealer().getTheDeck();
                gameData.getDealer().setTheDeck(receivedDeck);
                theDealer = receivedInfo.getDealer();
            } 
            catch(Exception ex) { ex.printStackTrace(); System.exit(0); /*server crashed here*/  }
        }
        System.out.println("AFTER GS 14 LOOP");
        gameData.getDealer().setDealersHand(theDealer.getDealersHand());

        //deal cards to dealer and evaluate hands
        flipDealerPause.setOnFinished( e -> {
            updateCardDisplay(card7, card8, card9, theDealer.getDealersHand());
        });

        //wait for dealer's cards to be flipped before finally evaluating hands and distribute winnings/losses
        evalHandsPause.setOnFinished( e -> {
            //settle winnings, update UI/chat, and go back to PlayScreen1
            chat.add("Turn " + gameData.addTurn() + ":");
            gameData.setGameState(15);
            clientConnection.send(gameData);
            PokerInfo receivedInfo2 = PokerInfo.getInstance();
            while(receivedInfo2.getGameState() != 16)
            {
                System.out.println("IN GS 16 LOOP");
                try
                {
                    receivedInfo2 = (PokerInfo)clientConnection.in.readObject();

                    Player receivedPlayer = receivedInfo2.getPlayerOne();
                    gameData.getPlayerOne().setHand(receivedPlayer.getHand());

                    gameData.getPlayerOne().setAnteBet(receivedPlayer.getAnteBet());
                    gameData.getPlayerOne().setPairPlusBet(receivedPlayer.getPairPlusBet());
                    gameData.getPlayerOne().setTotalWinnings(receivedPlayer.getTotalWinnings());

                    Deck receivedDeck = receivedInfo2.getDealer().getTheDeck();
                    gameData.getDealer().setTheDeck(receivedDeck);
                    theDealer = receivedInfo2.getDealer();

                    Queue<String> receivedChat = receivedInfo2.getChat();
                    gameData.setChat(receivedChat);
                    chat = receivedChat;

                    gameData.setPlayedHand(receivedInfo2.getPlayedHand());
                } 
                catch(Exception ex) { ex.printStackTrace(); System.exit(0); /*server crashed here*/ }
            }
            System.out.println("AFTER GS 16 LOOP");

            updateUI();
        });

        //let player actually process what's oging on before changing scenes
        transitionScene.setOnFinished( e -> {
            //for game return reasons, set playedHands to false and reset dealer's hand
            // theDealer.setDealersHand(null);

            gameData.setGameState(17);
            clientConnection.send(gameData);
            PokerInfo receivedInfo2 = PokerInfo.getInstance();
            while(receivedInfo2.getGameState() != 18)
            {
                System.out.println("IN GS 18 LOOP");
                try
                {
                    receivedInfo2 = (PokerInfo)clientConnection.in.readObject();
                    gameData.getDealer().setDealersHand(receivedInfo2.getDealer().getDealersHand());
                } 
                catch(Exception ex) { ex.printStackTrace(); System.exit(0); /*server crashed here*/ }
            }
            System.out.println("AFTER GS 18 LOOP");

            gameData.setPlayedHand(false);

            playAgainButton.setVisible(true);
            exitButton.setVisible(true);

        });

        flipDealerPause.play();
        evalHandsPause.play();
        transitionScene.play();
    }

    public void playAnother(ActionEvent e) 
    {
        try { loadPS1(e); } 
        catch(Exception ex) { ex.printStackTrace(); System.exit(1); }
        playAgainButton.setDisable(true);
        exitButton.setDisable(true);
    }

    public void terminateGame(ActionEvent e) { System.exit(0); }

    public void loadPS1(ActionEvent e) throws IOException {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PlayScreen1.fxml"));

        Parent ps1Root = loader.load(); //load view into parent
        ps1Root.getStylesheets().add(gameData.getStyle(1));

        pause.setOnFinished(ex-> {
                ps2Root.getScene().setRoot(ps1Root);//the scene graph
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
        ps2Root.getScene().setRoot(resetRoot);//update scene graph
    }

    @FXML
    public void newLook() throws IOException {
        ps2Root.getStylesheets().clear();
        ps2Root.getStylesheets().add(gameData.swapStyle(1));
    }

    @FXML
    public void exitGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ExitScreen.fxml"));
        Parent exitRoot = loader.load(); //load view into parent
        exitRoot.getStylesheets().add(gameData.getStyle(0));
        ps2Root.getScene().setRoot(exitRoot);//update scene graph
    }

    /******************************************************/
    /*               Returning to Game State              */
    /******************************************************/
    public void updateUI() {
        //update css
        ps2Root.getStylesheets().add(gameData.getStyle(1));

        //update card display
        updateCardDisplay(card7, card8, card9, theDealer.getDealersHand());
        updateCardDisplay(card1, card2, card3, playerOne.getHand());

        //update total winnings display
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

    public void updateCardDisplay(Button c1, Button c2, Button c3, ArrayList<Card> h) {
        String cardS1, cardS2, cardS3;
        if (h != null) {
            cardS1 = Integer.toString(h.get(0).getValue()) + Character.toString(h.get(0).getSuit()).toLowerCase() + ".png";
            cardS2 = Integer.toString(h.get(1).getValue()) + Character.toString(h.get(1).getSuit()).toLowerCase() + ".png";
            cardS3 = Integer.toString(h.get(2).getValue()) + Character.toString(h.get(2).getSuit()).toLowerCase() + ".png";

            Image pic1 = new Image(cardS1);
            ImageView c1Img = new ImageView(pic1);
            c1Img.setFitHeight(150);
            c1Img.setFitWidth(150);
            c1Img.setPreserveRatio(true);

            Image pic2 = new Image(cardS2);
            ImageView c2Img = new ImageView(pic2);
            c2Img.setFitHeight(150);
            c2Img.setFitWidth(150);
            c2Img.setPreserveRatio(true);

            Image pic3 = new Image(cardS3);
            ImageView c3Img = new ImageView(pic3);
            c3Img.setFitHeight(150);
            c3Img.setFitWidth(150);
            c3Img.setPreserveRatio(true);

            c1.setGraphic(c1Img);
            c2.setGraphic(c2Img);
            c3.setGraphic(c3Img);
        }
        else {
            if(gameData.getStyle(1).equals("/styles/PlayScreen1.css")){
                cardS1 = "bc.png";
                cardS2 = "bc.png";
                cardS3 = "bc.png";
            }
            else {
                cardS1 = "rc.png";
                cardS2 = "rc.png";
                cardS3 = "rc.png";
            }
            Image pic1 = new Image(cardS1);
            ImageView c1Img = new ImageView(pic1);
            c1Img.setFitHeight(150);
            c1Img.setFitWidth(150);
            c1Img.setPreserveRatio(true);

            Image pic2 = new Image(cardS2);
            ImageView c2Img = new ImageView(pic2);
            c2Img.setFitHeight(150);
            c2Img.setFitWidth(150);
            c2Img.setPreserveRatio(true);

            Image pic3 = new Image(cardS3);
            ImageView c3Img = new ImageView(pic3);
            c3Img.setFitHeight(150);
            c3Img.setFitWidth(150);
            c3Img.setPreserveRatio(true);

            c1.setGraphic(c1Img);
            c2.setGraphic(c2Img);
            c3.setGraphic(c3Img);
        }
    }



    }




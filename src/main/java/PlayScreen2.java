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
    GameData gameData = GameData.getInstance();
    Player playerOne = gameData.getPlayerOne();
    Player playerTwo = gameData.getPlayerTwo();
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

    private static boolean playedHand1 = false; //status of whether player played hand or not
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

    private static boolean playedHand2 = false; //status of whether player played hand or not
	@FXML
    private Label player2TW;
    @FXML
    private Button playHand2;
    @FXML
    private Button foldHand2;
    @FXML
    private Button card4;
    @FXML
    private Button card5;
    @FXML
    private Button card6;

    @FXML
    private TextArea chatLog;

    @FXML
    private MenuItem exitOption;
    @FXML
    private MenuItem restartOption;

	@Override
    public void initialize(URL location, ResourceBundle resources) {
        gameData.setGameState(1);

		gameData = GameData.getInstance();
		playerOne = gameData.getPlayerOne();
		playerTwo = gameData.getPlayerTwo();
		theDealer = gameData.getDealer();
        //always update UI in case we return from game from options
        updateUI();
    }
    
    @FXML
    public void playAction1(ActionEvent e) {
        //discard hand and lose money
        playHand1.setVisible(false);
        foldHand1.setVisible(false);
        playedHand1 = true;
        evalHands();
    }

    @FXML
    public void playAction2(ActionEvent e) {
        //discard hand and lose money
        playHand2.setVisible(false);
        foldHand2.setVisible(false);
        playedHand2 = true;
        evalHands();
    }

    @FXML
    public void foldAction1(ActionEvent e) {
        //discard hand and lose money
        playHand1.setVisible(false);
        foldHand1.setVisible(false);
        playedHand1 = false;
        evalHands();
    }

    @FXML
    public void foldAction2(ActionEvent e) {
        //discard hand and lose money
        playHand2.setVisible(false);
        foldHand2.setVisible(false);
        playedHand2 = false;
        evalHands();
    }

    public void evalHands() {
        //only evaluate hands if both players have decided to fold or play (whether their buttons are visible or not)
        if(playHand1.isVisible() || playHand2.isVisible()) return;
        
        //pause durations of dealer "flipping" cards and evaluating hands in case we want animations
        PauseTransition flipDealerPause = new PauseTransition(Duration.seconds(2));
        PauseTransition evalHandsPause = new PauseTransition(Duration.seconds(3));
        PauseTransition transitionScene = new PauseTransition(Duration.seconds(7)); //NEEDED BC PLAYERS GOTTA PROCESS WHATS GOING ON

        exitOption.setDisable(true); //DO NOT ALLOW PLAYERS TO EXIT GAME AND RETURN TO HAND WHILE EVALUATING CARDS
        restartOption.setDisable(true); //HAHA THEY CANT RESET IF THEY LOSE THEY HAVE TO WATCH

        //deal dealer's hand and pause on displaying it bc it's cool (in case we want to set up animations)
        theDealer.setDealersHand(theDealer.dealHand());
        //deal cards to dealer and evaluate hands
        flipDealerPause.setOnFinished( e -> {
            updateCardDisplay(card7, card8, card9, theDealer.getDealersHand());
        });

        //wait for dealer's cards to be flipped before finally evaluating hands and distribute winnings/losses
        evalHandsPause.setOnFinished( e -> {
            //settle winnings, update UI/chat, and go back to PlayScreen1
            chat.add("Turn " + gameData.addTurn() + ":");
            settlePlayerWinnings(playedHand1, playerOne, "Player 1");
            settlePlayerWinnings(playedHand2, playerTwo, "Player 2");
            chat.add(" ");
            updateUI();
        });

        //let player actually process what's oging on before changing scenes
        transitionScene.setOnFinished( e -> {
            //for game return reasons, set playedHands to false and reset dealer's hand
            theDealer.setDealersHand(null);
            playedHand1 = false;
            playedHand2 = false;
            try { loadPS1();} 
            catch(Exception ex) { ex.printStackTrace(); System.exit(1); }
        });

        flipDealerPause.play();
        evalHandsPause.play();
        transitionScene.play();
    }

    public void settlePlayerWinnings(boolean playedHand, Player p, String playerToString) 
    {
        //Player played hand
        if(playedHand)
        {
            //only settle ante if dealer's hand is valid
            if(ThreeCardLogic.validDealerHand(theDealer.getDealersHand()))
            {
                switch(ThreeCardLogic.compareHands(theDealer.getDealersHand(), p.getHand())) 
                {
                    case 0: { //push
                        chat.add(playerToString + " pushes against Dealer");
                        break;
                    }
                    case 1: { //dealer wins
                        chat.add(addDescription(1,p,playerToString));
                        chat.add(playerToString + " loses $" + Integer.toString(p.getAnteBet()));
                        p.setTotalWinnings(p.getTotalWinnings() - p.getAnteBet());
                        break;
                    }
                    case 2: { //player wins
                        chat.add(addDescription(0,p,playerToString));
                        chat.add(playerToString + " wins $" + Integer.toString(p.getAnteBet()));
                        p.setTotalWinnings(p.getTotalWinnings() + p.getAnteBet());
                        break;
                    }
                }
            }
            else {chat.add(playerToString + " pushes. Dealer doesn't have at least Queen High");}

            //settle pair plus winnings regardless of dealer's hand
            int ppWinnings = ThreeCardLogic.evalPPWinnings(p.getHand(), p.getPairPlusBet());
            if(ppWinnings > 0) 
            {
                int handValue = ThreeCardLogic.evalHand(p.getHand());
                chat.add(playerToString + " wins $" + Integer.toString(p.getPairPlusBet()) + " from Pair Plus with a " + handToString(handValue));
                p.setTotalWinnings(p.getTotalWinnings() + ppWinnings);
            }
            else 
            {
                chat.add(playerToString + " loses $" + Integer.toString(p.getPairPlusBet()) + " from Pair Plus");
                p.setTotalWinnings(p.getTotalWinnings() - ppWinnings);
            }
        }
        else
        {
            //otherwise, player one folded and must lose ante + pair plus (if made)
            chat.add(playerToString + " folds and loses $" + Integer.toString(p.getAnteBet() + p.getPairPlusBet()));
            p.setTotalWinnings(p.getTotalWinnings() - p.getAnteBet()); 
            p.setTotalWinnings(p.getTotalWinnings() - p.getPairPlusBet());
        }
    }

    public void loadPS1() throws IOException {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PlayScreen1.fxml"));

        Parent ps1Root = loader.load(); //load view into parent
        ps1Root.getStylesheets().add(gameData.getStyle(1));

        pause.setOnFinished(e-> {
                ps2Root.getScene().setRoot(ps1Root);//update scene graph
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
        updateCardDisplay(card4, card5, card6, playerTwo.getHand());

        //update total winnings display
        if(playerOne.getTotalWinnings() >= 0) player1TW.setText("Player 1 Total Winnings: $" + Integer.toString(playerOne.getTotalWinnings()));
        else player1TW.setText("Player 1 Total Winnings: -$" + Integer.toString(playerOne.getTotalWinnings()*-1));
		if(playerTwo.getTotalWinnings() >= 0) player2TW.setText("Player 2 Total Winnings: $" + Integer.toString(playerTwo.getTotalWinnings()));
        else player2TW.setText("Player 2 Total Winnings: -$" + Integer.toString(playerTwo.getTotalWinnings()*-1));

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

    public String addDescription(int winner, Player p, String playerToString){
        String winnerString, winnerDescription, loserDescription;
        int dealerValue = ThreeCardLogic.evalHand(theDealer.getDealersHand());
        int playerValue = ThreeCardLogic.evalHand(p.getHand());
        if (winner == 1){
            winnerString = "Dealer";
            winnerDescription = handToString(dealerValue);
            loserDescription = handToString(playerValue);
        }
        else {
            winnerString = playerToString;
            winnerDescription = handToString(playerValue);
            loserDescription = handToString(dealerValue);
        }
        if (playerValue == dealerValue){
            return winnerString + " won the game with a High Card";
        }
        return winnerString + " won the game, " + winnerDescription + " beats " + loserDescription;
    }

    public String handToString(int hand){
        switch(hand){
            case 1:
                return "Straight Flush";
            case 2:
                return "Three of a Kind";
            case 3:
                return "Straight";
            case 4:
                return "Flush";
            case 5:
                return "Pair";
            default:
                return "High Card";
        }
    }

    }




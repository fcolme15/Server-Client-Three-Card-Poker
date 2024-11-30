import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JavaFXTemplate extends Application {
	/*********************************************************************************/
	/********************** 	APPLICATION INITALIZATION 		**********************/
	/*********************************************************************************/
	GameData gameData;
	Player playerOne;
	Player playerTwo;
	Dealer theDealer;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		try
		{	
			Parent root = FXMLLoader.load(getClass().getResource("/FXML/StartScreen.fxml"));
			Scene s1 = new Scene(root, 1000, 800);
			s1.getStylesheets().add("/styles/WelcomeScreen.css");
			
			primaryStage.setTitle("Three Card Poker");
			primaryStage.setScene(s1);
			primaryStage.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*********************************************************************************/
	/*****************		PLAYSCREEN1 METHODS + COMPONENTS		******************/
	/*********************************************************************************/
	// @FXML
    // private BorderPane gsRoot;

    // @FXML
    // private Label dealerLabel;
    // @FXML
    // private Button card7;
    // @FXML
    // private Button card8;
    // @FXML
    // private Button card9;

	// @FXML
    // private Label player1TW;
    // @FXML
    // private Button player1Button;
    // @FXML
    // private Button card1;
    // @FXML
    // private Button card2;
    // @FXML
    // private Button card3;

	// @FXML
    // private Label player2TW;
    // @FXML
    // private Button player2Button;
    // @FXML
    // private Button card4;
    // @FXML
    // private Button card5;
    // @FXML
    // private Button card6;


	// @Override
    // //public void initialize() {
    // public void initialize(URL location, ResourceBundle resources) {
	// 	gameData = GameData.getInstance();
	// 	playerOne = gameData.getPlayerOne();
	// 	playerTwo = gameData.getPlayerTwo();
	// 	theDealer = gameData.getDealer();
	// 	player1TW.setText(player1TW.getText() + Integer.toString(playerOne.getTotalWinnings()));
	// 	player2TW.setText(player2TW.getText() + Integer.toString(playerTwo.getTotalWinnings()));
    //     // gsRoot.getStylesheets().add("/styles/PlayScreen1S.css");
    //     // TODO Auto-generated method stub
    // }

    // public void psButton1(ActionEvent e) throws IOException {
    //     System.out.println("PlayScreen1 controller button1 ");

    //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PlayScreen2.fxml"));
    //     Parent ps2Root = loader.load(); //load view into parent

	// 	ps2Root.getStylesheets().add("/styles/PlayScreen1S.css");
    //     gsRoot.getScene().setRoot(ps2Root);//update scene graph
    // }

    // @FXML
    // public void psButton2() {
    //     // Handle Button 2 action
    //     System.out.println("PlayScreen1 controller button2");
    //     if(player2Button.getText() == "player button 2 change!!!") {player2Button.setText("Button 2");}
    //     else {player2Button.setText("player button 2 change!!!");}

    //     playerTwo.setHand(theDealer.dealHand());
    //     for(Card c : playerTwo.getHand())
    //     {
    //         System.out.print(c.getSuit());
    //         System.out.println(c.getValue());
    //     }

    //     String card4Text = playerTwo.getHand().get(0).getSuit() + Integer.toString(playerTwo.getHand().get(0).getValue());
    //     String card5Text = playerTwo.getHand().get(1).getSuit() + Integer.toString(playerTwo.getHand().get(1).getValue());
    //     String card6Text = playerTwo.getHand().get(2).getSuit() + Integer.toString(playerTwo.getHand().get(2).getValue());
    //     card4.setText(card4Text);
    //     card5.setText(card5Text);
    //     card6.setText(card6Text);
    //     // player2Button.setDisable(true);

    //     player2Button.setVisible(false);
    // }

	/*********************************************************************************/
	/*****************		EXIT SCREEN METHODS + COMPONENTS		******************/
	/*********************************************************************************/
    // @FXML
    // public void freshStart() {

    // }

    // @FXML
    // public void newLook() {
        
    // }

    // @FXML
    // public void exitGame() throws IOException {
    //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ExitScreenServer.fxml"));
    //     Parent exitRoot = loader.load(); //load view into parent

	// 	exitRoot.getStylesheets().add("/styles/WelcomeScreenServer.css");
    //     gsRoot.getScene().setRoot(exitRoot);//update scene graph
    // }
}

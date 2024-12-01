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

}

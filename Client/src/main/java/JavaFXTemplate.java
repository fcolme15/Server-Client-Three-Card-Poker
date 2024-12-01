import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

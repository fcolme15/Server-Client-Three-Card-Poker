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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.ListView;

public class PlayScreen1 implements Initializable{
    GameData gameData = GameData.getInstance();
    Player playerOne = gameData.getPlayerOne();
    Dealer theDealer = gameData.getDealer();
    Queue<String> chat = gameData.getChat();
    ImageView c1Img;

    @FXML
    private BorderPane ps1Root;

    @FXML
    private Button numClients;

    @FXML
    private Label titleLabel;

    @FXML
    private Label statsLabel;

    @FXML
    private ListView statsList;

    @FXML
    private Label clientLabel;

    @FXML
    private ListView clientList;

    @Override
    //public void initialize() {
    public void initialize(URL location, ResourceBundle resources) {
        gameData.setGameState(0);
        //numClients.setEditable(false);

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ServerScreen.fxml"));
        Parent resetRoot = loader.load(); //load view into parent
        ps1Root.getScene().setRoot(resetRoot);//update scene graph
    }

    @FXML
    public void newLook() throws IOException {
        ps1Root.getStylesheets().clear();
        ps1Root.getStylesheets().add(gameData.swapStyle(1));
    }

    @FXML
    public void exitServer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ExitScreen.fxml"));
        Parent exitRoot = loader.load(); //load view into parent

		exitRoot.getStylesheets().add(gameData.getStyle(0));
        ps1Root.getScene().setRoot(exitRoot);//update scene graph
    }

}
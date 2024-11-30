import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.application.Platform;


public class StartController implements Initializable{

    @FXML
    private BorderPane ssRoot; //start screen

    @FXML
    private Label titleLabel;

    @FXML
    private Label serverLabel;

    @FXML
    private TextField portNum;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button returnButton;

    @FXML
    private Button exitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ssRoot.getStylesheets().add(GameData.getInstance().getStyle(0));

//        portNum.focusedProperty().addListener((o, oldValue, newValue) -> {
//            if (newValue) {
//                Platform.runLater(() -> {
//                    int carretPosition = portNum.getCaretPosition();
//                    if (portNum.getAnchor() != carretPosition) {
//                        portNum.selectRange(carretPosition, carretPosition);
//                    }
//                });
//            }
//        });

        //Filter for text field portNum to only accept numbers
        UnaryOperator<Change> NumberFilter = change -> {
            String newtxt = change.getControlNewText();
            String textused = change.getText();

            if (newtxt.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        };

        //Formatter for text field portNum to only accept numbers
        TextFormatter<String> NumberFormatter = new TextFormatter<>(NumberFilter);
        portNum.setTextFormatter(NumberFormatter);
    }

    public void startServer(ActionEvent e) throws IOException {
        //get instance of the loader class

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PlayScreen1.fxml"));
        Parent ps1Root = loader.load(); //load view into parent

        ps1Root.getStylesheets().add(GameData.getInstance().getStyle(1));
        ssRoot.getScene().setRoot(ps1Root);//update scene graph
    }

    public void returnToServer(ActionEvent e) throws IOException {
        String playScreen;
        switch(GameData.getInstance().getGameState()) {
            case 0: 
                playScreen = "/FXML/PlayScreen1.fxml";
                break;
            case 1: 
                playScreen = "/FXML/PlayScreen2.fxml";
                break;
            default:
                //its actually broken if this executes somehow
                throw new IOException("bruh you broke it");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(playScreen));
        Parent psRoot = loader.load(); //load view into parent

        psRoot.getStylesheets().add(GameData.getInstance().getStyle(1));
        ssRoot.getScene().setRoot(psRoot);//update scene graph
    }

    //Sets loader with start screen fxml
    public void newLook1() throws IOException {
        ssRoot.getStylesheets().clear();
        ssRoot.getStylesheets().add(GameData.getInstance().swapStyle(0));
    }

    //Sets loader with exit screen fxml
    public void newLook2() throws IOException {
        ssRoot.getStylesheets().clear();
        ssRoot.getStylesheets().add(GameData.getInstance().swapStyle(0));
    }

    public void exitServer(ActionEvent e) {
        System.exit(0);
    }
}
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
import javafx.scene.control.TextField;

import javafx.application.Platform;
import javafx.collections.ObservableList;

public class StartController implements Initializable{
    Server serverConnection;
    static private ObservableList<String> realClientList;

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

    @FXML
    private Label invalidText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ssRoot.getStylesheets().add(GameData.getInstance().getStyle(0));
    }

    //thank you compiler design
    private boolean onlyDigits(String s)
    {
        return s.matches("[0-9]+");
    }

    public void toServer(ActionEvent e) throws IOException {
        //make sure port number only consists of digits in here
        if(!onlyDigits(portNum.getText())) { invalidText.setVisible(true); return; }

        //if port number only consists of digits then we can start the server!
        int portNumber = Integer.parseInt(portNum.getText());
        serverConnection = Server.getInstance(gameData -> {
            Platform.runLater(()->{});}, 
        portNumber);

        realClientList = Server.realClientList;
        //realClientList.addAll("from start controollere");
        // clientList.setItems(realClientList);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ServerScreen.fxml"));
        Parent ps1Root = loader.load(); //load view into parent

        ps1Root.getStylesheets().add(GameData.getInstance().getStyle(1));
        ssRoot.getScene().setRoot(ps1Root);//update scene graph
    }

    public void returnToServer(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ServerScreen.fxml"));
        Parent ps1Root = loader.load(); //load view into parent

        ps1Root.getStylesheets().add(GameData.getInstance().getStyle(1));
        ssRoot.getScene().setRoot(ps1Root);//update scene graph
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
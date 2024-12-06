import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.Socket;

public class StartController implements Initializable{

    Client clientConnection;

    @FXML
    private BorderPane ssRoot; //start screen

    @FXML
    private Label titleLabel;

    @FXML
    private TextField portField;

    @FXML
    private TextField ipField;

    @FXML 
    private Button connectButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label invalidText;

    @Override
    //public void initialize() {
    public void initialize(URL location, ResourceBundle resources) {
        ssRoot.getStylesheets().add(PokerInfo.getInstance().getStyle(0));
    }

    //thank you compiler design
    private boolean onlyDigits(String s)
    {
        return s.matches("[0-9]+");
    }

    public void startGame(ActionEvent e) throws IOException {
        //make sure port number is valid
        if(!onlyDigits(portField.getText())) 
        { 
            invalidText.setText("Invalid Port Number..."); 
            invalidText.setVisible(true); 
            return; 
        }
        
        //disable text fields and button so we can process the IP address and port num
        portField.setDisable(true);
        ipField.setDisable(true);
        connectButton.setDisable(true);
        connectButton.setText("Connecting...");

        try
        {
            int portNumber = Integer.parseInt(portField.getText());
            String ipAddress = ipField.getText();
    
            try(Socket socket = new Socket(ipAddress, portNumber))
            {
                clientConnection = Client.getInstance(
                    gameData -> {Platform.runLater(()->{});}, portNumber, ipAddress);
                    clientConnection.start();
                    
                /* load into game */
                //get instance of the loader class
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PlayScreen1.fxml"));
                Parent ps1Root = loader.load(); //load view into parent --> CRASHES HERE
                    
                ps1Root.getStylesheets().add(PokerInfo.getInstance().getStyle(1));
                ssRoot.getScene().setRoot(ps1Root);//update scene graph
            }
            catch(Exception ex) 
            {  
                invalidText.setText("Failed to Connect, try again");
                invalidText.setVisible(true);
                portField.setDisable(false);
                ipField.setDisable(false);
                connectButton.setDisable(false);
                connectButton.setText("Connect to Server");
                ex.toString();
            }
        }
        catch(Exception ex) { ex.toString(); }
    }

    public void returnToGame(ActionEvent e) throws IOException {
        String playScreen;
        switch(PokerInfo.getInstance().getGameState()) {
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

        psRoot.getStylesheets().add(PokerInfo.getInstance().getStyle(1));
        ssRoot.getScene().setRoot(psRoot);//update scene graph
    }

    //Sets loader with start screen fxml
    public void newLook1() throws IOException {
        ssRoot.getStylesheets().clear();
        ssRoot.getStylesheets().add(PokerInfo.getInstance().swapStyle(0));
    }

    //Sets loader with exit screen fxml
    public void newLook2() throws IOException {
        ssRoot.getStylesheets().clear();
        ssRoot.getStylesheets().add(PokerInfo.getInstance().swapStyle(0));
    }

    public void exitGame(ActionEvent e) {
        System.exit(0);
    }
}
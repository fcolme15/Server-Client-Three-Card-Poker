import java.util.Queue;
import java.util.LinkedList;

public class GameData {
    private static GameData gameData;
    private int gameState; //used for determining which screen to go to when returning from exit screen
    private Player playerOne;
    private Player playerTwo;
    private Dealer theDealer;
    private String[] swapScreen;
    private int state;

    //chat properties
    private int numOfTurns;
    private Queue<String> chat; //used to win/loss history\

    private GameData() {
        gameState = 0;
        playerOne = new Player();
        playerTwo = new Player();
        theDealer = new Dealer();

        numOfTurns = 1;
        chat = new LinkedList<>();
        swapScreen = new String[4];
        swapScreen[0] = "/styles/PlayScreen1.css";
        swapScreen[1] = "/styles/PlayScreen2.css";
        swapScreen[2] = "/styles/WelcomeScreen.css";
        swapScreen[3] = "/styles/WelcomeScreen2.css";
        state = 0;
    }

    public String swapStyle(int page){
        state += 1;
        if (page == 0){
            return swapScreen[(state%2)+2];
        }
        return swapScreen[state%2];
    }

    public String getStyle(int page){
        if (page == 0){
            return swapScreen[(state%2)+2];
        }
        return swapScreen[state%2];
    }

    public static GameData getInstance() {
        if(gameData == null) {
            gameData = new GameData();
            return gameData;
        }
        return gameData;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Dealer getDealer() {
        return theDealer;
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int s) {
        gameState = s;
    }

    public Queue<String> getChat() {
        return chat;
    }

    public void setChat(Queue<String> t) {
        chat = t;
    }

    public void addChat(String text) {
        chat.add(text + "\n");
    }

    public String addTurn() {
        //return number of turns as a string for easy formatting before incrementing it
        return Integer.toString(numOfTurns++);
    }

    public void resetGame() {
        gameState = 0;
        playerOne = new Player();
        playerTwo = new Player();
        theDealer = new Dealer();
        numOfTurns = 1;
        chat.clear();
        // state = 0;
    }
}

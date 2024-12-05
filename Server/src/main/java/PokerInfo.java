import java.io.Serializable;
import java.util.Queue;
import java.util.LinkedList;

public class PokerInfo implements Serializable {
    private static PokerInfo gameData;

    /* 
    0 -> PLAY SCREEN 1: player places ante bet and pair plus bet, player gets dealt hand, go to PS2
    1 -> PLAY SCREEN 2: player plays or folds, dealer reveals hand, analyze hands, 
                        payout money, back to PS1

    11 -> Server deals player's hand
    12 -> Client receives dealt player hand
    13 -> Server deals Dealer's hand
    14 -> Client receives dealt dealer hand
    15 -> Server evaluates hands
    16 -> Client receives game results
    */
    private int gameState; //used for determining which screen to go to when returning from exit screen
    private Player playerOne;
    private boolean playedHand;
    private Dealer theDealer;
    private String[] swapScreen;
    private int state; //used to keep track of CSS sheet on a given screen

    //chat properties
    private int numOfTurns;
    private Queue<String> chat; //used to win/loss history

    private PokerInfo() {
        gameState = 0;

        playerOne = new Player();
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

    public static PokerInfo getInstance() {
        if(gameData == null) {
            gameData = new PokerInfo();
            return gameData;
        }
        return gameData;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Dealer getDealer() {
        return theDealer;
    }

    public boolean getPlayedHand() {
        return playedHand;
    }

    public void setPlayedHand(boolean c) {
        playedHand = c;
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
        theDealer = new Dealer();
        numOfTurns = 1;
        chat.clear();
    }
}

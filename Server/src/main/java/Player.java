import java.util.ArrayList;
import java.io.Serializable;

public class Player implements Serializable {
    private ArrayList<Card> hand;
    private int anteBet;
    private int playBet;
    private int pairPlusBet;
    private int totalWinnings;

    public Player() {
        this.hand = new ArrayList<Card>();
        this.hand = null;
        this.anteBet = 0;
        this.playBet = 0;
        this.pairPlusBet = 0;
        this.totalWinnings = 0;
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public int getAnteBet() {
        return this.anteBet;
    }

    public void setAnteBet(int anteBet) {
        this.anteBet = anteBet;
    }

    public int getPlayBet() {
        return this.playBet;
    }

    public void setPlayBet(int playBet) {
        this.playBet = playBet;
    }

    public int getPairPlusBet() {
        return this.pairPlusBet;
    }

    public void setPairPlusBet(int pairPlusBet) {
        this.pairPlusBet = pairPlusBet;
    }

    public int getTotalWinnings() {
        return this.totalWinnings;
    }

    public void setTotalWinnings(int totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

}

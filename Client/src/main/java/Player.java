import java.util.ArrayList;
import java.io.Serializable;

public class Player implements Serializable {
    private ArrayList<Card> hand;
    private int anteBet;
    private int playBet;
    private int pairPlusBet;
    private int totalWinnings;
    private int lastestHandWinnings;
    private int pushedAnte;
    private int wonLastHand;
    private int pairPlusWon;

    public Player() {
        this.hand = new ArrayList<Card>();
        this.hand = null;
        this.anteBet = 0;
        this.playBet = 0;
        this.pairPlusBet = 0;
        this.totalWinnings = 0;
        this.lastestHandWinnings = 0;
        this.pushedAnte = 0;
        this.wonLastHand = 0;
        this.pairPlusWon = 0;
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

    public int getLastestHandWinnings() {return this.lastestHandWinnings;}

    public void setLastestHandWinnings(int latest) {this.lastestHandWinnings = latest;}

    public int getPushedAnte() {return this.pushedAnte;}

    public void setPushedAnte(int push) {this.pushedAnte = push;}

    public int getWonLastHand() {return this.wonLastHand;}

    public void setWonLastHand(int whoWon) {this.wonLastHand = whoWon;}

    public int getPairPlusWon() {return this.pairPlusWon;}

    public void setPairPlusWon(int won) {this.pairPlusWon = won;}

}

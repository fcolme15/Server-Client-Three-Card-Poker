import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;

public class Deck extends ArrayList<Card> implements Serializable {

    private char[] suits = {'C', 'D', 'H', 'S'};

    public Deck() {
        for(int i = 2; i < 15; i++){
            for(int j = 0; j < 4; j++){
                this.add(new Card(suits[j], i));
            }
        }
    }

    public void shuffleDeck (){
        Collections.shuffle(this);
    }

    public void newDeck() {
        this.clear(); //clear the current deck
        for(int i = 2; i < 15; i++){
            for(int j = 0; j < 4; j++){
                this.add(new Card(suits[j], i));
            }
        }
    }

    public Card dealCard(){
        return this.remove(0); //remove and return the card at the same time
    }

}

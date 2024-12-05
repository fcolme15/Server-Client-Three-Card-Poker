import java.util.ArrayList;
import java.io.Serializable;

public class Dealer implements Serializable {
    private Deck theDeck;
    private ArrayList<Card> dealersHand;

    //no arg constructor will initialize the deck
    Dealer() 
    {
        this.theDeck = new Deck(); //set theDeck using Deck's constructor
        theDeck.shuffleDeck(); //start with preshuffled deck
        this.dealersHand = null; //set dealer's hand empty to start
    }


    //dealHand(): return an ArrayList<Card> of three cards removed from theDeck
    public ArrayList<Card> dealHand()
    {
        //retrieve a new deck if the deck's size is less than or equal to 34
        if(theDeck.size() <= 34)
        {
            theDeck.newDeck();
            theDeck.shuffleDeck(); //reshuffle new deck
        }

        //deal hand that we will return
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(theDeck.dealCard());
        hand.add(theDeck.dealCard());
        hand.add(theDeck.dealCard());

        return hand;
    }

    //getters + setters
    public Deck getTheDeck()
    {
        return theDeck;
    }

    public void setTheDeck(Deck d)
    {
        this.theDeck = d;
    }

    public ArrayList<Card> getDealersHand()
    {
        return dealersHand;
    }

    public void setDealersHand(ArrayList<Card> h)
    {
        this.dealersHand = h;
    }
}

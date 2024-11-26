import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;

class DealerTest {
    @Test
    void constructorTest()
    {
        Dealer dealer = new Dealer();
        Deck theDeck = dealer.getTheDeck();

        assertEquals(52, theDeck.size(), "Dealer's theDeck not initialized with 52 cards");
        assertNull(dealer.getDealersHand()); //make sure dealer's hand is empty
    }

    @Test
    void testDealHand()
    {
        Dealer dealer = new Dealer();

        ArrayList<Card> hand = dealer.dealHand();

        assertEquals(3, hand.size(), "Hand dealt wrong size");
        assertEquals(49, dealer.getTheDeck().size(), "Unadjusted Dealer's theDeck size");
    }

    @Test
    void testDeckReshuffle()
    {
        Dealer dealer = new Dealer();
        int[] deckSizes = {49, 46, 43, 40, 37, 34};

        for(int s : deckSizes) 
        {
            dealer.dealHand();
            assertEquals(s, dealer.getTheDeck().size(), "Did not adjust dealer's deck size");
        }

        //At this point, the dealer's deck is at 34, if I call dealHand(), it should reset the deck and dealing would make it 49
        dealer.dealHand();
        assertEquals(49, dealer.getTheDeck().size(), "Did not reset the deck");
    }

    @Test
    void testDeckRandomness() 
    {
        Dealer dealer1 = new Dealer();
        Dealer dealer2 = new Dealer();

        assertEquals(dealer1.getTheDeck().size(), dealer2.getTheDeck().size(), "Both deck sizes should be the same");

        //check that both decks are actually different (sure there is a slight chance that they both might be the same)
        assertFalse(dealer1.getTheDeck().equals(dealer2.getTheDeck()));
    }
}

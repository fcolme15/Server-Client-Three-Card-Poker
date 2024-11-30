import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;

class DeckTest {

	@Test
	void ConstructorTest() {
		Deck theDeck = new Deck();
		assertEquals(theDeck.size(), 52,  "Deck is not initialzed with 52 cards");

		char[] suits = {'C', 'D', 'H', 'S'};
		int index = 0;
		//Loop through the deck checking that every card is initialized
		for(int i = 2; i < 15; i++){
			for(int j = 0; j < 4; j++){
				assertEquals(theDeck.get(index).getValue(), i,  "Value of the card is different than expected");
				assertEquals(theDeck.get(index).getSuit(), suits[j],  "The Suit of the card is different than expected");
				index++;
			}
		}
	}

	@Test
	void newDeckTest1() {
		Deck theDeck = new Deck();
		assertEquals(theDeck.size(), 52, "Deck is not initialzed with 52 cards");
		char[] suits = {'C', 'D', 'H', 'S'};
		int index = 0;
		//Loop through the deck checking that every card is initialized
		for (int i = 2; i < 15; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(theDeck.get(index).getValue(), i, "Value of the card is different than expected");
				assertEquals(theDeck.get(index).getSuit(), suits[j], "The Suit of the card is different than expected");
				index++;
			}
		}

		//Call for a new deck
		theDeck.newDeck();
		//Call for the new deck
		assertEquals(theDeck.size(), 52, "Deck is not initialzed with 52 cards");
		index = 0;
		//Loop through the deck checking that every card is initialized in the new deck
		for (int i = 2; i < 15; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(theDeck.get(index).getValue(), i, "Value of the card in newDeck is different than expected");
				assertEquals(theDeck.get(index).getSuit(), suits[j], "The Suit of the card in newDeck is different than expected");
				index++;
			}
		}
		assertEquals(theDeck.size(), 52, "Deck is not initialzed with 52 cards");
	}

	@Test
	void newDeckTest2() {
		Deck theDeck = new Deck();
		assertEquals(theDeck.size(), 52, "Deck is not initialzed with 52 cards");
		char[] suits = {'C', 'D', 'H', 'S'};
		int index = 0;
		//Loop through the deck checking that every card is initialized
		for (int i = 2; i < 15; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(theDeck.get(index).getValue(), i, "Value of the card is different than expected");
				assertEquals(theDeck.get(index).getSuit(), suits[j], "The Suit of the card is different than expected");
				index++;
			}
		}

		//Remove cards from the deck to make sure newDeck isn't the same as old
		Card cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 51, "Deck should have 51 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 50, "Deck should have 50 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 49, "Deck should have 49 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 48, "Deck should have 48 cards");

		//Call for a new deck
		theDeck.newDeck();
		//Call for the new deck
		assertEquals(theDeck.size(), 52, "Deck is not initialzed with 52 cards");
		index = 0;
		//Loop through the deck checking that every card is initialized in the new deck
		for (int i = 2; i < 15; i++) {
			for (int j = 0; j < 4; j++) {
				assertEquals(theDeck.get(index).getValue(), i, "Value of the card in newDeck is different than expected");
				assertEquals(theDeck.get(index).getSuit(), suits[j], "The Suit of the card in newDeck is different than expected");
				index++;
			}
		}
		assertEquals(theDeck.size(), 52, "Deck is not initialzed with 52 cards");
	}

	@Test
	void dealCardTest1(){
		Deck theDeck = new Deck();
		assertEquals(theDeck.size(), 52,  "Deck is not initialzed with 52 cards");

		//Check that dealCard returns a card and removes it
		Card cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 51,  "Deck should have 51 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 50,  "Deck should have 50 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 49,  "Deck should have 49 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 48,  "Deck should have 48 cards");

		boolean typeRet = false;
		if (cardDelt instanceof Card) {
			typeRet = true;
		}
		assertEquals(typeRet, true,  "Return of card delt is not Card");

		//Remove more cards using dealCard and check for proper size
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 38,  "Deck should have 38 cards");
	}

	@Test
	void dealCardTest2(){
		Deck theDeck = new Deck();
		assertEquals(theDeck.size(), 52,  "Deck is not initialzed with 52 cards");

		//Check that dealCard returns a card and removes it
		Card cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 51,  "Deck should have 51 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 50,  "Deck should have 50 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 49,  "Deck should have 49 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 48,  "Deck should have 48 cards");

		boolean typeRet = false;
		if (cardDelt instanceof Card) {
			typeRet = true;
		}
		assertEquals(typeRet, true,  "Return of card delt is not Card");

		//Shuffle deck and make sure dealCard still works
		theDeck.shuffleDeck();

		assertEquals(theDeck.size(), 48,  "Deck is not initialzed with 48 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 47,  "Deck is not initialzed with 47 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 46,  "Deck is not initialzed with 46 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 45,  "Deck is not initialzed with 45 cards");
		cardDelt = theDeck.dealCard();
		assertEquals(theDeck.size(), 44,  "Deck is not initialzed with 44 cards");
	}

	@Test
	void shuffleDeckTest(){
		Deck theDeck = new Deck();
		assertEquals(theDeck.size(), 52, "Deck is not initialzed with 52 cards");
		char[] suits = {'C', 'D', 'H', 'S'};
		int index = 0;
		boolean cardDiff = true;

		ArrayList<Card> originalHand = new ArrayList<Card>();;
		for (int i = 0; i < 3; i++) {
			originalHand.add(theDeck.get(i));
		}
		theDeck.shuffleDeck();
		if (originalHand.get(0) == theDeck.get(0) && originalHand.get(1) == theDeck.get(1) && originalHand.get(2) == theDeck.get(2)) {
			cardDiff = false;
		}

		assertEquals(cardDiff, true,  "Hands aren't different");

		cardDiff = true;
		originalHand = new ArrayList<Card>();;
		for (int i = 0; i < 3; i++) {
			// originalHand.add(deck.get(i));
			originalHand.add(theDeck.get(i));
		}
		theDeck.shuffleDeck();
		if (originalHand.get(0) == theDeck.get(0) && originalHand.get(1) == theDeck.get(1) && originalHand.get(2) == theDeck.get(2)) {
			cardDiff = false;
		}
		assertEquals(cardDiff, true,  "Hands aren't different");
	}

}

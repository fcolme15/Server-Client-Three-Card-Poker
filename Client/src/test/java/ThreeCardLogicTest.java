import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;

class ThreeCardLogicTest {

	@Test
	void evalHandHighCardTest1() {
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add((new Card('C',2)));
		hand1.add((new Card('H',4)));
		hand1.add((new Card('D',7)));
		int handVal = ThreeCardLogic.evalHand(hand1);
		assertEquals(handVal, 0,  "Hand value isn't evaluating as high card");
	}

	@Test
	void evalHandHighCardTest2() {
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add((new Card('S',12)));
		hand2.add((new Card('H',8)));
		hand2.add((new Card('D',9)));
		int handVal = ThreeCardLogic.evalHand(hand2);
		assertEquals(handVal, 0,  "Hand value isn't evaluating as high card");
	}

	@Test
	void evalHandStraightFlushTest1() {
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add((new Card('C',2)));
		hand1.add((new Card('C',4)));
		hand1.add((new Card('C',3)));
		int handVal = ThreeCardLogic.evalHand(hand1);
		assertEquals(handVal, 1,  "Hand value isn't evaluating as Straight Flush");
	}

	@Test
	void evalHandStraightFlushTest2() {
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add((new Card('D',12)));
		hand2.add((new Card('D',13)));
		hand2.add((new Card('D',14)));
		int handVal = ThreeCardLogic.evalHand(hand2);
		assertEquals(handVal, 1,  "Hand value isn't evaluating as Straight Flush");
	}

	@Test
	void evalHandThreeKindTest1() {
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add((new Card('C',2)));
		hand1.add((new Card('S',2)));
		hand1.add((new Card('D',2)));
		int handVal = ThreeCardLogic.evalHand(hand1);
		assertEquals(handVal, 2,  "Hand value isn't evaluating as three of a kind");
	}

	@Test
	void evalHandThreeKindTest2() {
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add((new Card('D',12)));
		hand2.add((new Card('D',12)));
		hand2.add((new Card('S',12)));
		int handVal = ThreeCardLogic.evalHand(hand2);
		assertEquals(handVal, 2,  "Hand value isn't evaluating as three of a kind");
	}

	@Test
	void evalHandStraightTest1() {
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add((new Card('C',2)));
		hand1.add((new Card('S',3)));
		hand1.add((new Card('D',4)));
		int handVal = ThreeCardLogic.evalHand(hand1);
		assertEquals(handVal, 3,  "Hand value isn't evaluating as staight");
	}

	@Test
	void evalHandStraightTest2() {
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add((new Card('D',13)));
		hand2.add((new Card('H',14)));
		hand2.add((new Card('S',15)));
		int handVal = ThreeCardLogic.evalHand(hand2);
		assertEquals(handVal, 3,  "Hand value isn't evaluating as straight");
	}

	@Test
	void evalHandFlushTest1() {
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add((new Card('S',4)));
		hand1.add((new Card('S',8)));
		hand1.add((new Card('S',9)));
		int handVal = ThreeCardLogic.evalHand(hand1);
		assertEquals(handVal, 4,  "Hand value isn't evaluating as flush");
	}

	@Test
	void evalHandFlushTest2() {
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add((new Card('H',6)));
		hand2.add((new Card('H',5)));
		hand2.add((new Card('H',15)));
		int handVal = ThreeCardLogic.evalHand(hand2);
		assertEquals(handVal, 4,  "Hand value isn't evaluating as flush");
	}

	@Test
	void evalHandPairTest1() {
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add((new Card('S',8)));
		hand1.add((new Card('D',8)));
		hand1.add((new Card('H',9)));
		int handVal = ThreeCardLogic.evalHand(hand1);
		assertEquals(handVal, 5,  "Hand value isn't evaluating as pair");
	}

	@Test
	void evalHandPairTest2() {
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add((new Card('H',6)));
		hand2.add((new Card('D',15)));
		hand2.add((new Card('C',15)));
		int handVal = ThreeCardLogic.evalHand(hand2);
		assertEquals(handVal, 5,  "Hand value isn't evaluating as pair");
	}

	@Test
	void evalPPWinningsHighCardTest() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add((new Card('H',6)));
		hand.add((new Card('D',5)));
		hand.add((new Card('C',15)));
		int winnings = ThreeCardLogic.evalPPWinnings(hand, 1);
		assertEquals(winnings, 0,  "Winnings doesn't match expected value 0");
	}

	@Test
	void evalPPWinningsStraightFlushTest() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add((new Card('H',13)));
		hand.add((new Card('H',14)));
		hand.add((new Card('H',15)));
		int winnings = ThreeCardLogic.evalPPWinnings(hand, 1);
		assertEquals(winnings, 41,  "Winnings doesn't match expected value 41");
	}

	@Test
	void evalPPWinningsThreeKindTest() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add((new Card('H',5)));
		hand.add((new Card('D',5)));
		hand.add((new Card('C',5)));
		int winnings = ThreeCardLogic.evalPPWinnings(hand, 1);
		assertEquals(winnings, 31,  "Winnings doesn't match expected value 31");
	}

	@Test
	void evalPPWinningsStraightTest() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add((new Card('H',5)));
		hand.add((new Card('D',6)));
		hand.add((new Card('C',7)));
		int winnings = ThreeCardLogic.evalPPWinnings(hand, 1);
		assertEquals(winnings, 7,  "Winnings doesn't match expected value 7");
	}

	@Test
	void evalPPWinningsFlushTest() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add((new Card('C',5)));
		hand.add((new Card('C',8)));
		hand.add((new Card('C',10)));
		int winnings = ThreeCardLogic.evalPPWinnings(hand, 1);
		assertEquals(winnings, 4,  "Winnings doesn't match expected value 4");
	}

	@Test
	void evalPPWinningsPairTest() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add((new Card('D',8)));
		hand.add((new Card('S',8)));
		hand.add((new Card('C',10)));
		int winnings = ThreeCardLogic.evalPPWinnings(hand, 1);
		assertEquals(winnings, 2,  "Winnings doesn't match expected value 2");
	}

	@Test
	void compareHandsTie1() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('D',9)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',8)));
		playerHand.add((new Card('S',9)));
		playerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 0,  "Should be a tie");
	}

	@Test
	void compareHandsTie2() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('S',9)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',8)));
		playerHand.add((new Card('D',9)));
		playerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 0,  "Should be a tie");
	}

	@Test
	void compareHandsHighCardVStraightFlush() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('D',9)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',8)));
		playerHand.add((new Card('D',4)));
		playerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 1,  "Dealer should win Straight Flush beats High Card");
	}

	@Test
	void compareHandsThreeKindVStraightFlush() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('D',9)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',10)));
		playerHand.add((new Card('D',10)));
		playerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 1,  "Dealer should win Straight Flush beats Three of a Kind");
	}

	@Test
	void compareHandsStraightVStraightFlush() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('D',9)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',5)));
		playerHand.add((new Card('D',6)));
		playerHand.add((new Card('S',7)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 1,  "Dealer should win Straight Flush beats Straight");
	}

	@Test
	void compareHandsFlushVStraightFlush() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('D',9)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',5)));
		playerHand.add((new Card('S',10)));
		playerHand.add((new Card('S',7)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 1,  "Dealer should win Straight Flush beats Flush");
	}

	@Test
	void compareHandsPairVStraightFlush() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('D',9)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',10)));
		playerHand.add((new Card('D',10)));
		playerHand.add((new Card('S',7)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 1,  "Dealer should win Straight Flush beats Pair");
	}

	@Test
	void compareHandsHighCardVThreeKind() {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('D',10)));
		playerHand.add((new Card('S',10)));
		playerHand.add((new Card('D',10)));

		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('S',8)));
		dealerHand.add((new Card('D',4)));
		dealerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 2,  "Player should win Three of a Kind beats High Card");
	}

	@Test
	void compareHandsStraightVThreeKind() {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('D',10)));
		playerHand.add((new Card('S',10)));
		playerHand.add((new Card('D',10)));

		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('S',8)));
		dealerHand.add((new Card('D',9)));
		dealerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 2,  "Player should win Three of a Kind beats Straight");
	}

	@Test
	void compareHandsFlushVThreeKind() {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('D',10)));
		playerHand.add((new Card('S',10)));
		playerHand.add((new Card('D',10)));

		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('S',8)));
		dealerHand.add((new Card('S',3)));
		dealerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 2,  "Player should win Three of a Kind beats Flush");
	}

	@Test
	void compareHandsPairVThreeKind() {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('D',10)));
		playerHand.add((new Card('S',10)));
		playerHand.add((new Card('D',10)));

		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('S',9)));
		dealerHand.add((new Card('D',9)));
		dealerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 2,  "Player should win Three of a Kind beats Pair");
	}

	@Test
	void compareHandsHighCardVStraight() {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('D',2)));
		playerHand.add((new Card('S',3)));
		playerHand.add((new Card('D',4)));

		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('S',8)));
		dealerHand.add((new Card('D',4)));
		dealerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 2,  "Player should win Straight beats High Card");
	}

	@Test
	void compareHandsFlushVStraight() {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('D',2)));
		playerHand.add((new Card('S',3)));
		playerHand.add((new Card('D',4)));

		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('S',8)));
		dealerHand.add((new Card('S',4)));
		dealerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 2,  "Player should win Straight beats Flush");
	}

	@Test
	void compareHandsPairVStraight() {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('D',2)));
		playerHand.add((new Card('S',3)));
		playerHand.add((new Card('D',4)));

		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('S',8)));
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 2,  "Player should win Straight beats Pair");
	}

	@Test
	void compareHandsHighCardVFlush() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('D',7)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',8)));
		playerHand.add((new Card('D',4)));
		playerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 1,  "Dealer should win Flush beats High Card");
	}

	@Test
	void compareHandsPairVFlush() {
		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('D',8)));
		dealerHand.add((new Card('D',7)));
		dealerHand.add((new Card('D',10)));

		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('S',8)));
		playerHand.add((new Card('D',10)));
		playerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 1,  "Dealer should win Flush beats Pair");
	}

	@Test
	void compareHandsHighCardVPair() {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add((new Card('D',2)));
		playerHand.add((new Card('S',3)));
		playerHand.add((new Card('D',3)));

		ArrayList<Card> dealerHand = new ArrayList<Card>();
		dealerHand.add((new Card('S',8)));
		dealerHand.add((new Card('D',4)));
		dealerHand.add((new Card('S',10)));
		int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
		assertEquals(result, 2,  "Player should win Pair beats High Card");
	}

}

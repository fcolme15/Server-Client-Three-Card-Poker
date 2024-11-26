import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThreeCardLogic {
    public static int evalHand(ArrayList<Card> hand)  {
        Card c1 = hand.get(0);
        Card c2 = hand.get(1);
        Card c3 = hand.get(2);
        int[] values = {c1.getValue(), c2.getValue(), c3.getValue()};
        boolean flush = false;
        boolean straight = false;

        //Check for pairs
        if(values[0] == values[1] || values[0] == values[2] || values[1] == values[2]){
            //Checking for three of a kind
            if (values[0] == values[1] && values[0] == values[2]){
                return 2;
            }
            return 5;
        }

        //Checking if it's a flush
        if (c1.getSuit() == c2.getSuit() && c1.getSuit() == c3.getSuit()){
            flush = true;
        }

        //Sort values to make checking for a straight easier
        Arrays.sort(values);
        if (values[0] == values[1]-1 && values[0] == values[2]-2){
            straight = true;
        }

        //Check hand for a flush, straight, or both
        if (flush || straight){
            if (flush && straight){
                return 1;
            }
            else if(flush){
                return 4;
            }
            return 3;
        }
        return 0; //Base case
    }

    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int handValue = evalHand(hand);
        switch (handValue){
            case 0: //High card
                return 0;
            case 1: //Straight flush
                return ((bet * 40) + bet);
            case 2: //Three of a kind
                return ((bet * 30) + bet);
            case 3: //Straight
                return ((bet * 6) + bet);
            case 4: //Flush
                return ((bet * 3) + bet);
            case 5: //Pair
                return (bet+bet);
        }
        return -1;
    }

    private static int winner(int dealer, int player){
        if (dealer > player){
            return 2;
        }
        return 1;
    }

    private static int highCard(ArrayList<Card> dealer, ArrayList<Card> player){
        int[] dealerCardValues = {dealer.get(0).getValue(), dealer.get(1).getValue(), dealer.get(2).getValue()};
        int[] playerCardValues = {player.get(0).getValue(), player.get(1).getValue(), player.get(2).getValue()};
        Arrays.sort(dealerCardValues);
        Arrays.sort(playerCardValues);
        if (playerCardValues[2] == dealerCardValues[2]){
            if (dealerCardValues[1] == dealerCardValues[1]){
                if (dealerCardValues[0] == dealerCardValues[0]){
                    return 0;
                }
                return winner(dealerCardValues[0], playerCardValues[0]);
            }
            return winner(dealerCardValues[1], playerCardValues[1]);
        }
        return winner(dealerCardValues[2], playerCardValues[2]);
    }

    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerHandValue = evalHand(dealer);
        int playerHandValue = evalHand(player);
        if (dealerHandValue == playerHandValue){
            int highCardWinner = highCard(dealer, player);
            if (highCardWinner == 1){
                return 1;
            }
            else if (highCardWinner == 2){
                return 2;
            }
            return 0;
        }
        if (dealerHandValue == 0 || playerHandValue == 0){
            if(dealerHandValue > 0){
                return 1;
            }
            else{
                return 2;
            }
        }
        else if (dealerHandValue < playerHandValue){
            return 1;
        }
        return 2;
    }

    //Used to determine whether the dealer has at least a queen or higher
    public static boolean validDealerHand(ArrayList<Card> h) {
        if(evalHand(h) == 0) {
            //put hand values in a list and determine the max within the list
            List<Integer> handVals = Arrays.asList(h.get(0).getValue(), h.get(1).getValue(), h.get(2).getValue());
            if(Collections.max(handVals) < 12) return false; //12 is Queen value
        }
        return true;
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

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

    public static void settlePlayerWinnings(boolean playedHand, Player p, String playerToString, 
                                        Dealer theDealer, Queue<String> chat) 
    {
        //Player played hand
        if(playedHand)
        {
            //only settle ante if dealer's hand is valid
            if(ThreeCardLogic.validDealerHand(theDealer.getDealersHand()))
            {
                int bet = 0;
                bet = p.getAnteBet() + p.getPushedAnte();
                p.setPushedAnte(0);
                switch(ThreeCardLogic.compareHands(theDealer.getDealersHand(), p.getHand())) 
                {
                    case 0: { //push
                        chat.add(playerToString + " pushes against Dealer");
                        p.setPushedAnte(bet);
                        p.setLastestHandWinnings(0);
                        p.setWonLastHand(0);
                        break;
                    }
                    case 1: { //dealer wins
                        chat.add(addDescription(1,p,playerToString, theDealer));
                        chat.add(playerToString + " loses $" + Integer.toString(p.getAnteBet() + p.getAnteBet()));
                        p.setTotalWinnings(p.getTotalWinnings() - bet);
                        p.setLastestHandWinnings(-1*bet);
                        p.setWonLastHand(1);
                        break;
                    }
                    case 2: { //player wins
                        chat.add(addDescription(0,p,playerToString, theDealer));
                        chat.add(playerToString + " wins $" + Integer.toString(p.getAnteBet() + p.getAnteBet()));
                        p.setTotalWinnings(p.getTotalWinnings() + bet);
                        p.setLastestHandWinnings(bet);
                        p.setWonLastHand(2);
                        break;
                    }
                }
            }
            else {chat.add(playerToString + " pushes. Dealer doesn't have at least Queen High");}

            //settle pair plus winnings regardless of dealer's hand
            int ppWinnings = ThreeCardLogic.evalPPWinnings(p.getHand(), p.getPairPlusBet());
            p.setPairPlusWon(ppWinnings);
            if(ppWinnings != 0) 
            {
                int handValue = ThreeCardLogic.evalHand(p.getHand());
                chat.add(playerToString + " wins $" + Integer.toString(ppWinnings) + " from Pair Plus with a " + handToString(handValue));
                p.setTotalWinnings(p.getTotalWinnings() + ppWinnings);
            }
            else 
            {
                chat.add(playerToString + " loses $" + Integer.toString(p.getPairPlusBet()) + " from Pair Plus");
                p.setTotalWinnings(p.getTotalWinnings() - p.getPairPlusBet());
            }
        }
        else
        {
            //otherwise, player one folded and must lose ante + pair plus (if made)
            chat.add(playerToString + " folds and loses $" + Integer.toString(p.getAnteBet() + p.getPairPlusBet()));
            p.setTotalWinnings(p.getTotalWinnings() - p.getAnteBet()); 
            p.setTotalWinnings(p.getTotalWinnings() - p.getPairPlusBet());
        }
    }

    public static String addDescription(int winner, Player p, String playerToString, Dealer theDealer){
        String winnerString, winnerDescription, loserDescription;
        int dealerValue = ThreeCardLogic.evalHand(theDealer.getDealersHand());
        int playerValue = ThreeCardLogic.evalHand(p.getHand());
        if (winner == 1){
            winnerString = "Dealer";
            winnerDescription = handToString(dealerValue);
            loserDescription = handToString(playerValue);
        }
        else {
            winnerString = playerToString;
            winnerDescription = handToString(playerValue);
            loserDescription = handToString(dealerValue);
        }
        if (playerValue == dealerValue){
            return winnerString + " won the game with a High Card";
        }
        return winnerString + " won the game, " + winnerDescription + " beats " + loserDescription;
    }

    public static String handToString(int hand){
        switch(hand){
            case 1:
                return "Straight Flush";
            case 2:
                return "Three of a Kind";
            case 3:
                return "Straight";
            case 4:
                return "Flush";
            case 5:
                return "Pair";
            default:
                return "High Card";
        }
    }
}
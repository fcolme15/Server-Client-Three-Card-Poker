public class Card {
    private char suit;
    private int value;

    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }
    //Testing something

    public char getSuit() {
        return this.suit;
    }

    public int getValue() {
        return this.value;
    }
}

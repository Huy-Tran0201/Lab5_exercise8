/**
 * Chương trình chạy thử Exercise 8 (trên nền Exercise 9).
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("So Card da tao luc dau: " + Card.createdCount());

        Card first = Card.get(Rank.ACE, Suit.SPADES);
        System.out.println("Sau khi lay 1 la (" + first + "): " + Card.createdCount());

        Card again = Card.get(Rank.ACE, Suit.SPADES);
        System.out.println("first == again -> " + (first == again));

        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                Card.get(r, s);
            }
        }
        System.out.println("Sau khi lay du 52 la: " + Card.createdCount());
    }
}
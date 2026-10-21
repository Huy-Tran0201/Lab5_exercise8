/**
 * Chương trình chạy thử Exercise 8 (trên nền Exercise 9).
 */
public class Main {
    public static void main(String[] args) {
        // Lazy: trước khi ai yêu cầu, chưa có flyweight nào được tạo
        System.out.println("So Card da tao luc dau: " + Card.createdCount());

        Card first = Card.get(Rank.ACE, Suit.SPADES);
        System.out.println("Sau khi lay 1 la (" + first + "): " + Card.createdCount());

        // GameModel -> Deck.shuffle() cần đủ 52 lá nên lúc này mới tạo hết
        GameModel a = GameModel.instance();
        GameModel b = GameModel.instance();
        System.out.println("a == b: " + (a == b));
        System.out.println("Sau khi tao GameModel/Deck: " + Card.createdCount());

        // Mọi lần yêu cầu cùng (Rank, Suit) đều trả về CÙNG một đối tượng
        Card again = Card.get(Rank.ACE, Suit.SPADES);
        System.out.println(first + ": first == again -> " + (first == again));

        a.newGame();
        b.newGame();
        System.out.println("So Card da tao sau newGame() x2: " + Card.createdCount() + " (toi da 52)");
    }
}

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Bộ bài chuẩn gồm 52 lá.
 */
public class Deck {
    private final Deque<Card> aCards = new ArrayDeque<>();

    // Tạo bộ bài đầy đủ 52 lá, đã xáo trộn
    public Deck() {
        shuffle();
    }

    // Đưa bộ bài về đủ 52 lá và xáo trộn ngẫu nhiên
    public void shuffle() {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(Card.get(rank, suit)); // lấy flyweight (lazy) thay vì new Card
            }
        }
        Collections.shuffle(cards);
        aCards.clear();
        aCards.addAll(cards);
    }

    // Rút lá bài trên cùng. Điều kiện: bộ bài không rỗng
    public Card draw() {
        assert !isEmpty();
        return aCards.pop();
    }

    public boolean isEmpty() {
        return aCards.isEmpty();
    }

    public int size() {
        return aCards.size();
    }
}

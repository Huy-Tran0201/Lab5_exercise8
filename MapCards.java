import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Lazy flyweight theo lời giải mẫu của codesample.info: Map<Rank, Map<Suit, Card>>
 * dùng IdentityHashMap. Chỉ dùng để so sánh với bản mảng trong Card.get().
 */
public final class MapCards {
    private static final Map<Rank, Map<Suit, Card>> CARDS = new IdentityHashMap<>();

    private MapCards() {
    }

    public static Card get(Rank pRank, Suit pSuit) {
        Map<Suit, Card> innerMap = CARDS.getOrDefault(pRank, new IdentityHashMap<>());
        CARDS.putIfAbsent(pRank, innerMap);
        if (!innerMap.containsKey(pSuit)) {
            innerMap.put(pSuit, new Card(pRank, pSuit));
        }
        return innerMap.get(pSuit);
    }

    static void clear() {
        CARDS.clear();
    }
}

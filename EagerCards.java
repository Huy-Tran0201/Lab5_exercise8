/**
 * Cách tiếp cận EAGER (tạo sẵn): cả 52 flyweight được tạo ngay khi kho được
 * khởi tạo, bất kể có dùng đến hay không. Lớp này chỉ dùng để so sánh với
 * cách LAZY trong Card.get().
 */
public final class EagerCards {

    private static Card[][] cards = build();

    private EagerCards() {
    }

    private static Card[][] build() {
        Card[][] result = new Card[Suit.values().length][Rank.values().length];
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                result[suit.ordinal()][rank.ordinal()] = new Card(rank, suit);
            }
        }
        return result;
    }

    public static Card get(Rank pRank, Suit pSuit) {
        return cards[pSuit.ordinal()][pRank.ordinal()];
    }

    // Chỉ phục vụ benchmark: tạo lại toàn bộ 52 lá (mô phỏng lúc nạp lớp).
    static void rebuild() {
        cards = build();
    }
}

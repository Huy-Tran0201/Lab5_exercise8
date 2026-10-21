/**
 * Một lá bài, bất biến (immutable), gồm hạng (Rank) và chất (Suit).
 *
 * Thiết kế theo Flyweight Design Pattern với cơ chế LAZY: mỗi tổ hợp
 * (Rank, Suit) chỉ có tối đa một đối tượng Card, và đối tượng đó chỉ được
 * tạo ra ở lần đầu tiên có người yêu cầu (Card.get), không tạo sẵn 52 lá.
 */
public final class Card {

    // Kho flyweight: ô [suit][rank] ban đầu là null, được điền khi cần (lazy).
    private static final Card[][] CARDS =
            new Card[Suit.values().length][Rank.values().length];

    private final Rank aRank;
    private final Suit aSuit;

    // Package-private: bên ngoài gói không thể tự "new Card", phải đi qua get().
    // (Lớp EagerCards trong cùng gói dùng constructor này chỉ để so sánh hiệu năng.)
    Card(Rank pRank, Suit pSuit) {
        assert pRank != null && pSuit != null;
        aRank = pRank;
        aSuit = pSuit;
    }

    //Trả về flyweight ứng với (pRank, pSuit); tạo mới nếu chưa có (lazy).
    public static Card get(Rank pRank, Suit pSuit) {
        assert pRank != null && pSuit != null;
        Card card = CARDS[pSuit.ordinal()][pRank.ordinal()];
        if (card == null) {
            card = new Card(pRank, pSuit);
            CARDS[pSuit.ordinal()][pRank.ordinal()] = card;
        }
        return card;
    }

    
    /* Xóa toàn bộ cache flyweight.
     * Chỉ phục vụ benchmark: xóa kho để mô phỏng "chưa có flyweight nào".
     */
    static void clearCache() {
        for (Card[] row : CARDS) {
            java.util.Arrays.fill(row, null);
        }
    }

    // Số flyweight đã được tạo
    static int createdCount() {
        int n = 0;
        for (Card[] row : CARDS) {
            for (Card c : row) {
                if (c != null) {
                    n++;
                }
            }
        }
        return n;
    }

    public Rank getRank() {
        return aRank;
    }

    public Suit getSuit() {
        return aSuit;
    }

    @Override
    public String toString() {
        return aRank + " of " + aSuit;
    }
}

/**
 * Mô hình trò chơi, được thiết kế theo Singleton Design Pattern.
 * Chỉ có một thể hiện duy nhất, lấy ra bằng GameModel.instance().
 */
public final class GameModel {

    // Thể hiện duy nhất của lớp, được tạo khi lớp được nạp.
    private static final GameModel INSTANCE = new GameModel();

    // Biến thể hiện duy nhất của lớp: bộ bài.
    private final Deck aDeck = new Deck();

    // Constructor private để không thể tạo thêm đối tượng từ bên ngoài
    private GameModel() {
    }

    // Trả về thể hiện duy nhất của GameModel
    public static GameModel instance() {
        return INSTANCE;
    }

    // Bắt đầu ván mới: khởi tạo lại bộ bài (đủ 52 lá và xáo trộn)
    public void newGame() {
        aDeck.shuffle();
    }
}

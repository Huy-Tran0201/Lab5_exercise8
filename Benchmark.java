/**
 * So sánh thời gian lấy đủ 52 lá bài giữa EAGER và LAZY, dùng System.currentTimeMillis().
 *
 * Cách dùng:
 *   java Benchmark single eager|lazy   -> 1 lần đo trong JVM mới (gồm cả khởi tạo)
 *   java Benchmark repeat [N]          -> lặp N lần (mặc định 200000) để khuếch đại
 */
public class Benchmark {

    // Lấy đủ 52 lá, trả về checksum để JIT không loại bỏ vòng lặp.
    static int obtainAllEager() {
        int h = 0;
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                h += EagerCards.get(r, s).getRank().ordinal();
            }
        }
        return h;
    }

    static int obtainAllLazy() {
        int h = 0;
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                h += Card.get(r, s).getRank().ordinal();
            }
        }
        return h;
    }

    static int obtainAllMap() {
        int h = 0;
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                h += MapCards.get(r, s).getRank().ordinal();
            }
        }
        return h;
    }

    public static void main(String[] args) {
        String mode = args.length > 0 ? args[0] : "repeat";
        if (mode.equals("single")) {
            single(args[1]);
        } else {
            repeat(args.length > 1 ? Integer.parseInt(args[1]) : 200000);
        }
    }

    // 1 lần đo duy nhất; đo cả "lần đầu" (có khởi tạo) và "lần hai" (đã có sẵn).
    static void single(String which) {
        boolean eager = which.equals("eager");
        boolean map = which.equals("map");
        long t0 = System.currentTimeMillis();
        long n0 = System.nanoTime();
        int a = eager ? obtainAllEager() : map ? obtainAllMap() : obtainAllLazy();   // lần 1
        long n1 = System.nanoTime();
        long t1 = System.currentTimeMillis();
        int b = eager ? obtainAllEager() : map ? obtainAllMap() : obtainAllLazy();   // lần 2
        long n2 = System.nanoTime();
        System.out.println(which + ";" + (t1 - t0) + ";" + (n1 - n0) + ";" + (n2 - n1) + ";" + (a + b));
    }

    // Lặp N lần: mỗi vòng reset kho rồi lấy đủ 52 lá -> tổng ms đo được có ý nghĩa.
    static void repeat(int n) {
        int sink = 0;
        // khởi động JIT
        for (int i = 0; i < 20000; i++) {
            MapCards.clear();
            sink += obtainAllMap();
        }
        for (int i = 0; i < 20000; i++) {
            EagerCards.rebuild();
            sink += obtainAllEager();
            Card.clearCache();
            sink += obtainAllLazy();
        }

        // (1) Lấy 52 lá từ trạng thái "chưa có gì"
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            EagerCards.rebuild();          // eager: tạo sẵn 52 lá
            sink += obtainAllEager();
        }
        long eagerMs = System.currentTimeMillis() - t0;

        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            Card.clearCache();             // lazy: kho trống, tạo dần khi lấy
            sink += obtainAllLazy();
        }
        long lazyMs = System.currentTimeMillis() - t0;

        // (2) Chi phí riêng của clearCache/rebuild để trừ ra (phần "reset")
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            Card.clearCache();
            sink += i;
        }
        long clearMs = System.currentTimeMillis() - t0;

        // (3) Khi flyweight đã có sẵn: chỉ là tra mảng
        EagerCards.rebuild();
        obtainAllLazy();
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            sink += obtainAllEager();
        }
        long eagerHitMs = System.currentTimeMillis() - t0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            sink += obtainAllLazy();
        }
        long lazyHitMs = System.currentTimeMillis() - t0;

        // (3) Lazy theo Map (lời giải mẫu)
        long mapMs, mapHitMs;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            MapCards.clear();
            sink += obtainAllMap();
        }
        mapMs = System.currentTimeMillis() - t0;
        t0 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            sink += obtainAllMap();
        }
        mapHitMs = System.currentTimeMillis() - t0;
        System.out.println("Map lazy: tu trong = " + mapMs + " ms, da co san = " + mapHitMs + " ms");

        System.out.println("N = " + n + " lần lấy đủ 52 lá");
        System.out.println("(1) Từ trạng thái trống: eager = " + eagerMs + " ms, lazy = " + lazyMs
                + " ms (lazy gồm clearCache " + clearMs + " ms)");
        System.out.println("(2) Khi đã có sẵn:       eager = " + eagerHitMs + " ms, lazy = " + lazyHitMs + " ms");
        System.out.println("checksum " + sink);
    }
}

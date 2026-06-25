import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

/**
 * Bloom Filter Implementation in Java
 * ======================================
 * A Bloom filter is a space-efficient probabilistic data structure
 * that tests whether an element is a member of a set.
 *
 * Key properties:
 *   - False positives ARE possible  (may say "present" when it isn't)
 *   - False negatives are IMPOSSIBLE (never says "absent" when it is)
 *   - No deletion in standard Bloom filter
 *   - Space usage is O(m) bits regardless of number of elements
 *
 * How it works:
 *   - Maintain a bit array of size m (all zeros initially)
 *   - Use k independent hash functions
 *   - INSERT : hash element k times -> set those k bit positions to 1
 *   - LOOKUP : hash element k times -> if ALL k positions are 1, "probably present"
 *                                      if ANY position is 0, "definitely absent"
 *
 * Optimal parameters:
 *   m = -(n * ln(p)) / (ln(2)^2)     bit array size
 *   k = (m / n) * ln(2)              number of hash functions
 *
 * File contains:
 *   BloomFilter          - standard Bloom filter
 *   CountingBloomFilter  - supports deletion (integer counters)
 */
public class BloomFilter {

    // ── Fields ────────────────────────────────────────────────────────────────

    private final int    n;          // expected number of elements
    private final double p;          // false positive rate
    private final int    m;          // bit array size
    private final int    k;          // number of hash functions
    private final BitSet bits;       // the bit array
    private int          count;      // elements inserted so far

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param expectedElements   n — how many items you plan to insert
     * @param falsePositiveRate  p — acceptable false positive probability (0 < p < 1)
     */
    public BloomFilter(int expectedElements, double falsePositiveRate) {
        if (falsePositiveRate <= 0 || falsePositiveRate >= 1)
            throw new IllegalArgumentException("falsePositiveRate must be between 0 and 1");
        if (expectedElements <= 0)
            throw new IllegalArgumentException("expectedElements must be positive");

        this.n    = expectedElements;
        this.p    = falsePositiveRate;
        this.m    = optimalM(expectedElements, falsePositiveRate);
        this.k    = optimalK(this.m, expectedElements);
        this.bits = new BitSet(this.m);
        this.count = 0;

        System.out.println("BloomFilter initialised:");
        System.out.printf("  expected elements    n = %d%n", n);
        System.out.printf("  false positive rate  p = %.4f%n", p);
        System.out.printf("  bit array size       m = %d bits (%.2f KB)%n",
                m, m / 8.0 / 1024.0);
        System.out.printf("  hash functions       k = %d%n", k);
    }

    // ── Optimal parameter formulas ────────────────────────────────────────────

    /** m = -(n * ln(p)) / (ln 2)^2 */
    private static int optimalM(int n, double p) {
        return (int) Math.ceil(-(n * Math.log(p)) / (Math.log(2) * Math.log(2)));
    }

    /** k = (m / n) * ln(2) */
    private static int optimalK(int m, int n) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    // ── Hash functions ────────────────────────────────────────────────────────

    /**
     * Generate k bit positions for the given item using double hashing.
     * pos_i = (h1 + i * h2) % m
     * Avoids needing k independent hash libraries.
     */
    private int[] hashPositions(String item) {
        byte[] bytes = item.getBytes(StandardCharsets.UTF_8);
        long h1 = hash(bytes, "MD5");
        long h2 = hash(bytes, "SHA-256");

        int[] positions = new int[k];
        for (int i = 0; i < k; i++) {
            positions[i] = (int) (Math.abs(h1 + (long) i * h2) % m);
        }
        return positions;
    }

    private long hash(byte[] data, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest    = md.digest(data);
            long result      = 0L;
            // Take the first 8 bytes and combine into a long
            for (int i = 0; i < Math.min(8, digest.length); i++) {
                result = (result << 8) | (digest[i] & 0xFF);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found: " + algorithm, e);
        }
    }

    // ── Core API ──────────────────────────────────────────────────────────────

    /**
     * Insert item into the Bloom filter.
     * Time  : O(k)
     * Space : O(1)  — always sets exactly k bits
     */
    public void add(String item) {
        for (int pos : hashPositions(item)) {
            bits.set(pos);
        }
        count++;
    }

    /**
     * Check membership.
     * Returns:
     *   false -> item is DEFINITELY NOT in the set
     *   true  -> item is PROBABLY in the set (may be false positive)
     * Time : O(k)
     */
    public boolean contains(String item) {
        for (int pos : hashPositions(item)) {
            if (!bits.get(pos)) return false;
        }
        return true;
    }

    // ── Diagnostics ───────────────────────────────────────────────────────────

    /**
     * Actual FPR given current fill level.
     * FPR = (1 - e^(-k * count / m)) ^ k
     */
    public double currentFalsePositiveRate() {
        double exponent = -(double) k * count / m;
        return Math.pow(1 - Math.exp(exponent), k);
    }

    /** Fraction of bits currently set to 1. */
    public double bitArrayFill() {
        return (double) bits.cardinality() / m;
    }

    public void stats() {
        System.out.println("\nBloomFilter Stats:");
        System.out.printf("  inserted elements    : %d%n",  count);
        System.out.printf("  bit array size       : %d bits%n", m);
        System.out.printf("  hash functions (k)   : %d%n",  k);
        System.out.printf("  bits set (fill %%)    : %.1f%%%n", bitArrayFill() * 100);
        System.out.printf("  current FP rate      : %.4f%%%n", currentFalsePositiveRate() * 100);
        System.out.printf("  theoretical FP rate  : %.2f%%%n", p * 100);
    }

    @Override
    public String toString() {
        return String.format("BloomFilter(n=%d, p=%.4f, m=%d, k=%d, inserted=%d)",
                n, p, m, k, count);
    }


    // ── Counting Bloom Filter (inner static class) ────────────────────────────

    /**
     * Extension that uses integer counters instead of bits.
     * Supports deletion at the cost of higher memory.
     */
    static class CountingBloomFilter {

        private final int   n;
        private final double p;
        private final int   m;
        private final int   k;
        private final int[] counters;
        private int         count;
        private static final int MAX_COUNT = 255;

        public CountingBloomFilter(int expectedElements, double falsePositiveRate) {
            this.n        = expectedElements;
            this.p        = falsePositiveRate;
            this.m        = optimalM(expectedElements, falsePositiveRate);
            this.k        = optimalK(this.m, expectedElements);
            this.counters = new int[m];
            this.count    = 0;

            System.out.println("\nCountingBloomFilter initialised:");
            System.out.printf("  bit array size  m = %d%n", m);
            System.out.printf("  hash functions  k = %d%n", k);
        }

        private int[] hashPositions(String item) {
            byte[] bytes = item.getBytes(StandardCharsets.UTF_8);
            long h1 = hashLong(bytes, "MD5");
            long h2 = hashLong(bytes, "SHA-256");
            int[] positions = new int[k];
            for (int i = 0; i < k; i++) {
                positions[i] = (int) (Math.abs(h1 + (long) i * h2) % m);
            }
            return positions;
        }

        private long hashLong(byte[] data, String algorithm) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                byte[] digest    = md.digest(data);
                long result      = 0L;
                for (int i = 0; i < Math.min(8, digest.length); i++) {
                    result = (result << 8) | (digest[i] & 0xFF);
                }
                return result;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        /** Time : O(k) */
        public void add(String item) {
            for (int pos : hashPositions(item)) {
                if (counters[pos] < MAX_COUNT) counters[pos]++;
            }
            count++;
        }

        /**
         * Remove item from the filter.
         * Returns false if item was definitely never added.
         * Time : O(k)
         */
        public boolean remove(String item) {
            int[] positions = hashPositions(item);
            for (int pos : positions) {
                if (counters[pos] == 0) return false;   // definitely not present
            }
            for (int pos : positions) {
                counters[pos]--;
            }
            count--;
            return true;
        }

        /** Time : O(k) */
        public boolean contains(String item) {
            for (int pos : hashPositions(item)) {
                if (counters[pos] == 0) return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return String.format(
                "CountingBloomFilter(n=%d, p=%.4f, m=%d, k=%d, inserted=%d)",
                n, p, m, k, count);
        }
    }


    // ── Demo / Tests ──────────────────────────────────────────────────────────

    public static void main(String[] args) {

        System.out.println("=".repeat(60));
        System.out.println("  Bloom Filter Demo");
        System.out.println("=".repeat(60));

        // ── Standard Bloom Filter ─────────────────────────────
        System.out.println("\n── Standard Bloom Filter ──");
        BloomFilter bf = new BloomFilter(1000, 0.01);

        String[] presentWords = {"apple", "banana", "cherry", "date",
                                 "elderberry", "fig", "grape", "honeydew",
                                 "kiwi", "lemon"};
        for (String w : presentWords) bf.add(w);
        System.out.printf("%nInserted %d words.%n", presentWords.length);

        // True membership (should all be true)
        System.out.println("\n── Membership checks (inserted words) ──");
        for (String w : presentWords) {
            System.out.printf("  contains('%s') -> %b%n", w, bf.contains(w));
        }

        // Non-member queries
        System.out.println("\n── Membership checks (non-inserted words) ──");
        String[] absentWords = {"mango", "nectarine", "orange",
                                "papaya", "quince", "raspberry", "strawberry"};
        int fpCount = 0;
        for (String w : absentWords) {
            boolean result = bf.contains(w);
            if (result) fpCount++;
            System.out.printf("  contains('%s') -> %b%s%n",
                    w, result, result ? "  <- FALSE POSITIVE" : "");
        }
        System.out.printf("%nFalse positives: %d/%d%n", fpCount, absentWords.length);

        // Stats
        bf.stats();

        // ── Large Scale FP Rate Test ──────────────────────────
        System.out.println("\n── Large Scale FP Rate Test ──");
        BloomFilter bf2 = new BloomFilter(10_000, 0.001);

        for (int i = 0; i < 10_000; i++) bf2.add("user:" + i);

        int falsePositives = 0;
        for (int i = 0; i < 10_000; i++) {
            if (bf2.contains("visitor:" + i)) falsePositives++;
        }

        System.out.println("Inserted       : 10,000 items");
        System.out.println("Tested absent  : 10,000 items");
        System.out.println("False positives: " + falsePositives);
        System.out.printf("Observed FPR   : %.3f%%%n", falsePositives / 10_000.0 * 100);
        System.out.printf("Expected FPR   : %.3f%%%n", 0.001 * 100);

        // ── Counting Bloom Filter ─────────────────────────────
        System.out.println("\n── Counting Bloom Filter (with deletion) ──");
        CountingBloomFilter cbf = new CountingBloomFilter(500, 0.01);

        cbf.add("python");
        cbf.add("java");
        cbf.add("golang");

        System.out.println("\nAfter adding python, java, golang:");
        System.out.println("  contains('python') -> " + cbf.contains("python"));  // true
        System.out.println("  contains('java')   -> " + cbf.contains("java"));    // true
        System.out.println("  contains('rust')   -> " + cbf.contains("rust"));    // false

        System.out.println("\nAfter removing 'java':");
        cbf.remove("java");
        System.out.println("  contains('java')   -> " + cbf.contains("java"));    // false
        System.out.println("  contains('python') -> " + cbf.contains("python"));  // true

        System.out.println("\nTrying to remove non-existent 'rust':");
        System.out.println("  remove('rust')     -> " + cbf.remove("rust"));      // false

        // ── Real-world Use Case ───────────────────────────────
        System.out.println("\n── Real-world Use Case: Username Availability Check ──");
        BloomFilter userFilter = new BloomFilter(1_000_000, 0.001);

        String[] takenUsernames = {"alice", "bob", "charlie", "david", "eve"};
        for (String u : takenUsernames) userFilter.add(u);

        String[] toCheck = {"alice", "frank", "bob", "grace", "heidi"};
        for (String name : toCheck) {
            String status = userFilter.contains(name)
                    ? "UNAVAILABLE or possible collision — check DB"
                    : "AVAILABLE (definitely)";
            System.out.printf("  '%s': %s%n", name, status);
        }
    }
}

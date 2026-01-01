package com.next.engine.data;

import com.next.engine.annotations.internal.Experimental;

/**
 * {@link Experimental}<br/>
 * A small, linear table for storing unique unordered pairs. Efficient for small sets.
 * Please don't use this for anything larger than 64 entries, this is not intended for large datasets.
 */
@Experimental
public class SmallPairTable {

    public final long[] keys    = new long[64];
    public int[] valuesA        = new  int[64];
    public int[] valuesB        = new  int[64];
    private int size            = 0;

    /**
     * Clear the set. Call this whenever you want to reset the set.
     */
    public void clear() {
        size = 0;
    }

    /**
     * Use this instead of the arrays' length, as they might contain false information for the current frame.
     * @return current size of the set
     */
    public int size() {
        return size;
    }

    /**
     * Adds by linearly checking the inserted keys.
     * @param a first value
     * @param b second value
     */
    public void add(int a, int b) {
        long pairKey = pairKey(a, b);

        for (int i = 0; i < size; i++) {
            if (this.keys[i] == pairKey)
                return;
        }

        valuesA[size] = a;
        valuesB[size] = b;
        this.keys[size] = pairKey;
        size++;
    }

    private long pairKey(int aId, int bId) {
        long min = Math.min(aId, bId);
        long max = Math.max(aId, bId);
        return (min << 32) | (max & 0xffffffffL);
    }
}

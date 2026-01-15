package com.next.engine.util;

public class PCG32 implements Rng {
    private final long inc;
    private long state;

    public PCG32(long seed, long seq) {
        this.state = 0;
        this.inc = (seq << 1) | 1;
        nextInt();
        this.state += seed;
        nextInt();
    }

    @Override
    public int nextInt() {
        long oldState = this.state;
        this.state = oldState * 6364136223846793005L + this.inc;
        int xorShifted = (int) (((oldState >>> 18) ^ oldState) >>> 27);
        int rot = (int) (oldState >>> 59);
        return Integer.rotateRight(xorShifted, rot);
    }

    @Override
    public float nextFloat() {
        return (nextInt() >>> 8) * (1f / (1 << 24));
    }
}

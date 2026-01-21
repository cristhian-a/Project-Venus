package com.next.engine.util;

public interface Rng {
    int nextInt();
    float nextFloat();

    default boolean chance(float probability) {
        return nextFloat() < probability;
    }

    default int rollDice(int sides) {
        int bound = Integer.MAX_VALUE - (Integer.MAX_VALUE % sides);
        int r;
        do {
            r = nextInt() & 0x7FFFFFFF;
        } while (r >= bound);
        return (r % sides) + 1;
    }
}

package com.next.engine.data;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class IntObjectMapTest {

    @Test
    public void benchmarkMaps() {

        int N = 8;             // try 16, 32, 64, 128, 256...
        int OPS = 5_000_000;    // number of lookups

        IntObjectMap hashMap = new IntObjectMap(N * 2);
        SmallIntObjectMap linearMap = new SmallIntObjectMap();

        for (int i = 0; i < N; i++) {
            String v = "V" + i;
            hashMap.put(i, v);
            linearMap.put(i, v);
        }

        // ---------------- WARMUP ----------------

        long dummy = 0;

        for (int i = 0; i < 1_000_000; i++) {
            int k = i & (N - 1);
            dummy += hashMap.get(k).hashCode();
            dummy += linearMap.get(k).hashCode();
        }

        // ---------------- HASH MAP TEST ----------------

        var rng = new Random();

        long start = System.nanoTime();

        long sum1 = 0;
        for (int i = 0; i < OPS; i++) {
            int k = rng.nextInt(N - 1);
            Object v = hashMap.get(k);
            sum1 += v.hashCode();
        }

        long end = System.nanoTime();
        long hashTime = end - start;

        // ---------------- LINEAR MAP TEST ----------------

        start = System.nanoTime();

        long sum2 = 0;
        for (int i = 0; i < OPS; i++) {
            int k = rng.nextInt(N - 1);
            Object v = linearMap.get(k);
            sum2 += v.hashCode();
        }

        end = System.nanoTime();
        long linearTime = end - start;

        System.out.println("N = " + N);
        System.out.println("hash   ns/op: " + (hashTime / (double) OPS));
        System.out.println("linear ns/op: " + (linearTime / (double) OPS));
        System.out.println("sink = " + (dummy + sum1 + sum2)); // keep JVM honest

        // My conclusion so far: linear just beats hashing in really tiny sizes (8 or less)
    }
}

package com.next.engine.debug;

import java.util.Arrays;

public final class TimeStat {
    private final long[] samples;
    private int index = 0;
    private int count = 0;

    private long sum = 0;
    private long windowMax = 0;

    public TimeStat(int windowSize) {
        samples = new long[windowSize];
    }

    public void add(long nanos) {
        if (count < samples.length) {
            samples[count++] = nanos;
            sum += nanos;
            windowMax = Math.max(windowMax, nanos);
        } else {
            long old = samples[index];
            sum -= old;
            samples[index] = nanos;
            sum += nanos;
            index = (index + 1) % samples.length;

            if (old == windowMax) {
                windowMax = nanos;
                for (int i = 0; i < count; i++) {
                    windowMax = Math.max(windowMax, samples[i]);
                }
            }
        }
    }

    public long mean() {
        return count == 0 ? 0 : sum / count;
    }

    public long max() {
        return windowMax;
    }

    public long percentile(float p) {
        if (count == 0) return 0;

        long[] copy = Arrays.copyOf(samples, count);
        Arrays.sort(copy);

        int index = (int) Math.ceil(p * (copy.length - 1));
        return copy[index];
    }
}

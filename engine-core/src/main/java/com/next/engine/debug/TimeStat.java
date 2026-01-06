package com.next.engine.debug;

public final class TimeStat {
    private final long[] samples;
    private int index = 0;
    private int count = 0;

    private long sum = 0;
    private long max = 0;

    public TimeStat(int windowSize) {
        samples = new long[windowSize];
    }

    public void add(long nanos) {
        if (count < samples.length) {
            samples[count++] = nanos;
            sum += nanos;
        } else {
            long old = samples[index];
            sum -= old;
            samples[index] = nanos;
            sum += nanos;
            index = (index + 1) % samples.length;
        }
        max = Math.max(max, nanos);
    }

    public long mean() {
        return count == 0 ? 0 : sum / count;
    }

    public long max() {
        return max;
    }
}

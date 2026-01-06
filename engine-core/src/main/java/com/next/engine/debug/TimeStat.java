package com.next.engine.debug;

import java.util.Arrays;

/**
 * Maintains rolling statistics over a fixed-size window of time-based samples.
 * <p>
 * {@code TimeStat} is designed for lightweight runtime performance tracking, such as
 * frame times or subsystem execution durations. It stores the most recent {@code N}
 * samples in a circular buffer and provides common statistical metrics including:
 * <ul>
 *     <li>Mean (average)</li>
 *     <li>Window maximum</li>
 *     <li>Percentiles (e.g. median, p95)</li>
 * </ul>
 * <p>
 * All statistics are computed over the current window only, meaning older samples are
 * automatically discarded as new values are added. This makes the metrics stable,
 * responsive to recent changes, and resistant to startup noise or rare outliers.
 * <p>
 * This class is intended for real-time debugging and profiling scenarios where
 * allocations and complex synchronization should be avoided.
 */
public final class TimeStat {
    private final long[] samples;
    private int index = 0;
    private int count = 0;

    private long sum = 0;
    private long windowMax = 0;

    /**
     * Creates a {@code TimeStat} with a fixed-size rolling window.
     *
     * @param windowSize the maximum number of recent samples to retain.
     *                   Once the window is full, older samples are overwritten
     *                   in a circular fashion.
     */
    public TimeStat(int windowSize) {
        samples = new long[windowSize];
    }

    /**
     * Adds a new sample to the rolling window.
     * <p>
     * If the window is not yet full, the value is appended. Once the window reaches
     * capacity, older samples are overwritten in insertion order using a circular buffer.
     * <p>
     * Internal statistics (sum and window maximum) are updated incrementally to avoid
     * recomputing values on each query.
     *
     * @param nanos the sample value to record, typically a duration in nanoseconds
     */
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

    /**
     * Returns the arithmetic mean of all samples currently stored in the window.
     * <p>
     * The mean is computed as the sum of all values divided by the number of samples.
     *
     * @return the mean value of the current window, or {@code 0} if no samples exist
     */
    public long mean() {
        return count == 0 ? 0 : sum / count;
    }

    /**
     * Returns the maximum value recorded within the current window.
     * <p>
     * The returned value reflects only the active rolling window and does not
     * represent a lifetime maximum.
     *
     * @return the maximum sample value in the current window, or {@code 0} if empty
     */
    public long max() {
        return windowMax;
    }

    /**
     * Returns the value at the given percentile within the current sample window.
     * <p>
     * A percentile represents the value below which a certain percentage of samples fall.
     * For example, passing {@code 0.95f} (95th percentile) returns the frame time such that
     * 95% of recorded samples are faster than or equal to this value, and only 5% are slower.
     * <p>
     * This metric is particularly useful for performance analysis, as it highlights
     * typical worst-case behavior (e.g., frame spikes) without being overly influenced
     * by rare outliers, unlike a global maximum.
     * <p>
     * The percentile is computed by sorting the current samples and selecting the value
     * at the corresponding rank within the window.
     *
     * @param p the percentile to calculate, expressed as a normalized value in the range
     *          {@code [0.0, 1.0]}. For example:
     *          <ul>
     *              <li>{@code 0.0} — minimum value</li>
     *              <li>{@code 0.5} — median (50th percentile)</li>
     *              <li>{@code 0.95} — 95th percentile</li>
     *              <li>{@code 1.0} — maximum value</li>
     *          </ul>
     * @return the value at the specified percentile within the current window of samples,
     *         or {@code 0} if no samples have been recorded.
     */
    public long percentile(float p) {
        if (count == 0) return 0;

        p = Math.clamp(p, 0.0f, 1.0f);

        long[] copy = Arrays.copyOf(samples, count);
        Arrays.sort(copy);

        int index = (int) Math.ceil(p * (copy.length - 1));
        return copy[index];
    }
}

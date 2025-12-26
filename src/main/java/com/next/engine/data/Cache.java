package com.next.engine.data;

import java.util.function.Supplier;

/**
 * This is experimental stuff
 * @param <K>
 * @param <V>
 */
public final class Cache<K, V> {

    private V[] line;
    private K[] keys;
    private int[] usage;
    private int uncached = 0;

    private int uncachingThreshold = 10;

//    CacheLine[] buckets = new CacheLine[256];

    @SuppressWarnings("unchecked")
    public Cache() {
        line = (V[]) new Object[256];
        usage = new int[line.length];
        keys = (K[]) new Object[line.length];
    }

    public V get(K key) {
        int i = getIndex(key);

        usage[i]++;
        return line[i];
    }

    private int getIndex(K key) {
        int h = key.hashCode();
        return h & (line.length - 1);
    }

    public V getOrInsertIfNotCached(K key, Supplier<V> action) {
        int i = getIndex(key);

        if (keys[i] != null && keys[i].equals(key)) {
            return get(key);
        } else if (keys[i] != null) {
            uncached++;
        }

        if (uncached > uncachingThreshold) {
            uncached = 0;
            // TODO resizing
        }

        keys[i] = key;
        line[i] = action.get();
        return line[i];
    }

    // This is from a previous test, do not consider this, please
//    public void put2(K key, V value) {
//        int h = key.hashCode();
//        int i = h & (buckets.length - 1);
//        buckets[i] = new CacheLine();
//        buckets[i].hash = h;
//        buckets[i].data = value;
//    }
//
//    // This is from a previous test, do not consider this, please
//    private static final class CacheLine {
//        int hash;
//        Object key;
//        Object data;
//    }

}

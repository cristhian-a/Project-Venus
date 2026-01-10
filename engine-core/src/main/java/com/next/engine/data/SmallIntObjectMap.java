package com.next.engine.data;

import com.next.engine.annotations.internal.Experimental;

import java.util.Arrays;

/**
 * {@link Experimental} <br/>
 * A primitive integer map optimized for no more than 8 entries. It scans for values linearly through the internal array
 * of keys. This class is really only useful for tiny sizes, for anything more than 8 entries check {@link IntObjectMap}.
 */
@Experimental
public class SmallIntObjectMap {

    private int[] keys;
    private Object[] values;

    private int capacity = 8;
    private int count;

    public SmallIntObjectMap() {
        keys = new int[capacity];
        values = new Object[capacity];
    }

    public Object get(int key) {
        for (int i = 0; i < count; i++) {
            if (key == keys[i]) return values[i];
        }

        return null;
    }

    public void put(int key, Object value) {
        ensureCapacity();
        keys[count] = key;
        values[count] = value;
        count++;
    }

    public int size() {
        return count;
    }

    private void ensureCapacity() {
        if (count >= capacity) {
            capacity *= 2;
            keys = Arrays.copyOf(keys, capacity);
            values = Arrays.copyOf(values, capacity);
        }
    }
}

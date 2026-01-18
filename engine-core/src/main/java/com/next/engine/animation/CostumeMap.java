package com.next.engine.animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Uses a {@link HashMap} to store {@link Costume} instances.
 * @param <K> The type of keys maintained by this map.
 */
public final class CostumeMap<K> implements Wardrobe<K> {
    private final Map<K, Costume> costumes = new HashMap<>();

    public void add(K key, Costume costume) {
        costumes.put(key, costume);
    }

    public Costume get(K key) {
        return costumes.get(key);
    }
}

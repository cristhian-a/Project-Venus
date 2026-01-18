package com.next.engine.animation;

import java.util.EnumMap;
import java.util.Map;

/**
 * Uses an {@link EnumMap} to store costumes.
 * @param <E> The type of enum values maintained by this map.
 */
public final class EnumWardrobe<E extends Enum<E>> implements Wardrobe<E> {

    private final Map<E, Costume> costumes;

    public EnumWardrobe(Class<E> enumClass) {
        costumes = new EnumMap<>(enumClass);
    }

    @Override
    public Costume get(E key) {
        return costumes.get(key);
    }

    @Override
    public void add(E key, Costume costume) {
        costumes.put(key, costume);
    }
}

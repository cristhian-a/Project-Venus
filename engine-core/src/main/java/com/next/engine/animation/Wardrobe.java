package com.next.engine.animation;

public interface Wardrobe<K> {
    Costume get(K key);
    void add(K key, Costume costume);
}

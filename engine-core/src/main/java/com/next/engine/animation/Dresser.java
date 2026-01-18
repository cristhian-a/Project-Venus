package com.next.engine.animation;

public final class Dresser<K> implements Costume, Animated {
    private final Wardrobe<K> wardrobe;
    private Costume current;

    public Dresser(Wardrobe<K> wardrobe) {
        this.wardrobe = wardrobe;
    }

    public void wear(K key) {
        Costume next = wardrobe.get(key);
        if (next != null && next != current) {
            current = next;
            reset();
        }
    }

    public Costume current() {
        return current;
    }

    @Override
    public void reset() {
        if (current instanceof Animated animated) {
            animated.reset();
        }
    }

    @Override
    public void update(double delta) {
        if (current instanceof Animated animated) {
            animated.update(delta);
        }
    }

    @Override
    public int texture() {
        return current.texture();
    }
}

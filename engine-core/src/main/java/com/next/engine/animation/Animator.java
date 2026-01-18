package com.next.engine.animation;

public class Animator {
    private Animation current;
    private double accumulator;
    private int index;

    public void set(Animation animation) {
        if (current != animation) {
            current = animation;
            accumulator = 0;
            index = 0;
        }
    }

    public int update(double delta) {
        if (!current.loop || current.frameDuration <= 0) return current.frames[index];

        accumulator += delta;
        if (accumulator >= current.frameDuration) {
            int advances = (int) (accumulator / current.frameDuration);
            accumulator -= advances * current.frameDuration;
            index = (index + advances) % current.frames.length;
        }

        return current.frames[index];
    }
}

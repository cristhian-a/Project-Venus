package com.next.engine.animation;

public class Animator {
    private Animation current;
    private int frame;
    private int index;

    public void set(Animation animation) {
        if (current != animation) {
            current = animation;
            frame = 0;
            index = 0;
        }
    }

    public int update() {
        if (!current.loop) return current.frames[index];

        frame++;
        if (frame >= current.frameRate) {
            frame = 0;
            index = (index + 1) % current.frames.length;
        }
        return current.frames[index];
    }
}

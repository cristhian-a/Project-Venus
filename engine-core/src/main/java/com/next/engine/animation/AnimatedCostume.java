package com.next.engine.animation;

public final class AnimatedCostume implements Costume, Animated {
    Animation animation;
    private int frame;
    private int index;

    public AnimatedCostume(Animation animation) {
        this.animation = animation;
    }

    @Override
    public int texture() {
        return animation.frames[index];
    }

    @Override
    public void reset() {
        frame = 0;
        index = 0;
    }

    @Override
    public void update(double delta) {
        if (!animation.loop) return;

        frame++;
        if (frame >= animation.frameRate) {
            frame = 0;
            index = (index + 1) % animation.frames.length;
        }
    }
}

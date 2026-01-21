package com.next.engine.animation;

public final class AnimatedCostume implements Costume, Animated {
    Animation animation;
    private int index;
    private double accumulator;

    /**
     *
     * @param animation
     * @throws IllegalArgumentException if animation has no frames
     */
    public AnimatedCostume(Animation animation) {
        if (animation.frames.length == 0)
            throw new IllegalArgumentException("Animation must have at least one frame");

        this.animation = animation;
    }

    @Override
    public int texture() {
        return animation.frames[index];
    }

    @Override
    public void reset() {
        accumulator = 0;
        index = 0;
    }

    @Override
    public void update(double delta) {
        if (!animation.loop || animation.frameDuration <= 0) return;

        accumulator += delta;
        if (accumulator >= animation.frameDuration) {
            int advances = (int) (accumulator / animation.frameDuration);
            accumulator -= advances * animation.frameDuration;
            index = (index + advances) % animation.frames.length;
        }
    }
}

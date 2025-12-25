package com.next.engine.physics;

import com.next.engine.model.Actor;

/**
 * Interface for objects that want to be subject to the physics engine. Implementing its methods will make it able to be
 * considered during {@link Physics} apply.
 */
public interface Body {
    float getX();
    float getY();
    float getZ();
    CollisionBox getCollisionBox();
    CollisionType getCollisionType();
    void setPosition(float x, float y);
    CollisionResult onCollision(Actor other);

    /**
     * By default, should add dx to the world x of the object.
     * @param dx distance to move
     * @param delta delta time
     */
    default void moveX(float dx, double delta) {
        if (dx == 0) return;
        dx += getX();
        setPosition(dx, getY());
    }

    /**
     * By default, should add dy to the world y of the object.
     * @param dy distance to move
     * @param delta delta time
     */
    default void moveY(float dy, double delta) {
        if (dy == 0) return;
        dy += getY();
        setPosition(getX(), dy);
    }

}

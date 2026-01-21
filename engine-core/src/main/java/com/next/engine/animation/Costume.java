package com.next.engine.animation;

/**
 * Represents a costume that can provide a texture to be used in an animation or graphical system.
 * Implementations of this interface can define how the texture is retrieved or managed.
 *
 * This interface is primarily used in animation systems to handle different types of costumes,
 * such as static or animated ones. It allows seamless integration into wardrobe-like structures
 * for switching and managing costumes dynamically.
 *
 * @see StaticCostume
 * @see AnimatedCostume
 * @see Wardrobe
 * @see Dresser
 */
public interface Costume {
    int texture();
}

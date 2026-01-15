package com.next.engine.event;

/**
 * Represents a general contract for events that occur within the game.
 *
 * This interface is designed to be implemented by various types of events that
 * dictate specific actions or behaviors in the game when fired. Implementations
 * of this interface can be used in conjunction with event dispatch systems to
 * trigger appropriate handlers.
 *
 * Game events offer flexibility in design, allowing developers to define and manage
 * multiple scenarios such as starting the game, pausing the game, reacting to player
 * actions, or handling specific game rules.
 */
public interface GameEvent {
}

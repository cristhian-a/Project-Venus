package com.next.world;

import com.next.model.Actor;
import com.next.model.Player;

/**
 * A {@code Scene} holds all the runtime entities relevant to the current game scene.
 */
public class Scene {
    public final World world;
    public final Player player;
    public final Actor[] actors;

    public Scene(World world, Player player, Actor[] actors) {
        this.world = world;
        this.player = player;
        this.actors = actors;
    }
}

package com.next.world;

import com.next.core.data.Mailbox;
import com.next.core.model.Actor;
import com.next.model.Player;

import java.util.Arrays;

/**
 * A {@code Scene} holds all the runtime entities relevant to the current game scene.
 */
public class Scene {
    public final World world;
    public final Player player;
    public Actor[] actors;

    public Scene(World world, Player player, Actor[] actors) {
        this.world = world;
        this.player = player;
        this.actors = actors;
    }

    public void dismissDisposedActors() {
        for (int i = 0; i < actors.length; i++) {
            Actor actor = actors[i];
            if (actor.isDisposed()) {
                actors[i] = actors[actors.length - 1];
                actors[actors.length - 1] = null;
                actors = Arrays.copyOf(actors, actors.length - 1);
            }
        }
    }

}

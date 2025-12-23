package com.next.world;

import com.next.engine.data.Mailbox;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Actor;
import com.next.model.Player;
import lombok.Getter;

import java.util.Arrays;

/**
 * A {@code Scene} holds all the runtime entities relevant to the current game scene.
 */
public class Scene {
    public final World world;
    public final Player player;

    @Getter private Actor[] actors;
    private int activeCount;

    public int size() { return activeCount; }

    public Scene(World world, Player player, Actor[] actors) {
        this.world = world;
        this.player = player;
        this.actors = actors;
        activeCount = actors.length;
    }

    public void addActor(Actor actor) {
        if (activeCount >= actors.length) {
            actors = Arrays.copyOf(actors, actors.length * 2);
        }
        actors[activeCount++] = actor;
    }

    public void update(double delta, Mailbox mailbox) {
        for (int i = 0; i < activeCount; i++) {
            actors[i].update(delta, mailbox);
        }
    }

    public void submitRender(RenderQueue queue) {
        for (int i = 0; i < activeCount; i++) {
            actors[i].submitRender(queue);
        }
    }

    public void dismissDisposedActors() {
        for (int i = 0; i < activeCount; i++) {
            if (actors[i].isDisposed()) {
                actors[i].onDispose();

                actors[i] = actors[activeCount - 1];
                actors[activeCount - 1] = null;
                activeCount--;
                i--;
            }
        }
    }

}

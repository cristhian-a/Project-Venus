package com.next.world;

import com.next.engine.data.Mailbox;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Actor;
import com.next.engine.model.Entity;
import com.next.engine.physics.Body;
import com.next.engine.system.Debugger;
import com.next.model.Player;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * A {@code Scene} holds all the runtime entities relevant to the current game scene.
 */
public class Scene {
    public final World world;
    public final Player player;

    @Getter private Entity[] entities;  // everyone
    private int entityCount;

    @Getter private Actor[] actors; // every subject of update + rendering
    private int actorCount;

    @Getter private Body[] bodies;  // every subject of physics
    private int bodyCount;

    private int nextId = 1;

    public Scene(World world, Player player) {
        this.world = world;
        this.player = player;

        this.entities = new Entity[16];
        this.actors = new Actor[16];
        this.bodies = new Body[16];
    }

    public void addAll(Entity[] entities) {
        for (Entity entity : entities) {
            add(entity);
        }
    }

    public void add(Entity entity) {
        entity.setId(nextId++);

        if (entityCount >= entities.length) {
            entities = Arrays.copyOf(entities, entities.length * 2);
        }

        entities[entityCount++] = entity;

        if (entity instanceof Body body) {
            if (bodyCount >= bodies.length) {
                bodies = Arrays.copyOf(bodies, bodies.length * 2);
            }

            bodies[bodyCount++] = body;
        }

        if (entity instanceof Actor actor) {
            if (actorCount >= actors.length) {
                actors = Arrays.copyOf(actors, actors.length * 2);
            }

            actors[actorCount++] = actor;
        }
    }

    public Body findBodyById(int id) {
        for (Body b : bodies) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public void update(double delta, Mailbox mailbox) {
        for (int i = 0; i < actorCount; i++) {
            actors[i].update(delta, mailbox);
        }
    }

    public void submitRender(RenderQueue queue) {
        for (int i = 0; i < actorCount; i++) {
            actors[i].submitRender(queue);
        }
    }

    /**
     * Destroys all actors that have been marked as disposed.
     */
    public void dismissDisposed() {
        for (int i = 0; i < entityCount; i++) {
            if (entities[i].isDisposed()) {
                entities[i].onDispose();

                entities[i] = entities[entityCount - 1];
                entities[entityCount - 1] = null;
                entityCount--;
                i--;
            }
        }

        for (int i = 0; i < actorCount; i++) {
            if (actors[i].isDisposed()) {

                actors[i] = actors[actorCount - 1];
                actors[actorCount - 1] = null;
                actorCount--;
                i--;
            }
        }

        for (int i = 0; i < bodyCount; i++) {
            Entity e = (Entity) bodies[i];
            if (e.isDisposed()) {
                bodies[i] = bodies[bodyCount - 1];
                bodies[bodyCount - 1] = null;
                bodyCount--;
                i--;
            }
        }
    }

    public void forEachBody(Consumer<Body> consumer) {
        for (int i = 0; i < bodyCount; i++) {
            Debugger.publish("HITBOX" + bodies[i].getId(), bodies[i].getCollisionBox());
            consumer.accept(bodies[i]);
        }
    }

}

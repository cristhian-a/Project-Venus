package com.next.world;

import com.next.engine.data.Mailbox;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.*;
import com.next.engine.physics.Body;
import com.next.engine.system.Debugger;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

/**
 * A {@code Scene} holds all the runtime entities relevant to the current scene.
 */
public class Scene {
    public final World world;
    public Camera camera;

    @Getter private Entity[] entities;  // everyone
    private int entityCount;

    @Getter private Actor[] actors; // every subject of update + rendering
    private int actorCount;

    @Getter private Body[] bodies;  // every subject of physics
    private int bodyCount;

    @Getter private Light[] lights;
    private int lightCount;

    @Getter private Sensor[] sensors;
    @Getter private int sensorCount;

    private Entity[] entitiesById;
    private int nextId = 0;

    public Scene(World world) {
        this.world = world;

        this.entities = new Entity[16];
        this.actors = new Actor[16];
        this.bodies = new Body[16];
        this.lights = new Light[16];
        this.sensors = new Sensor[16];

        this.entitiesById = new Entity[16];
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

        if (nextId >= entitiesById.length) {
            entitiesById = Arrays.copyOf(entitiesById, entitiesById.length * 2);
        }
        entitiesById[entity.getId()] = entity;

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

        if (entity instanceof Light light) {
            if (lightCount >= lights.length) {
                lights = Arrays.copyOf(lights, lights.length * 2);
            }

            lights[lightCount++] = light;
        }

        if (entity instanceof Sensor sensor) {
            if (sensorCount >= sensors.length) {
                sensors = Arrays.copyOf(sensors, sensors.length * 2);
            }

            sensors[sensorCount++] = sensor;
        }
    }

    public Body findBodyById(int id) {
        for (Body b : bodies) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public Entity getEntity(int id) {
        return entitiesById[id];
    }

    public void update(double delta, Mailbox mailbox) {
        for (int i = 0; i < actorCount; i++) {
            actors[i].update(delta, mailbox);
        }

        for (int i = 0; i < sensorCount; i++) {
            sensors[i].update(delta);
        }
    }

    public void submitRender(RenderQueue queue) {
        // sorting by Y before submitting; Probably not the best, but fine for now
        Arrays.sort(actors, 0, actorCount, Comparator.comparingDouble(Actor::getWorldY));

        for (int i = 0; i < actorCount; i++) {
            actors[i].submitRender(queue);
        }

        for (int i = 0; i < lightCount; i++) {
            queue.punchLight(
                    lights[i].getWorldX(),
                    lights[i].getWorldY(),
                    0f,
                    lights[i].getRadius(),
                    lights[i].getIntensity(),
                    lights[i].getTextureId()
            );
        }
    }

    /**
     * Destroys all actors that have been marked as disposed.
     */
    public void dismissDisposed() {
        for (int i = 0; i < entityCount; i++) {
            if (entities[i].isDisposed()) {
                entities[i].onDispose();

                entitiesById[entities[i].getId()] = null;

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

        for (int i = 0; i < sensorCount; i++) {
            if (sensors[i].isDisposed()) {
                sensors[i] = sensors[sensorCount - 1];
                sensors[sensorCount - 1] = null;
                sensorCount--;
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

    public void forEachSensor(Consumer<Sensor> consumer) {
        for (int i = 0; i < sensorCount; i++) {
            consumer.accept(sensors[i]);
        }
    }

}

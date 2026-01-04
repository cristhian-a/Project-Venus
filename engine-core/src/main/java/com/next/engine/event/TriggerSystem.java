package com.next.engine.event;

import com.next.engine.data.SmallPairTable;
import com.next.engine.model.Sensor;
import com.next.engine.physics.*;
import com.next.world.Scene;

@Deprecated(forRemoval = true)
public class TriggerSystem implements SpatialGridHandler {

    private SmallPairTable current = new SmallPairTable();
    private SmallPairTable previous = new SmallPairTable();

    private void beginFrame() {
        var temp = previous;
        previous = current;
        current = temp;

        current.clear();
    }

    /**
     * Queries neighbors for each sensor in the scene and collect intersections. Be aware that this method is not
     * thread-safe, so call it only in the main thread.
     * @param scene a {@link Scene} containing all the sensors to be collected.
     * @param grid a {@link SpatialGrid} containing spatial information about all the entities in the scene.
     */
    public void collect(Scene scene, SpatialGrid grid) {
        beginFrame();

        for (int i = 0; i < scene.getSensorCount(); i++) {
            Sensor sensor = scene.getSensors()[i];
            processingSensorBox = sensor.getCollisionBox().getBounds();
            grid.queryNeighbors(sensor, this);
        }
    }

    // aux var only between collect and handleNeighbor
    // supposedly this helps by reducing the number of virtual calls in the JVM
    private AABB processingSensorBox;

    @Override
    public void handleNeighbor(Body self, Body neighbor) {
        Sensor sensor = (Sensor) self;
        if (neighbor.getCollisionBox().getBounds().intersects(processingSensorBox)) {
            current.add(sensor.getId(), neighbor.getId());
        }
    }

    /**
     * Processes collisions and collects generated events.
     * @param scene a {@link Scene} containing all the entities in the scene.
     * @param collector a {@link EventCollector} to collect all the generated events.
     */
    public void compute(Scene scene, EventCollector collector) {
        for (int i = 0; i < current.size(); i++) {
            boolean found = false;
            for (int j = 0; j < previous.size(); j++) {
                if (current.keys[i] == previous.keys[j]) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                fireEnter(current.valuesA[i], current.valuesB[i], scene, collector);
            }

            fireCollision(current.valuesA[i], current.valuesB[i], scene, collector);
        }

        for (int i = 0; i < previous.size(); i++) {
            boolean found = false;
            for (int j = 0; j < current.size(); j++) {
                if (previous.keys[i] == current.keys[j]) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                fireExit(previous.valuesA[i], previous.valuesB[i], scene, collector);
            }
        }
    }

    private void fireEnter(int sensor, int agent, Scene scene, EventCollector collector) {
        Sensor self = (Sensor) scene.getEntity(sensor);
        Body other = (Body) scene.getEntity(agent);
        if (self != null && other != null) {
            self.onEnter(other, collector);
        }
    }

    private void fireExit(int sensor, int agent, Scene scene, EventCollector collector) {
        Sensor self = (Sensor) scene.getEntity(sensor);
        Body other = (Body) scene.getEntity(agent);
        if (self != null && other != null) {
            self.onExit(other, collector);
        }
    }

    private void fireCollision(int sensor, int agent, Scene scene, EventCollector collector) {
        Sensor self = (Sensor) scene.getEntity(sensor);
        Body other = (Body) scene.getEntity(agent);
        if (self != null && other != null) {
            self.onCollision(other, collector);
        }
    }
}

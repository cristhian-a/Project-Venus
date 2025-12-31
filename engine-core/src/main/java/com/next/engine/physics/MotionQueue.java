package com.next.engine.physics;

import java.util.Arrays;

/**
 * A queue of movement requests to be processed by the physics engine.
 */
public final class MotionQueue {

    private final int DEFAULT_BUFFER = 16;
    private int BUFFER_SIZE = DEFAULT_BUFFER;
    private int count = 0;

    public int[] actorIds = new int[DEFAULT_BUFFER];
    public float[] deltaX = new float[DEFAULT_BUFFER];
    public float[] deltaY = new float[DEFAULT_BUFFER];
    public float[] deltaZ = new float[DEFAULT_BUFFER];

    public void clear() {
        count = 0;
    }

    public int size() {
        return count;
    }

    private void ensureCapacity() {
        if (count >= BUFFER_SIZE) {
            BUFFER_SIZE *= 2;
            actorIds = Arrays.copyOf(actorIds, BUFFER_SIZE);
            deltaX = Arrays.copyOf(deltaX, BUFFER_SIZE);
            deltaY = Arrays.copyOf(deltaY, BUFFER_SIZE);
            deltaZ = Arrays.copyOf(deltaZ, BUFFER_SIZE);
        }
    }

    public void submit(int actorId, float deltaX, float deltaY, float deltaZ) {
        ensureCapacity();
        this.actorIds[count] = actorId;
        this.deltaX[count] = deltaX;
        this.deltaY[count] = deltaY;
        this.deltaZ[count] = deltaZ;
        count++;
    }
}

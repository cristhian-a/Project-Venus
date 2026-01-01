package com.next.engine.physics;

/**
 * Enables a class to be visited by the spatial grid. (see {@link SpatialGrid})
 */
public interface SpatialGridHandler {
    void handleNeighbor(Body self, Body neighbor);
}

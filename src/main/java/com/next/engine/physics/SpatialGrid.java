package com.next.engine.physics;

import com.next.engine.model.AABB;
import com.next.engine.model.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SpatialGrid {
    private final int cellSize;
    private final List<Body>[][] cells;
    private final int cols, rows;

    private int queryCounter = 0;

    @SuppressWarnings("unchecked")
    public SpatialGrid(int width, int height, int cellSize) {
        this.cellSize = cellSize;
        this.cols = (int) Math.ceil((double) width / cellSize);
        this.rows = (int) Math.ceil((double) height / cellSize);
        this.cells = new ArrayList[cols][rows];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[c][r] = new ArrayList<>(16);
            }
        }
    }

    public void clear() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[c][r].clear();
            }
        }
    }

    public void insert(Body body) {
        AABB box = body.getCollisionBox().getBounds();
        int left    = Math.max(0, (int) (box.x / cellSize));
        int right   = Math.min(cols - 1, (int) ((box.x + box.width) / cellSize));
        int top     = Math.max(0, (int) (box.y / cellSize));
        int bottom  = Math.min(rows - 1, (int) ((box.y + box.height) / cellSize));

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                cells[col][row].add(body);
            }
        }
    }

    public void forEachNearby(Body body, Consumer<Body> action) {
        queryCounter++;

        AABB box = body.getCollisionBox().getBounds();
        int left    = Math.max(0, (int) (box.x / cellSize));
        int right   = Math.min(cols - 1, (int) ((box.x + box.width) / cellSize));
        int top     = Math.max(0, (int) (box.y / cellSize));
        int bottom  = Math.min(rows - 1, (int) ((box.y + box.height) / cellSize));

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                List<Body> cell = cells[col][row];
                for (Body other : cell) {
                    if (other != body && other.getLastQueryId() != queryCounter) {
                        other.setLastQueryId(queryCounter);
                        action.accept(other);
                    }
                }
            }
        }

    }

}

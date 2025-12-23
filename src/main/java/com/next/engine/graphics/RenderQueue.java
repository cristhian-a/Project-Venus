package com.next.engine.graphics;

import com.next.engine.model.AABB;
import com.next.engine.physics.CollisionBox;

import java.util.Arrays;

/**
 * A queue of requests to be submitted to the renderer pipeline.
 */
public final class RenderQueue {

    private final LayerBucket[] buckets;

    public RenderQueue() {
        int layersCount = Layer.values().length;
        buckets = new LayerBucket[layersCount];
        for (int i = 0; i < layersCount; i++) {
            buckets[i] = new LayerBucket();
        }
    }

    public LayerBucket getBucket(Layer layer) {
        return buckets[layer.ordinal()];
    }

    public void clear() {
        for (LayerBucket bucket : buckets) {
            bucket.clear();
        }
    }

    public void submit(Layer layer, int x, int y, int spriteId) {
        buckets[layer.ordinal()].sprites.add(x, y, spriteId);
    }

    public void submit(Layer layer, CollisionBox box) {
        if (box == null) return;
        AABB bounds = box.getBounds();
        buckets[layer.ordinal()].collisions.add(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void submit(Layer layer, String message, String font, String color, int x, int y, RenderPosition pos, int frames) {
        buckets[layer.ordinal()].texts.add(message, font, color, x, y, frames, pos);
    }

    public void submit(Layer layer, RenderType type) {
        buckets[layer.ordinal()].overlays.add(0, 0);
    }

    public static final class LayerBucket {
        public final TextTable texts = new TextTable(32);
        public final SpriteTable sprites = new SpriteTable(32);
        public final OverlayTable overlays = new OverlayTable(1);
        public final CollisionTable collisions = new CollisionTable(32);

        public void clear() {
            texts.clear();
            sprites.clear();
            overlays.clear();
            collisions.clear();
        }
    }

    public static final class SpriteTable {
        public int[] x, y, spriteId;
        public int count = 0;
        private int capacity;

        public SpriteTable(int capacity) {
            this.capacity = capacity;
            x = new int[capacity];
            y = new int[capacity];
            spriteId = new int[capacity];
        }

        private void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                spriteId = Arrays.copyOf(spriteId, capacity);
            }
        }

        public void add(int x, int y, int spriteId) {
            ensureCapacity();
            this.x[count] = x;
            this.y[count] = y;
            this.spriteId[count] = spriteId;
            count++;
        }

        public void clear() {
            count = 0;
        }
    }

    public static final class TextTable {
        public String[] message, font, color;
        public RenderPosition[] positions;
        public int[] x, y, frames;
        public int count = 0;
        private int capacity;

        public TextTable(int capacity) {
            this.capacity = capacity;
            positions = new RenderPosition[capacity];
            message = new String[capacity];
            font = new String[capacity];
            color = new String[capacity];
            x = new int[capacity];
            y = new int[capacity];
            frames = new int[capacity];
        }

        private void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                positions = Arrays.copyOf(positions, capacity);
                message = Arrays.copyOf(message, capacity);
                font = Arrays.copyOf(font, capacity);
                color = Arrays.copyOf(color, capacity);
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                frames = Arrays.copyOf(frames, capacity);
            }
        }

        public void add(String message, String font, String color, int x, int y, int frames, RenderPosition pos) {
            ensureCapacity();
            this.message[count] = message;
            this.font[count] = font;
            this.color[count] = color;
            this.x[count] = x;
            this.y[count] = y;
            this.frames[count] = frames;
            this.positions[count] = pos;
            count++;
        }

        public void clear() {
            count = 0;
            Arrays.fill(message, null);
            Arrays.fill(font, null);
            Arrays.fill(color, null);
        }
    }

    public static final class CollisionTable {
        public float [] x, y, width, height;
        public int count = 0;
        private int capacity;

        public CollisionTable(int capacity) {
            this.capacity = capacity;
            x = new float[capacity];
            y = new float[capacity];
            width = new float[capacity];
            height = new float[capacity];
        }

        private void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
            }
        }

        public void add(float x, float y, float width, float height) {
            ensureCapacity();
            this.x[count] = x;
            this.y[count] = y;
            this.width[count] = width;
            this.height[count] = height;
            count++;
        }

        public void clear() {
            count = 0;
        }
    }

    public static final class OverlayTable {
        public int[] x, y;
        public int count;
        private int capacity;

        public OverlayTable(int capacity) {
            this.capacity = capacity;
            x = new int[capacity];
            y = new int[capacity];
        }

        private void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
            }
        }

        public void add(int x, int y) {
            ensureCapacity();
            this.x[count] = x;
            this.y[count] = y;
            count++;
        }

        public void clear() {
            count = 0;
        }
    }

}

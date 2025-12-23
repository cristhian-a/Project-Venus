package com.next.engine.graphics;

import com.next.engine.physics.CollisionBox;

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

    private void ensureCapacity() {
//        if (current >= BUFFER_SIZE) {
//            int newSize = BUFFER_SIZE * 2;
//            layer = Arrays.copyOf(layer, newSize);
//            type = Arrays.copyOf(type, newSize);
//            position = Arrays.copyOf(position, newSize);
//            sprite = Arrays.copyOf(sprite, newSize);
//            x = Arrays.copyOf(x, newSize);
//            y = Arrays.copyOf(y, newSize);
//            frames = Arrays.copyOf(frames, newSize);
//            message = Arrays.copyOf(message, newSize);
//            font = Arrays.copyOf(font, newSize);
//            color = Arrays.copyOf(color, newSize);
//            box = Arrays.copyOf(box, newSize);
//            BUFFER_SIZE = newSize;
//        }
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
        buckets[layer.ordinal()].collisions.add(box);
    }

    public void submit(Layer layer, String message, String font, String color, int x, int y, RenderPosition pos, int frames) {
        buckets[layer.ordinal()].texts.add(message, font, color, x, y, frames, pos);
    }

    public void submit(Layer layer, RenderType type) {
        buckets[layer.ordinal()].overlays.add(0, 0);
    }

    public static final class LayerBucket {
        public final TextTable texts = new TextTable(100);
        public final SpriteTable sprites = new SpriteTable(100);
        public final OverlayTable overlays = new OverlayTable(2);
        public final CollisionTable collisions = new CollisionTable(100);

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

        public SpriteTable(int capacity) {
            x = new int[capacity];
            y = new int[capacity];
            spriteId = new int[capacity];
        }

        public void add(int x, int y, int spriteId) {
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

        public TextTable(int capacity) {
            positions = new RenderPosition[capacity];
            message = new String[capacity];
            font = new String[capacity];
            color = new String[capacity];
            x = new int[capacity];
            y = new int[capacity];
            frames = new int[capacity];
        }

        public void add(String message, String font, String color, int x, int y, int frames, RenderPosition pos) {
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
        }
    }

    public static final class CollisionTable {
        public CollisionBox[] boxes;
        public int count = 0;

        public CollisionTable(int capacity) {
            boxes = new CollisionBox[capacity];
        }

        public void add(CollisionBox box) {
            boxes[count] = box;
            count++;
        }

        public void clear() {
            count = 0;
        }
    }

    public static final class OverlayTable {
        public int[] x, y;
        public int count;

        public OverlayTable(int capacity) {
            x = new int[capacity];
            y = new int[capacity];
        }

        public void add(int x, int y) {
            this.x[count] = x;
            this.y[count] = y;
            count++;
        }

        public void clear() {
            count = 0;
        }
    }

}

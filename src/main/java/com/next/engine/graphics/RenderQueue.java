package com.next.engine.graphics;

import com.next.engine.physics.AABB;
import com.next.engine.physics.CollisionBox;
import com.next.util.Colors;

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

    public void submit(Layer layer, CollisionBox box, boolean hit) {
        if (box == null) return;
        AABB bounds = box.getBounds();
        int color = hit ? Colors.WHITE : Colors.RED;
        buckets[layer.ordinal()].rectangles.add(bounds.x, bounds.y, bounds.width, bounds.height, color);
    }

    public void rectangle(Layer layer, float x, float y, float width, float height, int color) {
        buckets[layer.ordinal()].rectangles.add(x, y, width, height, color);
    }

    public void roundStrokeRect(Layer layer, float x, float y, float width, float height, int thickness, int color, int arc) {
        buckets[layer.ordinal()].roundedStrokeRectTable.add(x, y, width, height, thickness, color, arc);
    }

    public void fillRectangle(Layer layer, float x, float y, float width, float height, int color) {
        buckets[layer.ordinal()].filledRectangles.add(x, y, width, height, color);
    }

    public void fillRoundRect(Layer layer, float x, float y, float width, float height, int color, int arc) {
        buckets[layer.ordinal()].filledRoundRects.add(x, y, width, height, color, arc);
    }

    public void submit(Layer layer, String message, String font, int color, int x, int y, RenderPosition pos, int frames) {
        buckets[layer.ordinal()].texts.add(message, font, color, x, y, frames, pos);
    }

    public void submit(Layer layer, RenderType type) {
        buckets[layer.ordinal()].overlays.add(0, 0);
    }

    public static final class LayerBucket {
        public final TextTable texts = new TextTable(32);
        public final SpriteTable sprites = new SpriteTable(32);
        public final OverlayTable overlays = new OverlayTable(1);
        public final RectangleTable rectangles = new RectangleTable(16);
        public final FilledRectangleTable filledRectangles = new FilledRectangleTable(8);
        public final RoundedFilledRectTable filledRoundRects = new RoundedFilledRectTable(8);
        public final RoundedStrokeRectTable roundedStrokeRectTable = new RoundedStrokeRectTable(8);

        public void clear() {
            texts.clear();
            sprites.clear();
            overlays.clear();
            rectangles.clear();
            filledRectangles.clear();
            filledRoundRects.clear();
            roundedStrokeRectTable.clear();
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
        public String[] message, font;
        public int[] colors;
        public RenderPosition[] positions;
        public int[] x, y, frames;
        public int count = 0;
        private int capacity;

        public TextTable(int capacity) {
            this.capacity = capacity;
            positions = new RenderPosition[capacity];
            message = new String[capacity];
            font = new String[capacity];
            colors = new int[capacity];
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
                colors = Arrays.copyOf(colors, capacity);
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                frames = Arrays.copyOf(frames, capacity);
            }
        }

        public void add(String message, String font, int color, int x, int y, int frames, RenderPosition pos) {
            ensureCapacity();
            this.message[count] = message;
            this.font[count] = font;
            this.colors[count] = color;
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
        }
    }

    public static final class RectangleTable {
        public float[] x, y, width, height;
        public int[] colors;
        public int count = 0;
        private int capacity;

        public RectangleTable(int capacity) {
            this.capacity = capacity;
            x = new float[capacity];
            y = new float[capacity];
            width = new float[capacity];
            height = new float[capacity];
            colors = new int[capacity];
        }

        private void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
                colors = Arrays.copyOf(colors, capacity);
            }
        }

        public void add(float x, float y, float width, float height, int color) {
            ensureCapacity();
            this.x[count] = x;
            this.y[count] = y;
            this.width[count] = width;
            this.height[count] = height;
            this.colors[count] = color;
            count++;
        }

        public void clear() {
            count = 0;
        }
    }

    public static final class RoundedStrokeRectTable {
        public float[] x, y, width, height;
        public int[] thickness;
        public int[] colors;
        public int[] arc;
        private int capacity;
        public int count = 0;

        public RoundedStrokeRectTable(int capacity) {
            this.capacity = capacity;
            x = new float[capacity];
            y = new float[capacity];
            width = new float[capacity];
            height = new float[capacity];
            thickness = new int[capacity];
            colors = new int[capacity];
            arc = new int[capacity];
        }

        private void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
                thickness = Arrays.copyOf(thickness, capacity);
                colors = Arrays.copyOf(colors, capacity);
                arc = Arrays.copyOf(arc, capacity);
            }
        }

        public void add(float x, float y, float width, float height, int thickness, int color, int arc) {
            ensureCapacity();
            this.x[count] = x;
            this.y[count] = y;
            this.width[count] = width;
            this.height[count] = height;
            this.thickness[count] = thickness;
            this.colors[count] = color;
            this.arc[count] = arc;
            count++;
        }

        public void clear() {
            count = 0;
        }
    }

    public static final class FilledRectangleTable {
        public float[] x, y, width, height;
        public int[] colors;
        public int count = 0;
        private int capacity;

        public FilledRectangleTable(int capacity) {
            this.capacity = capacity;
            x = new float[capacity];
            y = new float[capacity];
            width = new float[capacity];
            height = new float[capacity];
            colors = new int[capacity];
        }

        private void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
                colors = Arrays.copyOf(colors, capacity);
            }
        }

        public void add(float x, float y, float width, float height, int color) {
            ensureCapacity();
            this.x[count] = x;
            this.y[count] = y;
            this.width[count] = width;
            this.height[count] = height;
            this.colors[count] = color;
            count++;
        }

        public void clear() {
            count = 0;
        }
    }

    public static final class RoundedFilledRectTable {
        public float[] x, y, width, height;
        public int[] colors;
        public int[] arc;
        private int capacity;
        public int count = 0;

        public RoundedFilledRectTable(int capacity) {
            this.capacity = capacity;
            x = new float[capacity];
            y = new float[capacity];
            width = new float[capacity];
            height = new float[capacity];
            colors = new int[capacity];
            arc = new int[capacity];
        }

        private void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
                colors = Arrays.copyOf(colors, capacity);
                arc = Arrays.copyOf(arc, capacity);
            }
        }

        public void add(float x, float y, float width, float height, int color, int arc) {
            ensureCapacity();
            this.x[count] = x;
            this.y[count] = y;
            this.width[count] = width;
            this.height[count] = height;
            this.colors[count] = color;
            this.arc[count] = arc;
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

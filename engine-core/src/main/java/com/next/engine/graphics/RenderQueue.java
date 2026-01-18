package com.next.engine.graphics;

import com.next.engine.animation.Costume;
import com.next.engine.physics.AABB;
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

    public void submit(Layer layer, float x, float y, int spriteId) {
        buckets[layer.ordinal()].sprites.add(x, y, spriteId);
    }

    public void draw(Layer layer, int sortKey, float x, float y, Costume costume) {
        buckets[layer.ordinal()].sprites.add((int) x, (int) y, costume.texture());
    }

    public void submit(Layer layer, CollisionBox box, boolean hit) {
        if (box == null) return;
        AABB bounds = box.getBounds();
        int color = hit ? 0xffffffff : 0xffff0000;
        buckets[layer.ordinal()].rectangles.add(bounds.x, bounds.y, bounds.width, bounds.height, color);
    }

    public void rectangle(Layer layer, float x, float y, float width, float height, int color) {
        buckets[layer.ordinal()].rectangles.add(x, y, width, height, color);
    }

    public void roundStrokeRect(Layer layer, float x, float y, float width, float height, int thickness, int color, int arc) {
        buckets[layer.ordinal()].roundedStrokeRectTable.add(x, y, width, height, thickness, color, arc);
    }

    public void fillRect(Layer layer, float x, float y, float width, float height, int color) {
        buckets[layer.ordinal()].filledRectangles.add(x, y, width, height, color);
    }

    public void fillRoundRect(Layer layer, float x, float y, float width, float height, int color, int arc) {
        buckets[layer.ordinal()].filledRoundRects.add(x, y, width, height, color, arc);
    }

    public void submit(Layer layer, String message, String font, int color, float x, float y, RenderPosition pos, int frames) {
        buckets[layer.ordinal()].texts.add(message, font, color, x, y, frames, pos);
    }

    public void punchLight(float x, float y, float color, float radius, float intensity, int texture) {
        buckets[Layer.LIGHTS.ordinal()].lights.add(x, y, color, radius, intensity, texture);
    }

    public void overlay(Layer layer) {
        buckets[layer.ordinal()].overlays.add(0, 0);
    }

    public static final class LayerBucket {
        public final TextTable texts = new TextTable(32);
        public final LightTable lights = new LightTable(16);
        public final SpriteTable sprites = new SpriteTable(32);
        public final OverlayTable overlays = new OverlayTable(1);
        public final RectangleTable rectangles = new RectangleTable(16);
        public final FilledRectangleTable filledRectangles = new FilledRectangleTable(8);
        public final RoundedFilledRectTable filledRoundRects = new RoundedFilledRectTable(8);
        public final RoundedStrokeRectTable roundedStrokeRectTable = new RoundedStrokeRectTable(8);

        public void clear() {
            texts.clear();
            lights.clear();
            sprites.clear();
            overlays.clear();
            rectangles.clear();
            filledRectangles.clear();
            filledRoundRects.clear();
            roundedStrokeRectTable.clear();
        }
    }

    private static sealed abstract class AbstractRenderTable {
        public float[] x, y, z;
        public int count;
        protected int capacity;

        public void clear() {
            count = 0;
        }

        protected AbstractRenderTable(int capacity) {
            this.capacity = capacity;
            x = new float[capacity];
            y = new float[capacity];
            z = new float[capacity];
        }

        protected abstract void ensureCapacity();

        protected void add(float x, float y, float z) {
            ensureCapacity();
            this.x[count] = x;
            this.y[count] = y;
            this.z[count] = z;
        }
    }

    public static final class SpriteTable extends AbstractRenderTable {
        public int[] spriteId;

        public SpriteTable(int capacity) {
            super(capacity);
            spriteId = new int[capacity];
        }

        @Override
        protected void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                z = Arrays.copyOf(z, capacity);
                spriteId = Arrays.copyOf(spriteId, capacity);
            }
        }

        public void add(float x, float y, int spriteId) {
            super.add(x, y, 0);
            this.spriteId[count] = spriteId;
            count++;
        }
    }

    public static final class TextTable extends AbstractRenderTable {
        public String[] message, font;
        public int[] colors;
        public RenderPosition[] positions;
        public int[] frames;

        public TextTable(int capacity) {
            super(capacity);
            positions = new RenderPosition[capacity];
            message = new String[capacity];
            font = new String[capacity];
            colors = new int[capacity];
            frames = new int[capacity];
        }

        @Override
        protected void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                z = Arrays.copyOf(z, capacity);
                positions = Arrays.copyOf(positions, capacity);
                message = Arrays.copyOf(message, capacity);
                font = Arrays.copyOf(font, capacity);
                colors = Arrays.copyOf(colors, capacity);
                frames = Arrays.copyOf(frames, capacity);
            }
        }

        public void add(String message, String font, int color, float x, float y, int frames, RenderPosition pos) {
            super.add(x, y, 0);
            this.message[count] = message;
            this.font[count] = font;
            this.colors[count] = color;
            this.frames[count] = frames;
            this.positions[count] = pos;
            count++;
        }

        @Override
        public void clear() {
            count = 0;
            Arrays.fill(message, null);
            Arrays.fill(font, null);
        }
    }

    public static final class RectangleTable extends AbstractRenderTable {
        public float[] width, height;
        public int[] colors;

        public RectangleTable(int capacity) {
            super(capacity);
            width = new float[capacity];
            height = new float[capacity];
            colors = new int[capacity];
        }

        @Override
        protected void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                z = Arrays.copyOf(z, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
                colors = Arrays.copyOf(colors, capacity);
            }
        }

        public void add(float x, float y, float width, float height, int color) {
            super.add(x, y, 0);
            this.width[count] = width;
            this.height[count] = height;
            this.colors[count] = color;
            count++;
        }
    }

    public static final class RoundedStrokeRectTable extends AbstractRenderTable {
        public float[] width, height;
        public int[] thickness;
        public int[] colors;
        public int[] arc;

        public RoundedStrokeRectTable(int capacity) {
            super(capacity);
            width = new float[capacity];
            height = new float[capacity];
            thickness = new int[capacity];
            colors = new int[capacity];
            arc = new int[capacity];
        }

        @Override
        protected void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                z = Arrays.copyOf(z, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
                thickness = Arrays.copyOf(thickness, capacity);
                colors = Arrays.copyOf(colors, capacity);
                arc = Arrays.copyOf(arc, capacity);
            }
        }

        public void add(float x, float y, float width, float height, int thickness, int color, int arc) {
            super.add(x, y, 0);
            this.width[count] = width;
            this.height[count] = height;
            this.thickness[count] = thickness;
            this.colors[count] = color;
            this.arc[count] = arc;
            count++;
        }
    }

    public static final class FilledRectangleTable extends AbstractRenderTable {
        public float[] width, height;
        public int[] colors;

        public FilledRectangleTable(int capacity) {
            super(capacity);
            width = new float[capacity];
            height = new float[capacity];
            colors = new int[capacity];
        }

        @Override
        protected void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                z = Arrays.copyOf(z, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
                colors = Arrays.copyOf(colors, capacity);
            }
        }

        public void add(float x, float y, float width, float height, int color) {
            super.add(x, y, 0);
            this.width[count] = width;
            this.height[count] = height;
            this.colors[count] = color;
            count++;
        }
    }

    public static final class RoundedFilledRectTable extends AbstractRenderTable {
        public float[] width, height;
        public int[] colors;
        public int[] arc;

        public RoundedFilledRectTable(int capacity) {
            super(capacity);
            width = new float[capacity];
            height = new float[capacity];
            colors = new int[capacity];
            arc = new int[capacity];
        }

        @Override
        protected void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                z = Arrays.copyOf(z, capacity);
                width = Arrays.copyOf(width, capacity);
                height = Arrays.copyOf(height, capacity);
                colors = Arrays.copyOf(colors, capacity);
                arc = Arrays.copyOf(arc, capacity);
            }
        }

        public void add(float x, float y, float width, float height, int color, int arc) {
            super.add(x, y, 0);
            this.width[count] = width;
            this.height[count] = height;
            this.colors[count] = color;
            this.arc[count] = arc;
            count++;
        }
    }

    public static final class OverlayTable extends AbstractRenderTable {

        public OverlayTable(int capacity) {
            super(capacity);
        }

        @Override
        protected void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                z = Arrays.copyOf(z, capacity);
            }
        }

        public void add(int x, int y) {
            super.add(x, y, 0);
            count++;
        }
    }

    public static final class LightTable extends AbstractRenderTable {
        public float[] colors, radius, intensity;
        public int[] textureIds;

        public LightTable(int capacity) {
            super(capacity);
            colors = new float[capacity];
            radius = new float[capacity];
            textureIds = new int[capacity];
            intensity = new float[capacity];
        }

        @Override
        protected void ensureCapacity() {
            if (count >= capacity) {
                capacity *= 2;
                x = Arrays.copyOf(x, capacity);
                y = Arrays.copyOf(y, capacity);
                z = Arrays.copyOf(z, capacity);
                colors = Arrays.copyOf(colors, capacity);
                radius = Arrays.copyOf(radius, capacity);
                intensity = Arrays.copyOf(intensity, capacity);
                textureIds = Arrays.copyOf(textureIds, capacity);
            }
        }

        public void add(float x, float y, float color, float radius, float intensity, int texture) {
            super.add(x, y, 0);
            this.colors[count] = color;
            this.radius[count] = radius;
            this.intensity[count] = intensity;
            this.textureIds[count] = texture;
            count++;
        }
    }

}

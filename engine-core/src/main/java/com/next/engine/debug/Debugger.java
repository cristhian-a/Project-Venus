package com.next.engine.debug;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.physics.AABB;

import java.util.*;

/**
 * The Debugger class provides functionality for managing and rendering debugging information,
 * such as diagnostic values and collision data, often useful in game development or system profiling.
 * It maintains a set of enabled debug channels and provides methods to publish debug instructions
 * to be rendered on screen. Debugger uses a singleton pattern through the public static {@code INSTANCE}.
 */
public final class Debugger implements DebugSink {

    private static final String FONT = "debug";

    public static final Debugger INSTANCE = new Debugger();

    private volatile Map<String, DebugRenderInstruction> renderQueue;
    private final Set<DebugChannel> enabledChannels;

    private Map<String, DebugRenderInstruction> writeBuffer;
    private Map<String, DebugRenderInstruction> readBuffer;
    private final Map<String, DebugRenderInstruction> snapshot;

    private Debugger() {
        enabledChannels = new HashSet<>();
        renderQueue = Map.of();

        writeBuffer = new LinkedHashMap<>();
        readBuffer = new LinkedHashMap<>();
        snapshot = new LinkedHashMap<>();
    }

    public void toggleChannel(DebugChannel channel) {
        if (channel == null) return;
        if (!enabledChannels.add(channel)) enabledChannels.remove(channel);
    }

    public boolean isEnabled(DebugChannel channel) {
        return enabledChannels.contains(channel);
    }

    public void update() {
        // swap to deal with concurrency
        var temp = readBuffer;
        readBuffer = writeBuffer;
        writeBuffer = temp;
        writeBuffer.clear();

        snapshot.clear();  // snapshotting to deal with concurrency

        for (var entry : readBuffer.entrySet()) {
            if (enabledChannels.contains(entry.getValue().channel)) {
                snapshot.put(entry.getKey(), entry.getValue());
            }
        }

        renderQueue = Map.copyOf(snapshot);
    }

    public void enqueueRequests(RenderQueue rq) {
        var screenBucket = rq.getBucket(Layer.DEBUG_SCREEN);
        var worldBucket = rq.getBucket(Layer.DEBUG_WORLD);

        for (var instr : renderQueue.values()) {

            DebugValue value = instr.value();
            int x = instr.x();
            int y = instr.y();

            // Text debug
            String text = value.displayInfo();
            if (text != null) {
                screenBucket.texts.add(
                        text,
                        FONT,
                        0xFF00FF00,
                        x, y,
                        0,
                        RenderPosition.AXIS
                );
            }

            // Collision / geometry debug
            AABB box = value.displayBox();
            if (box != null) {
                drawCollision(worldBucket, box);
            }
        }
    }

    private void drawCollision(RenderQueue.LayerBucket bucket, AABB aabb) {
        bucket.rectangles.add(
                aabb.x,
                aabb.y,
                aabb.width,
                aabb.height,
                0xFFFF0000  // green
        );
    }

    @Override
    public void box(String id, AABB bounds, DebugChannel channel) {
        writeBuffer.put(id, new DebugRenderInstruction(0, 0, new DebugCollision(bounds), channel));
    }

    @Override
    public void text(String id, String text, int x, int y, DebugChannel channel) {
        writeBuffer.put(id, new DebugRenderInstruction(x, y, new DebugText(text), channel));
    }

    @Override
    public void value(String id, long value, int x, int y, DebugChannel channel) {
        writeBuffer.put(id, new DebugRenderInstruction(x, y, new DebugLong(value), channel));
    }

    @Override
    public void value(String id, double value, int x, int y, DebugChannel channel) {
        writeBuffer.put(id, new DebugRenderInstruction(x, y, new DebugDouble(value), channel));
    }

    /**
     * Publishes a debug render instruction to the internal write buffer, associating it with the
     * specified key. This method is used to queue a combination of positional and visual debug data
     * to a specified debug channel.
     *
     * @param key      The unique identifier for the debug element. It serves as a key to track and
     *                 manage debug information.
     * @param value    The debug value to be associated with the key. It encapsulates the data or
     *                 visual representation to be rendered.
     * @param x        The x-coordinate of the debug element's position.
     * @param y        The y-coordinate of the debug element's position.
     * @param channel  The debug channel to which the render instruction is assigned. Used to categorize
     *                 and manage debug elements.
     */
    public static void publish(String key, DebugValue value, int x, int y, DebugChannel channel) {
        INSTANCE.writeBuffer.put(key, new DebugRenderInstruction(x, y, value, channel));
    }

    public sealed interface DebugValue {
        default String displayInfo() {
            return null;
        }
        default AABB displayBox() {
            return null;
        }
    }

    public record DebugInt(int value) implements DebugValue {
        @Override
        public String displayInfo() {
            return String.valueOf(value);
        }
    }

    public record DebugDouble(double value) implements DebugValue {
        @Override
        public String displayInfo() {
            return String.valueOf(value);
        }
    }

    public record DebugText(String value) implements DebugValue {
        @Override
        public String displayInfo() {
            return value;
        }
    }

    public record DebugLong(Long value) implements DebugValue {
        @Override
        public String displayInfo() {
            return value.toString();
        }
    }

    public record DebugCollision(AABB box) implements DebugValue {
        @Override
        public AABB displayBox() {
            return box;
        }
    }

    public record DebugRenderInstruction(int x, int y, DebugValue value, DebugChannel channel) {}
}

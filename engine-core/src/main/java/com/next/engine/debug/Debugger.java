package com.next.engine.debug;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.physics.CollisionBox;

import java.util.*;

/**
 * The Debugger class provides functionality for managing and rendering debugging information,
 * such as diagnostic values and collision data, often useful in game development or system profiling.
 * It maintains a set of enabled debug channels and provides methods to publish debug instructions
 * to be rendered on screen. Debugger uses a singleton pattern through the public static {@code INSTANCE}.
 */
public class Debugger {

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
        ProfilerAssistant.collectMemoryInfo();  // TODO move this to another place please

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

        if (snapshot.isEmpty()) return;
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
            CollisionBox box = value.displayBox();
            if (box != null) {
                drawCollision(worldBucket, box);
            }
        }
    }

    private void drawCollision(RenderQueue.LayerBucket bucket, CollisionBox box) {
        var aabb = box.getBounds();
        bucket.rectangles.add(
                aabb.x,
                aabb.y,
                aabb.width,
                aabb.height,
                0xFFFF0000  // green
        );
    }

    public static void publish(String key, DebugValue value, int x, int y, DebugChannel channel) {
        INSTANCE.writeBuffer.put(key, new DebugRenderInstruction(x, y, value, channel));
    }

    public static void publish(String key, CollisionBox box) {
        publish(key, new DebugCollision(box), 0, 0, DebugChannel.COLLISION);
    }

    public sealed interface DebugValue {
        default String displayInfo() {
            return null;
        }

        default CollisionBox displayBox() {
            return null;
        }
    }

    public record DebugInt(int value) implements DebugValue {
        @Override
        public String displayInfo() {
            return String.valueOf(value);
        }
    }

    public record DebugFloat(float value) implements DebugValue {
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

    public record DebugCollision(CollisionBox box) implements DebugValue {
        @Override
        public CollisionBox displayBox() {
            return box;
        }
    }

    public record DebugRenderInstruction(int x, int y, DebugValue value, DebugChannel channel) {}
}

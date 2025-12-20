package com.next.system;

import com.next.engine.physics.CollisionBox;

import java.util.LinkedHashMap;
import java.util.Map;


public class Debugger {

    public enum TYPE {
        INFO, COLLISION
    }

    private static final Debugger INSTANCE = new Debugger();

    private final Map<String, DebugRenderInstruction> context;
    private volatile Map<String, DebugRenderInstruction> renderQueue;

    private boolean DEBUG_1;

    private Debugger() {
        context = new LinkedHashMap<>();
        renderQueue = Map.of();
    }

    public void update() {
        Map<String, DebugRenderInstruction> snapshot = new LinkedHashMap<>();  // snapshotting to deal with concurrency

        if (DEBUG_1) {
            snapshot = Map.copyOf(context);
        }

        renderQueue = Map.copyOf(snapshot);
    }

    public static void update(Input input) {
        if (input.isPressed(Input.Action.DEBUG_1))
            INSTANCE.DEBUG_1 = !INSTANCE.DEBUG_1;

        INSTANCE.update();
    }

    public static void publish(String key, DebugValue value, int x, int y, TYPE type) {
        INSTANCE.context.put(key, new DebugRenderInstruction(x, y, value, type));
    }

    public static void publishCollision(String key, CollisionBox box) {
        publish(key, new DebugCollision(box), 0, 0, TYPE.COLLISION);
    }

    public static Map<String, DebugRenderInstruction> getRenderQueue() {
        return INSTANCE.renderQueue;
    }

    public sealed interface DebugValue {
        default String displayInfo() {
            return toString();
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

    public record DebugRenderInstruction(int x, int y, DebugValue value, TYPE type) {}
}

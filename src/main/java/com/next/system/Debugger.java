package com.next.system;

import java.util.LinkedHashMap;
import java.util.Map;

public class Debugger {

    private static final Debugger INSTANCE = new Debugger();

    private final Map<String, DebugValue> context;
    private volatile Map<String, DebugValue> publishedData;
    private boolean DEBUG_1;

    private Debugger() {
        context = new LinkedHashMap<>();
        publishedData = Map.of();
    }

    public void update() {
        Map<String, DebugValue> snapshot = new LinkedHashMap<>();   // snapshotting to deal with concurrency

        if (DEBUG_1) {
//            var fps = context.get("FPS");
//            var render = context.get("RENDER");
//
//            if (fps != null) snapshot.put("FPS", fps);
//            if (render != null) snapshot.put("RENDER", render);
            snapshot = Map.copyOf(context);
        }

        publishedData = Map.copyOf(snapshot);
    }

    public static void update(Input input) {
        if (input.isPressed(Input.Action.DEBUG_1))
            INSTANCE.DEBUG_1 = !INSTANCE.DEBUG_1;

        INSTANCE.update();
    }

    public static void put(String key, DebugValue value) {
        INSTANCE.context.put(key, value);
    }

    public static Map<String, DebugValue> getPublishedData() {
        return INSTANCE.publishedData;
    }

    public sealed interface DebugValue permits DebugInt, DebugFloat, DebugText, DebugLong {
        String display();
    }

    public record DebugInt(int value) implements DebugValue {
        @Override
        public String display() {
            return String.valueOf(value);
        }
    }

    public record DebugFloat(float value) implements DebugValue {
        @Override
        public String display() {
            return String.valueOf(value);
        }
    }

    public record DebugText(String value) implements DebugValue {
        @Override
        public String display() {
            return value;
        }
    }

    public record DebugLong(Long value) implements DebugValue {
        @Override
        public String display() {
            return value.toString();
        }
    }
}

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
            if (context.containsKey("FPS")) snapshot.put("FPS", context.get("FPS"));
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

    public sealed interface DebugValue permits DebugInt, DebugFloat, DebugText {
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
}

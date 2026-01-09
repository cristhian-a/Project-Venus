package com.next.engine.debug;

import java.util.ArrayList;
import java.util.List;

public final class DevToolkit {

    private static final List<DevTool> tools = new ArrayList<>();

    private DevToolkit() {}

    public static void register(DevTool tool) {
        tools.add(tool);
    }

    public static void unregister(DevTool tool) {
        tools.remove(tool);
    }

    public static void update() {
        for (int i = 0; i < tools.size(); i++) {
            DevTool tool = tools.get(i);
            if (tool.isEnabled()) tool.update();
        }
    }

    public static void emit(DebugSink sink) {
        for (int i = 0; i < tools.size(); i++) {
            DevTool tool = tools.get(i);
            if (tool.isEnabled()) tool.emit(sink);
        }
    }

    public static boolean isEnabled() {
        return true;
    }
}

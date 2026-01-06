package com.next.engine.system;

import com.next.engine.debug.DebugChannel;
import com.next.engine.debug.Debugger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class InputBindings {

    private final Map<String, DebugChannel> actionToChannel = new ConcurrentHashMap<>();
    private final Input input;

    public InputBindings(Input input) {
        this.input = input;
    }

    public void bindActionToChannel(String action, DebugChannel channel) {
        actionToChannel.put(action, channel);
    }

    public void process() {
        for (var entry : actionToChannel.entrySet()) {
            if (input.isTyped(entry.getKey())) {
                Debugger.INSTANCE.toggleChannel(entry.getValue());
            }
        }
    }
}

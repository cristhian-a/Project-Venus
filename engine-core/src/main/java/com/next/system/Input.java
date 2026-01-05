package com.next.system;

import com.next.engine.io.RawInputListener;

import java.util.*;

public class Input {

    private final Map<String, List<DeviceMapping>> mappings = new HashMap<>();
    private final Map<String, ActionState> actionStates = new HashMap<>();
    private final List<RawInputListener> devices = new ArrayList<>();
    private final Set<String> disabledActions = new HashSet<>();

    public void poll() {
        for (RawInputListener device : devices) {
            device.snapshot();
        }

        for (String action : mappings.keySet()) {
            boolean down = false;
            boolean pressed = false;
            boolean released = false;

            for (DeviceMapping mapping : mappings.get(action)) {
                down |= mapping.device.isDown(mapping.button);
                pressed |= mapping.device.isTyped(mapping.button);
                released |= mapping.device.isReleased(mapping.button);
            }

            var state = actionStates.get(action);
            state.down = down;
            state.typed = pressed;
            state.released = released;
        }

        for (String action : disabledActions) {
            var state = actionStates.get(action);
            if (state != null) {
                state.down = false;
                state.typed = false;
                state.released = false;
            }
        }
    }

    public void mapActions(Map<String, Integer> mappedActions, RawInputListener device) {
        devices.add(device);

        for (Map.Entry<String, Integer> entry : mappedActions.entrySet()) {
            var key = entry.getKey();
            var button = entry.getValue();
            mappings.computeIfAbsent(key, _ -> new ArrayList<>()).add(new DeviceMapping(button, device));

            actionStates.putIfAbsent(key, new ActionState());
        }
    }

    public void setActionEnabled(String action, boolean enabled) {
        if (enabled) disabledActions.remove(action);
        else disabledActions.add(action);
    }

    public boolean isTyped(String action) {
        return actionStates.get(action).typed;
    }

    public boolean isReleased(String action) {
        return actionStates.get(action).released;
    }

    public boolean isDown(String action) {
        return actionStates.get(action).down;
    }

    private record DeviceMapping(int button, RawInputListener device) {}

    private static final class ActionState {
        boolean down;
        boolean typed;
        boolean released;
    }

}

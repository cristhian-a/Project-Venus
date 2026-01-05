package com.next.system;

import com.next.engine.io.InputReader;

import java.util.*;

public class Input {

    private final List<InputReader> devices;
    private final Map<String, List<DeviceMapping>> mappings;
    private final Map<String, ActionState> actionStates;

    public Input() {
        devices = new ArrayList<>();
        mappings = new HashMap<>();
        actionStates = new HashMap<>();
    }

    public void poll() {
        for (InputReader device : devices) {
            device.snapshot();
        }

        for (String action : mappings.keySet()) {
            boolean down = false;
            boolean pressed = false;
            boolean released = false;

            for (DeviceMapping mapping : mappings.get(action)) {
                down |= mapping.device.isDown(mapping.button);
                pressed |= mapping.device.isPressed(mapping.button);
                released |= mapping.device.isReleased(mapping.button);
            }

            var state = actionStates.get(action);
            state.down = down;
            state.typed = pressed;
            state.released = released;
        }
    }

    public InputReader mapActions(Map<String, Integer> mappedActions) {
        var device = new InputReader();
        devices.add(device);

        for (Map.Entry<String, Integer> entry : mappedActions.entrySet()) {
            var key = entry.getKey();
            var button = entry.getValue();
            mappings.computeIfAbsent(key, _ -> new ArrayList<>()).add(new DeviceMapping(button, device));

            actionStates.putIfAbsent(key, new ActionState());
        }

        return device;
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

    private record DeviceMapping(int button, InputReader device) {}

    private static final class ActionState {
        boolean down;
        boolean typed;
        boolean released;
    }

}

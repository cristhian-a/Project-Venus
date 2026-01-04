package com.next.system;

import com.next.engine.io.InputReader;

import java.util.*;

public class Input {

    public enum Action {
        UP, DOWN, LEFT, RIGHT, TALK, PAUSE, DEBUG_1
    }

    private final List<InputReader> devices;
    private final EnumMap<Action, List<DeviceMapping>> mappings;
    private final EnumMap<Action, ActionState> actionStates;

    public Input() {
        devices = new ArrayList<>();
        mappings = new EnumMap<>(Action.class);
        actionStates = new EnumMap<>(Action.class);

        for (Action a : Action.values()) {
            actionStates.put(a, new ActionState());
        }
    }

    public void poll() {
        for (InputReader device : devices) {
            device.snapshot();
        }

        for (Action action : mappings.keySet()) {
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

    public InputReader mapActions(Settings.ControlSettings controls) {
        var device = new InputReader();
        devices.add(device);

        var actionUp = new DeviceMapping(controls.up, device);
        var actionDown = new DeviceMapping(controls.down, device);
        var actionLeft = new DeviceMapping(controls.left, device);
        var actionRight = new DeviceMapping(controls.right, device);
        var actionTalk = new DeviceMapping(controls.talk, device);
        var pause = new DeviceMapping(controls.pause, device);
        var debug1 = new DeviceMapping(controls.debugMode1, device);

        mappings.computeIfAbsent(Action.UP, _ -> new ArrayList<>()).add(actionUp);
        mappings.computeIfAbsent(Action.DOWN, _ -> new ArrayList<>()).add(actionDown);
        mappings.computeIfAbsent(Action.LEFT, _ -> new ArrayList<>()).add(actionLeft);
        mappings.computeIfAbsent(Action.RIGHT, _ -> new ArrayList<>()).add(actionRight);
        mappings.computeIfAbsent(Action.TALK, _ -> new ArrayList<>()).add(actionTalk);
        mappings.computeIfAbsent(Action.PAUSE, _ -> new ArrayList<>()).add(pause);
        mappings.computeIfAbsent(Action.DEBUG_1, _ -> new ArrayList<>()).add(debug1);

        return device;
    }

    public boolean isTyped(Action action) {
        return actionStates.get(action).typed;
    }

    public boolean isReleased(Action action) {
        return actionStates.get(action).released;
    }

    public boolean isDown(Action action) {
        return actionStates.get(action).down;
    }

    private record DeviceMapping(int button, InputReader device) {}

    private static final class ActionState {
        boolean down;
        boolean typed;
        boolean released;
    }

}

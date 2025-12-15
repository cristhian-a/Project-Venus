package com.next.graphics;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public final class RenderQueue {

    private final EnumMap<Layer, List<RenderInstruction>> layers = new EnumMap<>(Layer.class);

    public RenderQueue() {
        for (Layer l : Layer.values()) {
            layers.put(l, new ArrayList<>());
        }
    }

    public void submit(RenderInstruction instruction) {
        layers.get(instruction.layer()).add(instruction);
    }

    public List<RenderInstruction> getLayer(Layer layer) {
        return layers.get(layer);
    }

    public void clear() {
        layers.values().forEach(List::clear);
    }
}

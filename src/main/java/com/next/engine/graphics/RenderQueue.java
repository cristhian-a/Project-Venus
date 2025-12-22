package com.next.engine.graphics;

import com.next.engine.physics.CollisionBox;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public final class RenderQueue implements Cloneable {

    private final EnumMap<Layer, List<RenderRequest>> layers = new EnumMap<>(Layer.class);

    public RenderQueue() {
        for (Layer l : Layer.values()) {
            layers.put(l, new ArrayList<>());
        }
    }

    /**
     * Submits a render request to the renderer.
     * ONLY HERE TO SUPPORT {@code RenderRequest} CHILDREN
     * @param request a {@link RenderRequest} to be made to the renderer
     */
    public void submit(RenderRequest request) {
        layers.get(request.getLayer()).add(request);
    }

    public void submit(Layer layer, int x, int y, int spriteId) {
        layers.get(layer).add(new RenderRequest(layer, x, y, spriteId));
    }

    public void submit(Layer layer, CollisionBox box) {
        layers.get(layer).add(new RenderRequest(layer, box));
    }

    public void submit(Layer layer, String message, String font, String color, int x, int y, RenderRequest.Position pos, int frames) {
        layers.get(layer).add(new RenderRequest(layer, message, font, color, x, y, pos, frames));
    }

    public void submit(Layer layer, RenderRequest.Type type) {
        layers.get(layer).add(new RenderRequest(type, layer));
    }

    public List<RenderRequest> getLayer(Layer layer) {
        return layers.get(layer);
    }

    public void clear() {
        layers.values().forEach(List::clear);
    }

    @Override
    public RenderQueue clone() throws CloneNotSupportedException {
        return (RenderQueue) super.clone();
    }
}

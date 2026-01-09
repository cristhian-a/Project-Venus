package com.next.engine.debug;

import com.next.engine.physics.AABB;
import com.next.engine.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public final class PhysicsTool implements DevTool {

    public static final String PHYSICS_BOXES = "physics.collision.boxes";

    private final Map<String, AABB> boxes = new HashMap<>();

    public void gatherInfo(Scene scene) {
        boxes.clear();
        scene.forEachBody((body) -> {
            boxes.put(PHYSICS_BOXES + body.getId(), body.getCollisionBox().getBounds());
        });
    }

    @Override
    public void update() {
    }

    @Override
    public void emit(DebugSink sink) {
        for (Map.Entry<String, AABB> entry : boxes.entrySet()) {
            sink.box(entry.getKey(), entry.getValue(), channel());
        }
    }

    @Override
    public DebugChannel channel() {
        return DebugChannel.PHYSICS;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

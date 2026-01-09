package com.next.engine.debug;

import com.next.engine.scene.Scene;

public final class SceneTool implements DevTool {

    public static final String ENTITY_COUNT = "scene.entity.count";
    public static final String DISPOSED_ACTORS = "scene.disposed.actors";

    private static final String entityCountLabel = "ENTITIES: ";
    private static final String disposedCountLabel = "DIS. ACTORS: ";

    private int entities;
    private String entityCountMsg;

    public void gatherInfo(Scene scene) {
        if (scene.getEntityCount() != entities) {
            entities = scene.getEntityCount();
            entityCountMsg = entityCountLabel + entities;
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void emit(DebugSink sink) {
        sink.text(ENTITY_COUNT, entityCountMsg, 10, 400, channel());
    }

    @Override
    public DebugChannel channel() {
        return DebugChannel.INFO;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

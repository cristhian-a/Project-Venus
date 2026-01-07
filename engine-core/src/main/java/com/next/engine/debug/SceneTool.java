package com.next.engine.debug;

public final class SceneTool implements DevTool {

    public static final String ENTITY_COUNT = "scene.entity.count";
    public static final String DISPOSED_ACTORS = "scene.disposed.actors";

    private static final String entityCountLabel = "ENTITIES: ";
    private static final String disposedCountLabel = "DIS. ACTORS: ";

    private int disposedActors;

    private String entityCountMsg;
    private String disposedCountMsg;

    public void setEntityCount(int entityCount) {
        entityCountMsg = entityCountLabel + entityCount;
    }

    public void setDisposedActors(int disposedActors) {
        this.disposedActors = disposedActors;
        disposedCountMsg = disposedCountLabel + disposedActors;
    }

    public void onDispose() {
        disposedActors++;
        disposedCountMsg = disposedCountLabel + disposedActors;
    }

    @Override
    public void update() {
    }

    @Override
    public void emit(DebugSink sink) {
        sink.text(ENTITY_COUNT, entityCountMsg, 10, 400, channel());
        sink.text(DISPOSED_ACTORS, disposedCountMsg, 10, 430, channel());
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

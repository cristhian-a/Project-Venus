package com.next.engine.debug;

public final class PhysicsTool implements DevTool {

    @Override
    public void update() {
    }

    @Override
    public void emit(DebugSink sink) {

    }

    @Override
    public DebugChannel channel() {
        return DebugChannel.PHYSICS;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

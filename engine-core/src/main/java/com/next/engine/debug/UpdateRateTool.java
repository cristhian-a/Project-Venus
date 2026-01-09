package com.next.engine.debug;

public final class UpdateRateTool implements DevTool {

    public static final String UPDATE_RATE = "performance.update.rate";
    private static final String LABEL_UPS = "UPS: ";

    private String ups;

    public void setUps(int ups) {
        this.ups = LABEL_UPS + ups;
    }

    @Override
    public void update() {
    }

    @Override
    public void emit(DebugSink sink) {
        sink.text(UPDATE_RATE, ups, 130, 30, channel());
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

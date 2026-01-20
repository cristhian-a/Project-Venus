package com.next.engine.debug;

public final class PerformanceTool implements DevTool {

    public static final String FPS = "performance.fps";
    public static final String PHYSICS = "performance.physics";
    public static final String RENDER_TOTAL = "performance.render.total";
    public static final String RENDER_TILES = "performance.render.tiles";
    public static final String RENDER_LIGHTS = "performance.render.lights";
    public static final String RENDER_DEBUG_UI = "performance.render.debug.ui";
    public static final String DIRECTOR_UPDATE = "performance.director.update";
    public static final String RENDER_PARTICLES = "performance.render.particles";

    private static final String AVG_P95 = "avg(120): %.2f ms | p95: %.2f ms";
    private static final String FPS_LABEL = "FPS: ";

    private int frameCounter = 0;

    private String renderTotal;
    private String renderTiles;
    private String renderParticles;
    private String renderLights;
    private String renderDebugUI;
    private String directorUpdate;
    private String physicsUpdate;
    private String fpsLabel;

    @Override
    public void update() {
        if (frameCounter++ % 60 != 0) return;

        var renderTimer = DebugTimers.of(DebugTimers.RENDERER).stat();
        long renderingAvg = renderTimer.mean();
        long renderingP95 = renderTimer.percentile(0.95f);
        renderTotal = String.format(AVG_P95, renderingAvg / 1e6f, renderingP95 / 1e6f);

        long fps = (long) (1e9 / renderTimer.percentile(0.5f));
        fpsLabel = FPS_LABEL + fps;

        var particleTimer = DebugTimers.of(DebugTimers.RENDER_PARTICLES).stat();
        long particleAvg = particleTimer.mean();
        long particleP95 = particleTimer.percentile(0.95f);
        renderParticles = String.format(AVG_P95, particleAvg / 1e6f, particleP95 / 1e6f);

        var lightTimer = DebugTimers.of(DebugTimers.RENDER_LIGHTS).stat();
        long lightingAvg = lightTimer.mean();
        long lightingP95 = lightTimer.percentile(0.95f);
        renderLights = String.format(AVG_P95, lightingAvg / 1e6f, lightingP95 / 1e6f);

        var tileTimer = DebugTimers.of(DebugTimers.RENDER_TILES).stat();
        long tileAvg = tileTimer.mean();
        long tileP95 = tileTimer.percentile(0.95f);
        renderTiles = String.format(AVG_P95, tileAvg / 1e6f, tileP95 / 1e6f);

        var uiTimer = DebugTimers.of(DebugTimers.RENDER_DEBUG_UI).stat();
        long uiAvg = uiTimer.mean();
        long uiP95 = uiTimer.percentile(0.95f);
        renderDebugUI = String.format(AVG_P95, uiAvg / 1e6f, uiP95 / 1e6f);

        var gameTimer = DebugTimers.of(DebugTimers.UPDATE).stat();
        long gameAvg = gameTimer.mean();
        long gameP95 = gameTimer.percentile(0.95f);
        directorUpdate = String.format(AVG_P95, gameAvg / 1e6f, gameP95 / 1e6f);

        var physicsTimer = DebugTimers.of(DebugTimers.PHYSICS).stat();
        long physicsAvg = physicsTimer.mean();
        long physicsP95 = physicsTimer.percentile(0.95f);
        physicsUpdate = String.format(AVG_P95, physicsAvg / 1e6f, physicsP95 / 1e6f);
    }

    @Override
    public void emit(DebugSink sink) {
        sink.text(RENDER_TOTAL, renderTotal, 660, 30, channel());
        sink.text(RENDER_TILES, renderTiles, 660, 60, channel());
        sink.text(RENDER_PARTICLES, renderParticles, 660, 90, channel());
        sink.text(RENDER_LIGHTS, renderLights, 660, 120, channel());
        sink.text(RENDER_DEBUG_UI, renderDebugUI, 660, 150, channel());
        sink.text(DIRECTOR_UPDATE, directorUpdate, 660, 260, channel());
        sink.text(PHYSICS, physicsUpdate, 660, 290, channel());
        sink.text(FPS, fpsLabel, 10, 30, channel());
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

package com.next.engine.debug;

public final class Tools {
    private Tools() {}

    public static final PerformanceTool PERFORMANCE_TOOL = new PerformanceTool();
    public static final UpdateRateTool UPDATE_RATE_TOOL = new UpdateRateTool();
    public static final PhysicsTool PHYSICS_TOOL = new PhysicsTool();
    public static final MemoryTool MEMORY_TOOL = new MemoryTool();
    public static final SceneTool SCENE_TOOL = new SceneTool();

    public static void registerTools() {
        DevToolkit.register(PERFORMANCE_TOOL);
        DevToolkit.register(UPDATE_RATE_TOOL);
        DevToolkit.register(PHYSICS_TOOL);
        DevToolkit.register(MEMORY_TOOL);
        DevToolkit.register(SCENE_TOOL);
    }
}

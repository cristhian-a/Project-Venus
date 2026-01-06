package com.next.engine.debug;

import java.lang.management.*;

public final class ProfilerAssistant {

    private static final String HEAP_USED = "HEAP used";
    private static final String HEAP_COMMITED = "HEAP comm";
    private static final String HEAP_MAX = "HEAP max";
    private static final String GC_COLLECTIONS = "GC collections";
    private static final String GC_TIME = "GC time";
    private static final String CLASSES_LOADED = "CLASSES loaded";
    private static final String CLASSES_TOTAL = "CLASSES total";
    private static final String CLASSES_UNLOADED = "CLASSES unloaded";

    public static void collectMemoryInfo() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();

        MemoryUsage heap = mem.getHeapMemoryUsage();
        long used = heap.getUsed();
        long commited = heap.getCommitted();
        long max = heap.getMax();

        String usedMsg = HEAP_USED + ": " + used + " bytes";
        String commMsg = HEAP_COMMITED + ": " + commited + " bytes";
        String maxMsg = HEAP_MAX + ": " + max + " bytes";
        Debugger.publish("HEAP used", new Debugger.DebugText(usedMsg), 10, 200, DebugChannel.MEMORY);
        Debugger.publish("HEAP comm", new Debugger.DebugText(commMsg), 10, 230, DebugChannel.MEMORY);
        Debugger.publish("HEAP max", new Debugger.DebugText(maxMsg), 10, 260, DebugChannel.MEMORY);

        long gcCollections = 0;
        long time = 0;
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcCollections += gc.getCollectionCount();
            time += gc.getCollectionTime();
        }

        String gcMsg = GC_COLLECTIONS + ": " + gcCollections;
        String gcTimeMsg = GC_TIME + ": " + time + " ms";
        Debugger.publish("GC collections", new Debugger.DebugText(gcMsg), 10, 300, DebugChannel.MEMORY);
        Debugger.publish("GC time", new Debugger.DebugText(gcTimeMsg), 10, 330, DebugChannel.MEMORY);

        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
        long loaded = cl.getLoadedClassCount();
        long totalLoaded = cl.getTotalLoadedClassCount();
        long unloaded = cl.getUnloadedClassCount();

        String loadedMsg = CLASSES_LOADED + ": " + loaded;
        String totalMsg = CLASSES_TOTAL + ": " + totalLoaded;
        String unloadedMsg = CLASSES_UNLOADED + ": " + unloaded;

        Debugger.publish("CLASSES loaded", new Debugger.DebugText(loadedMsg), 10, 360, DebugChannel.MEMORY);
        Debugger.publish("CLASSES total", new Debugger.DebugText(totalMsg), 10, 390, DebugChannel.MEMORY);
        Debugger.publish("CLASSES unloaded", new Debugger.DebugText(unloadedMsg), 10, 420, DebugChannel.MEMORY);
    }
}

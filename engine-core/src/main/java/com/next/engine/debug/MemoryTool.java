package com.next.engine.debug;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public final class MemoryTool implements DevTool {

    public static final String HEAP_USED = "memory.heap.used";
    public static final String HEAP_COMMITED = "memory.heap.commited";
    public static final String HEAP_MAX = "memory.heap.max";
    public static final String GC_COLLECTIONS = "memory.gc.collections";
    public static final String GC_TIME = "memory.gc.time";

    private static final String LABEL_HEAP_USED = "HEAP used: ";
    private static final String LABEL_HEAP_COMMITED = "HEAP comm: ";
    private static final String LABEL_HEAP_MAX = "HEAP max: ";
    private static final String LABEL_GC_COLLECTIONS = "GC collections: ";
    private static final String LABEL_GC_TIME = "GC time: ";
    private static final String LABEL_CLASSES_LOADED = "CLASSES loaded: ";
    private static final String LABEL_CLASSES_TOTAL = "CLASSES total: ";
    private static final String LABEL_CLASSES_UNLOADED = "CLASSES unloaded: ";

    private long used, committed, max;
    private long gcCount, gcTime;

    private int frameCounter = 0;

    @Override
    public void update() {
        if (frameCounter++ % 60 != 0) return;

        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = mem.getHeapMemoryUsage();

        used = heap.getUsed();
        committed = heap.getCommitted();
        max = heap.getMax();

        gcCount = 0;
        gcTime = 0;
        for (var gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcCount += gc.getCollectionCount();
            gcTime += gc.getCollectionTime();
        }
    }

    @Override
    public void emit(DebugSink sink) {
        sink.text(HEAP_USED, LABEL_HEAP_USED + used, 10, 200, channel());
        sink.text(HEAP_COMMITED, LABEL_HEAP_COMMITED + committed, 10, 230, channel());
        sink.text(HEAP_MAX,  LABEL_HEAP_MAX + max, 10, 260, channel());

        sink.text(GC_COLLECTIONS, LABEL_GC_COLLECTIONS + gcCount, 10, 300, channel());
        sink.text(GC_TIME,  LABEL_GC_TIME + gcTime + " ms", 10, 330, channel());
    }

    @Override
    public DebugChannel channel() {
        return DebugChannel.MEMORY;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

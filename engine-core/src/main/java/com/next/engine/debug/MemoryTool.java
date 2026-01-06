package com.next.engine.debug;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public final class MemoryTool implements DevTool {

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
        sink.text("heap.used", "HEAP used: " + used, 10, 200, channel());
        sink.text("heap.comm", "HEAP comm: " + committed, 10, 230, channel());
        sink.text("heap.max",  "HEAP max: " + max, 10, 260, channel());

        sink.text("gc.count", "GC collections: " + gcCount, 10, 300, channel());
        sink.text("gc.time",  "GC time: " + gcTime + " ms", 10, 330, channel());
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

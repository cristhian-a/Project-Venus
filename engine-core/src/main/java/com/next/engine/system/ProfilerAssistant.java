package com.next.engine.system;

import java.lang.management.*;

public final class ProfilerAssistant {

    public static void collectMemoryInfo() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();

        MemoryUsage heap = mem.getHeapMemoryUsage();
        long used = heap.getUsed();
        long commited = heap.getCommitted();
        long max = heap.getMax();

        Debugger.publish("HEAP used", new Debugger.DebugLong(used), 10, 200, Debugger.TYPE.INFO);
        Debugger.publish("HEAP comm", new Debugger.DebugLong(commited), 10, 230, Debugger.TYPE.INFO);
        Debugger.publish("HEAP max", new Debugger.DebugLong(max), 10, 260, Debugger.TYPE.INFO);

        long gcCollections = 0;
        long time = 0;
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcCollections += gc.getCollectionCount();
            time += gc.getCollectionTime();
        }

        Debugger.publish("GC collections", new Debugger.DebugLong(gcCollections), 10, 300, Debugger.TYPE.INFO);
        Debugger.publish("GC time", new Debugger.DebugLong(time), 10, 330, Debugger.TYPE.INFO);

        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
        long loaded = cl.getLoadedClassCount();
        long totalLoaded = cl.getTotalLoadedClassCount();
        long unloaded = cl.getUnloadedClassCount();

        Debugger.publish("CLASSES loaded", new Debugger.DebugLong(loaded), 10, 360, Debugger.TYPE.INFO);
        Debugger.publish("CLASSES total", new Debugger.DebugLong(totalLoaded), 10, 390, Debugger.TYPE.INFO);
        Debugger.publish("CLASSES unloaded", new Debugger.DebugLong(unloaded), 10, 420, Debugger.TYPE.INFO);
    }
}

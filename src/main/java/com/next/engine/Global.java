package com.next.engine;

public final class Global {

    public static int targetFPS = 60;
    public static double fixedDelta = 1d / 60d;

    public static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) {
                return t;
            }
        }
        return null; // Thread not found
    }
}

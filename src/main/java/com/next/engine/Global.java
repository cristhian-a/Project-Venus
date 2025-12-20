package com.next.engine;

import com.next.Loop;
import lombok.Setter;

public final class Global {

    @Setter private static Loop gameLoop;

    public static int targetFPS = 60;
    public static double fixedDelta = 1d / 60d;

    public static void end() {
        gameLoop.finishGame();
    }

    public static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) {
                return t;
            }
        }
        return null; // Thread not found
    }
}

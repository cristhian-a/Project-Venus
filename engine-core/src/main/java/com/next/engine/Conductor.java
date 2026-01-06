package com.next.engine;

import com.next.engine.debug.DevToolkit;
import com.next.engine.graphics.GamePanel;
import com.next.engine.debug.DebugChannel;
import com.next.engine.debug.Debugger;
import com.next.engine.system.Input;
import com.next.engine.system.InputBindings;

/**
 * Manages the main game loop and synchronizes everything.
 */
public class Conductor implements Runnable {

    private final Director director;
    private final Input input;
    private final GamePanel panel;
    private final InputBindings inputBindings;

    private Thread mainThread;
    private boolean running;

    public Conductor(Director director, GamePanel panel, Input input, InputBindings inputBindings) {
        this.director = director;
        this.panel = panel;
        this.input = input;
        this.inputBindings = inputBindings;
    }

    public void start() {
        running = true;
        mainThread = new Thread(this, "Game Conductor Thread");

        director.init();
        panel.openWindow();
        mainThread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            mainThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops the game loop after finishing the current cycle.
     */
    public void gracefullyStop() {
        running = false;
    }

    @Override
    public void run() {
        final double fixedDelta = Global.fixedDelta;
        double accumulator = 0.0;
        double lastTime = System.nanoTime();

        // Debug info *(frame rate)*
        int frames = 0;
        double timer = System.currentTimeMillis();
        int framesLastSecond = 0;

        boolean shouldRender = false;

        while (running) {
            double now = System.nanoTime();
            double delta = (now - lastTime) / 1e9;
            lastTime = now;
            accumulator += delta;

            // Accumulating time into Global.time
            Global.accumulateTime(delta);

            while (accumulator >= fixedDelta) {
                input.poll();
                inputBindings.process();

                DevToolkit.update();
                DevToolkit.emit(Debugger.INSTANCE);
                Debugger.INSTANCE.update();

                director.update(fixedDelta);

                accumulator -= fixedDelta;
                shouldRender = true;

                frames++;   // Debug info *(frame rate)*
                Debugger.publish("FPS", new Debugger.DebugInt(framesLastSecond), 10, 30, DebugChannel.INFO);
            }

            if (shouldRender) {
                panel.requestRender();
                shouldRender = false;
            }

            // Debug info *(frame rate)*
            if (System.currentTimeMillis() - timer >= 1000) {
                framesLastSecond = frames;
                frames = 0;
                timer = System.currentTimeMillis();
            }
        }

        stop();
    }
}

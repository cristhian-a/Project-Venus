package com.next;

import com.next.engine.Global;
import com.next.engine.graphics.GamePanel;
import com.next.engine.system.Debugger;
import com.next.system.Input;

public class Loop implements Runnable {

    private final Game game;
    private final Input input;
    private final GamePanel panel;

    private Thread mainThread;
    private boolean running;

    public Loop(Game game, GamePanel panel, Input input) {
        this.game = game;
        this.panel = panel;
        this.input = input;
    }

    public void start() {
        running = true;
        mainThread = new Thread(this, "Main Game Thread");

        game.start();
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

        while (running) {
            double now = System.nanoTime();
            double delta = (now - lastTime) / 1e9;
            lastTime = now;
            accumulator += delta;

            while (accumulator >= fixedDelta) {
                input.poll();
                Debugger.update(input);
                game.update(delta);
                panel.requestRender();

                accumulator -= fixedDelta;
                frames++;   // Debug info *(frame rate)*
                Debugger.publish("FPS", new Debugger.DebugInt(framesLastSecond), 10, 30, Debugger.TYPE.INFO);
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

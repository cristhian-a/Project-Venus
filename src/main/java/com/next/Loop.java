package com.next;

import com.next.graphics.GamePanel;
import com.next.system.Debugger;
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

    public void gracefullyEnd() {
        running = false;
    }

    @Override
    public void run() {
        final double fixedDelta = 1.0 / 60.0;
        double accumulator = 0.0;
        double lastTime = System.nanoTime();

        // Debug info *(frame rate)*
        int frames = 0;
        double timer = System.currentTimeMillis();

        while (running) {
            double now = System.nanoTime();
            double delta = (now - lastTime) / 1e9;
            lastTime = now;
            accumulator += delta;

            while (accumulator >= fixedDelta) {
                input.poll();
                Debugger.update(input);
                game.update(fixedDelta);
                panel.requestRender();

                accumulator -= fixedDelta;
                frames++;   // Debug info *(frame rate)*
            }

            // Debug info *(frame rate)*
            if (System.currentTimeMillis() - timer >= 1000) {
                Debugger.publish("FPS", new Debugger.DebugInt(frames), 10, 30, Debugger.TYPE.INFO);
                frames = 0;
                timer = System.currentTimeMillis();
            }
        }

        stop();
    }
}

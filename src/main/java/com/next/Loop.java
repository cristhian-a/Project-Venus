package com.next;

import com.next.graphic.Renderer;
import com.next.system.Input;

public class Loop implements Runnable {

    private final Game game;
    private final Renderer renderer;
    private final Input input;

    private Thread mainThread;
    private boolean running;

    public Loop(Game game, Renderer renderer, Input input) {
        this.game = game;
        this.renderer = renderer;
        this.input = input;
    }

    public void start() {
        running = true;
        mainThread = new Thread(this, "Main Thread");

        renderer.openWindow();
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
                game.update(fixedDelta);

                accumulator -= fixedDelta;
                frames++;   // Debug info *(frame rate)*
            }

            renderer.render();

            // Debug info *(frame rate)*
            if (System.currentTimeMillis() - timer >= 1000) {
                if (game.DEBUG_MODE_1) IO.println("FPS: " + frames);
                frames = 0;
                timer = System.currentTimeMillis();
            }
        }
    }
}

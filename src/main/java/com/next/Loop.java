package com.next;

import com.next.graphic.Renderer;

public class Loop implements Runnable {

    private final Game game;
    private final Renderer renderer;

    private Thread mainThread;
    private boolean running;

    public Loop(Game game, Renderer renderer) {
        this.game = game;
        this.renderer = renderer;
    }

    public void start() {
        running = true;
        mainThread = new Thread(this, "Main Thread");

        renderer.open();
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
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            double delta = (now - lastTime) / 1000000000.0;
            lastTime = now;

            game.update(delta);
            renderer.render();
        }
    }
}

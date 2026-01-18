package com.next.engine.scene;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Particle;
import com.next.engine.model.Renderable;
import com.next.engine.model.Updatable;

public final class ParticleSystem implements Updatable, Renderable {

    private final Particle[] particles;
    private int aliveCount = 0;

    public ParticleSystem(int max) {
        particles = new Particle[max];
    }

    public void spawn(Particle particle) {
        if (aliveCount < particles.length) {
            particles[aliveCount++] = particle;
        }
    }

    @Override
    public void update(double delta) {
        for (int i = 0; i < aliveCount; i++) {
            Particle p = particles[i];

            p.life -= delta;
            if (p.life <= 0) {
                kill(i);
                i--;
                continue;
            }

            p.x += (float) (p.vx * delta);
            p.y += (float) (p.vy * delta);
        }
    }

    public void kill(int index) {
        particles[index] = particles[--aliveCount];
    }

    @Override
    public void collectRender(RenderQueue queue) {
        for (int i = 0; i < aliveCount; i++) {
            Particle p = particles[i];
            queue.fillRectangle(Layer.PARTICLES, p.x, p.y, p.size, p.size, p.color);
        }
    }
}

package com.next.engine.scene;

import com.next.engine.model.Particle;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ParticleEmitter {
    public double rate;
    public double accumulator;
    public Supplier<Particle> factory;

    public void update(double delta, ParticleSystem system) {
        accumulator += delta * rate;

        int count = (int) accumulator;
        accumulator -= count;

        for (int i = 0; i < count; i++) {
            system.spawn(factory.get());
        }
    }

    public void update(double delta, Consumer<Particle> consumer) {
        accumulator += delta * rate;
        int count = (int) accumulator;
        accumulator -= count;
        for (int i = 0; i < count; i++) {
            consumer.accept(factory.get());
        }
    }
}

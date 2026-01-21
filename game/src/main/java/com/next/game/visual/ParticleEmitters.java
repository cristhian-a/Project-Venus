package com.next.game.visual;

import com.next.engine.model.Particle;
import com.next.engine.scene.ParticleEmitter;
import com.next.engine.util.PCG32;
import com.next.engine.util.Rng;
import com.next.game.config.ConfigUtils;
import com.next.game.util.Colors;

public final class ParticleEmitters {
    private ParticleEmitters() {}

    private static final Rng rng = new PCG32(ConfigUtils.SEED, 99);

    public static ParticleEmitter smoke(float x, float y) {
        ParticleEmitter smoke = new ParticleEmitter();
        smoke.rate = 30;
        smoke.factory = () -> {
            Particle p = new Particle();
            p.color = Colors.GRAY;
            p.x = x + rng.rollDice(14) - 7;
            p.y = y;
            p.vx = rng.rollDice(20) - 10;
            p.vy = rng.rollDice(20) - 40;
            p.life = rng.rollDice(2) + 1;
            p.size = rng.rollDice(5) + 1;
            return p;
        };

        return smoke;
    }
}

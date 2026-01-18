package com.next.engine.scene;

import com.next.engine.data.Mailbox;
import com.next.engine.model.Particle;

public interface SceneContext {
    Mailbox mailbox();
    void emitParticle(Particle particle);
}

package com.next.game.event.handlers;

import com.next.engine.event.GameEvent;

public interface InteractionContext {
    void dispatch(GameEvent event);
}

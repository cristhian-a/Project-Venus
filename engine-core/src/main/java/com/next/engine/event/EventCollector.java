package com.next.engine.event;

import java.util.function.Supplier;

public interface EventCollector {
    void post(Supplier<? extends GameEvent> eventFactory);
}

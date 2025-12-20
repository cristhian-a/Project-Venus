package com.next.event;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.event.GameEvent;
import com.next.engine.event.GracefullyStopEvent;
import com.next.graphics.Layer;
import com.next.graphics.RenderRequest;

public record FinishGameEvent() implements GameEvent {

    public static class Handler {
        private final Mailbox mailbox;
        private final EventDispatcher dispatcher;

        public Handler(EventDispatcher dispatcher, Mailbox mailbox) {
            this.mailbox = mailbox;
            this.dispatcher = dispatcher;

            dispatcher.register(FinishGameEvent.class, this::onFire);
        }

        public void onFire(FinishGameEvent event) {
            mailbox.renderQueue.submit(Layer.UI, "You Win!", -50, -25, RenderRequest.Position.CENTERED, 1);
            dispatcher.dispatch(new GracefullyStopEvent());
        }
    }
}

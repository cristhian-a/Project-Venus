package com.next.ui;

import com.next.engine.event.EventDispatcher;
import com.next.engine.event.ExitEvent;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.event.StartGameEvent;
import com.next.system.Input;
import com.next.engine.util.Colors;
import com.next.engine.util.Fonts;

public class StartMenuUIState implements UIState {

    private final Input input;
    private final EventDispatcher dispatcher;

    private final String NAME = "PROTOTYPE";
    private final String START = "New";
    private final String LOAD = "Load";
    private final String EXIT = "Quit";
    private final String CURSOR = "<          >";

    private int cursor;

    public StartMenuUIState(Input input, EventDispatcher dispatcher) {
        this.input = input;
        this.dispatcher = dispatcher;
    }

    @Override
    public void update(double delta) {
        if (input.isTyped(Input.Action.UP)) {
            cursor--;
        }

        if (input.isTyped(Input.Action.DOWN)) {
            cursor++;
        }

        cursor = Math.clamp(cursor, 0, 2);

        if (input.isTyped(Input.Action.TALK)) {
            switch (cursor) {
                case 0 -> dispatcher.dispatch(new StartGameEvent());
                case 1 -> {}
                case 2 -> dispatcher.dispatch(new ExitEvent());
            }
        }
    }

    @Override
    public void submitRender(RenderQueue queue) {
        queue.submit(
                Layer.UI,
                CURSOR,
                Fonts.DEFAULT,
                Colors.WHITE,
                0,
                50 * cursor,
                RenderPosition.CENTERED,
                0
        );

        queue.submit(
                Layer.UI,
                NAME,
                Fonts.DEFAULT_80_BOLD,
                Colors.RED,
                0,
                -100,
                RenderPosition.CENTERED,
                0
        );

        queue.submit(
                Layer.UI,
                START,
                Fonts.DEFAULT,
                Colors.WHITE,
                0,
                0,
                RenderPosition.CENTERED,
                0
        );

        queue.submit(
                Layer.UI,
                LOAD,
                Fonts.DEFAULT,
                Colors.WHITE,
                0,
                50,
                RenderPosition.CENTERED,
                0
        );

        queue.submit(
                Layer.UI,
                EXIT,
                Fonts.DEFAULT,
                Colors.WHITE,
                0,
                100,
                RenderPosition.CENTERED,
                0
        );
    }
}

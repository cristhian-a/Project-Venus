package com.next.engine.ui.component;

public final class ActionComponent implements UIComponent {
    private final Runnable action;

    public ActionComponent(Runnable action) {
        this.action = action;
    }

    public void fire() {
        action.run();
    }
}

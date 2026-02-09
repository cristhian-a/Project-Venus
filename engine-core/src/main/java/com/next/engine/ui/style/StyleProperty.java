package com.next.engine.ui.style;

import static com.next.engine.ui.style.StyleValue.*;

public enum StyleProperty {
    BACKGROUND_COLOR {
        final void apply(ComputedStyle s, StyleValue v) {
            s.backgroundColor = ((Color) v).argb();
        }
    },
    BORDER_COLOR {
        final void apply(ComputedStyle s, StyleValue v) {
            s.borderColor = ((Color) v).argb();
        }
    },
    TEXT_COLOR {
        final void apply(ComputedStyle s, StyleValue v) {
            s.textColor = ((Color) v).argb();
        }
    },
    FONT_SIZE {
        final void apply(ComputedStyle s, StyleValue v) {
            s.fontSize = (int) ((Length) v).value();
        }
    },
    CORNER_RADIUS {
        final void apply(ComputedStyle s, StyleValue v) {
            s.cornerRadius = (int) ((Length) v).value();
        }
    },
    CURSOR_SYMBOL {
        final void apply(ComputedStyle s, StyleValue v) {
            s.cursorSymbol = ((Keyword) v).value();
        }
    };

    abstract void apply(ComputedStyle s, StyleValue v);

    public static StyleProperty of(String name) {
        return switch (name) {
            case "backgroundColor" -> BACKGROUND_COLOR;
            case "borderColor" -> BORDER_COLOR;
            case "textColor" -> TEXT_COLOR;
            case "fontSize" -> FONT_SIZE;
            case "cornerRadius" -> CORNER_RADIUS;
            case "cursorSymbol" -> CURSOR_SYMBOL;
            default -> throw new IllegalArgumentException("Unknown style property: " + name);
        };
    }
}

package com.next.engine.graphics.awt;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

final class RenderCache {
    private RenderCache() {}
    final static RenderCache INSTANCE = new RenderCache();

    final Map<Integer, BufferedImage> coloredLights = new HashMap<>();
    final Map<Float, Stroke> strokes = new HashMap<>();
    final Map<Integer, Color> colors = new HashMap<>();
    final Map<String, Font> fonts = new HashMap<>();

    Color getColor(int argb) {
        return colors.computeIfAbsent(argb, this::newColor);
    }

    private Color newColor(int argb) { return new Color(argb, true); }

    Stroke getStroke(float width) {
        return strokes.computeIfAbsent(width, BasicStroke::new);
    }

    BufferedImage getColoredLight(int color, Supplier<BufferedImage> supplier) {
        return coloredLights.computeIfAbsent(color, _ -> supplier.get());
    }
}

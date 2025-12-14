package com.next.system;

import com.next.graphics.awt.SpriteSheet;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssetRegistry {

    // Awt resources (remember that)
    private final Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private final Map<String, Font> fonts = new HashMap<>();

    public void load() {
        try {
            loadSprites();
            loadFonts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSprites() throws IOException {
        spriteSheets.put("world", new SpriteSheet("/sprites/spritesheet.png", 16, 16, 4));
    }

    private void loadFonts() {
        fonts.put("arial_30", new Font("Arial", Font.PLAIN, 30));
    }

    public SpriteSheet getSpriteSheet(String name) {
        return spriteSheets.get(name);
    }

    public Font getFont(String name) {
        return fonts.get(name);
    }
}

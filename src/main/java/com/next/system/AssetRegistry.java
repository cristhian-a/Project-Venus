package com.next.system;

import com.next.engine.graphics.awt.SpriteSheet;
import com.next.engine.io.FileReader;
import com.next.util.Colors;
import com.next.util.Fonts;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AssetRegistry {

    private final Map<String, Integer[][]> tileMaps = new HashMap<>();

    // Awt resources (remember that)
    private final Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private final Map<String, Font> fonts = new HashMap<>();
    private final Map<String, Color> colors = new HashMap<>();

    public void load() {
        try {
            loadSprites();
            loadFonts();
            loadMaps();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSprites() throws IOException {
        spriteSheets.put("world", new SpriteSheet("/sprites/spritesheet.png", 16, 16));
    }

    private void loadFonts() {
        fonts.put(Fonts.ARIAL_30, new Font("Arial", Font.PLAIN, 30));
        fonts.put(Fonts.ARIAL_80_BOLD, new Font("Arial", Font.BOLD, 80));

        colors.put(Colors.WHITE, Color.WHITE);
        colors.put(Colors.BLACK, Color.BLACK);
        colors.put(Colors.RED, Color.RED);
        colors.put(Colors.GREEN, Color.GREEN);
        colors.put(Colors.BLUE, Color.BLUE);
        colors.put(Colors.YELLOW, Color.YELLOW);
        colors.put(Colors.ORANGE, Color.ORANGE);
        colors.put(Colors.PINK, Color.PINK);
        colors.put(Colors.GRAY, Color.GRAY);
        colors.put(Colors.MAGENTA, Color.MAGENTA);
    }

    private void loadMaps() throws IOException {
        Integer[][] tileMap = new Integer[50][50];

        InputStream is = FileReader.getFile("/maps/map_01.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            int row = 0;

            while (row < 50) {
                String line = br.readLine();
                int col = 0;

                while (col < 50) {
                    String[] numbers = line.split(" ");
                    tileMap[row][col] = Integer.parseInt(numbers[col]);
                    col++;
                }

                row++;
            }
        }

        tileMaps.put("map_01", tileMap);
    }

    public SpriteSheet getSpriteSheet(String name) { return spriteSheets.get(name); }
    public Font getFont(String name) { return fonts.get(name); }
    public Integer[][] getTileMap(String name) { return tileMaps.get(name); }
    public Color getColor(String name) { return colors.get(name); }
}

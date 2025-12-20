package com.next.engine.graphics.awt;

import com.next.io.FileReader;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {
    @Getter private final int spriteWidth;
    @Getter private final int spriteHeight;
    private final int columns;
    private final int rows;
    private final int count;

    private final BufferedImage image;
    private final BufferedImage[] sprites;

    public SpriteSheet(String path, int spriteWidth, int spriteHeight) throws IOException {
        this.image = ImageIO.read(FileReader.getFile(path));
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;

        columns = image.getWidth() / spriteWidth;
        rows = image.getHeight() / spriteHeight;
        count = columns * rows;

        sprites = new BufferedImage[count];
        splitImages();
    }

    public BufferedImage getSprite(int index) {
        return sprites[index];
    }

    public BufferedImage getSprite(int x, int y) {
        if (x % spriteWidth != 0 && y % spriteHeight != 0)
            throw new IllegalArgumentException("Sprite coordinates must be divisible by tile size");

        int col = x / spriteWidth;
        int row = y / spriteHeight;

        int index = row * columns + col;
        return sprites[index];
    }

    private void splitImages() {
        int y = 0;
        int rowCount = 0;
        int index = 0;

        while (rowCount < rows) {
            int x = 0;
            int columnCount = 0;
            while (columnCount < columns) {
                if (index == count) break;

                var img = getSubImage(x, y, spriteWidth, spriteHeight);
                sprites[index] = img;
                index++;

                x += spriteWidth;
                columnCount++;
            }

            y += spriteHeight;
            rowCount++;
        }
    }

    private BufferedImage getSubImage(int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }
}

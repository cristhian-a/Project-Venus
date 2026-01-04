package com.next.engine.data;

import com.next.engine.dto.Frame;
import com.next.engine.dto.Point;
import com.next.engine.dto.Rectangle;
import com.next.engine.dto.TextureMetadata;
import com.next.engine.graphics.Sprite;

import java.awt.image.BufferedImage;
import java.util.List;

public class AtlasImporter {

    public static void register(BufferedImage sheet, TextureMetadata metadata) {
        if (sheet == null || metadata == null) {
            throw new IllegalArgumentException("Sheet or metadata cannot be null");
        }

        Registry.textures.put(99, sheet);   // Never actually queried, just here for safety

        List<Frame> frames = metadata.getFrames();
        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            String filename = frame.getFilename();
            Rectangle coordinates = frame.getFrame();
            Point pivot = frame.getPivot();
            float pivotX = 0, pivotY = 0;
            if (pivot != null) {
                pivotX = coordinates.width * frame.getPivot().x;
                pivotY = coordinates.height * frame.getPivot().y;
            }

            var subImage = sheet.getSubimage(coordinates.x, coordinates.y, coordinates.width, coordinates.height);

            Sprite sprite = new Sprite(
                    i, filename,
                    coordinates.x, coordinates.y, coordinates.width, coordinates.height,
                    pivotX, pivotY,
                    subImage
            );

            Registry.textureIds.put(filename, i);
            Registry.sprites.put(i, sprite);
        }
    }
}
